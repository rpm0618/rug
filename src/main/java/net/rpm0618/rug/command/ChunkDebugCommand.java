
package net.rpm0618.rug.command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;

import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.world.ServerWorld;

import com.google.gson.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class ChunkDebugCommand extends AbstractCommand {

    private static class ChunkMetadata {
        private static class ChunkMetadataJson {
            String stackTrace;
            String custom;
        }

        StackTraceElement[] stackTrace;
        String custom;

        public String toJson() {
            ChunkMetadataJson metadata = new ChunkMetadataJson();
            metadata.custom = custom;

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            for (int i = 0; i < this.stackTrace.length; i++) {
                pw.println(this.stackTrace[i].toString());
            }
            metadata.stackTrace = sw.toString();

            Gson gson = new Gson();
            return gson.toJson(metadata);
        }
    }

    private static class ChunkEntrySender implements Runnable {

        ConcurrentLinkedQueue<ChunkDebugEntry> entryQueue;

        CommandSource sender;

        boolean running;

        int port;

        ChunkEntrySender(ConcurrentLinkedQueue<ChunkDebugEntry> entryQueue, CommandSource sender, int port) {
            this.entryQueue = entryQueue;
            this.running = true;
            this.sender = sender;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                System.out.println("PORT: " + this.port);
                Socket socket = new Socket("127.0.0.1", this.port);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                while(this.running) {
                    ChunkDebugEntry entry = this.entryQueue.poll();
                    while (entry != null) {
                        entry.write(out);
                        entry = this.entryQueue.poll();
                    }
                    out.flush();
                    Thread.sleep(50);
                }

                out.flush();
                out.close();

                socket.close();


                this.sender.sendMessage(new LiteralText("Disconnected from chunk debug server"));
            } catch (IOException exception) {
                this.sender.sendMessage(new LiteralText("Error talking to chunk debug server, check console"));
                ChunkDebugCommand.chunkDebugEnabled = false;
                exception.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            this.running = false;
        }

    }

    private static class ChunkDebugEntry {
        int x;
        int z;
        int tick;
        int world;
        String event;
        ChunkMetadata metadata;

        ChunkDebugEntry(int x, int z, int tick, int world, String event, ChunkMetadata metadata) {
            this.x = x;
            this.z = z;
            this.tick = tick;
            this.world = world;
            this.event = event;
            this.metadata = metadata;
        }

        public void write(PrintWriter pw) {
            String metadataStr = Base64.getEncoder().encodeToString(metadata.toJson().getBytes());

            // Avoid carriage return
            String out = x + "," + z + "," + tick + "," + world + "," + event + "," + metadataStr + "\n";
            pw.print(out);
        }
    }

    public static boolean chunkDebugEnabled = false;

    private static final ConcurrentLinkedQueue<ChunkDebugEntry> entries = new ConcurrentLinkedQueue<ChunkDebugEntry>();

    private static ChunkEntrySender chunkSender = null;

    @Override
    public String getName() {
        return "chunkDebug";
    }

    @Override
    public String getUsage(CommandSource sender) {
        return "/chunkDebug <start|stop|connect|disconnect> [port]";
    }

    @Override
    public void run(CommandSource sender, String[] args) throws CommandException {
        if (args.length < 1) {
            sender.sendMessage(new LiteralText(getUsage(sender)));
            return;
        }

        if (args[0].equalsIgnoreCase("start")) {
            startChunkDebug(sender);
        } else if (args[0].equalsIgnoreCase("stop")) {
            stopChunkDebug(sender);
        } else if (args[0].equalsIgnoreCase("connect")) {
            int port = 20000;
            if (args.length >= 2) {
                try {
                    port = Integer.parseInt(args[1], 10);
                } catch (NumberFormatException e) {
                    sender.sendMessage(new LiteralText(getUsage(sender)));
                    return;
                }
            }
            connect(sender, port);
        } else if (args[0].equalsIgnoreCase("disconnect")) {
            disconnect(sender);
        } else {
            sender.sendMessage(new LiteralText(getUsage(sender)));
            return;
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return suggestMatching(args, "start", "stop", "connect", "disconnect");
        }
        return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    private static void connect(CommandSource sender, int port) {
        if (chunkDebugEnabled) {
            sender.sendMessage(new LiteralText("Already enabled"));
            return;
        }

        chunkDebugEnabled = true;
        entries.clear();

        chunkSender = new ChunkEntrySender(entries, sender, port);
        new Thread(chunkSender, "Chunk Debug Thread").start();

        addAlreadyLoadedChunks();
    }

    private static void disconnect(CommandSource sender) {
        chunkDebugEnabled = false;
        if (chunkSender != null) {
            chunkSender.stop();
        }
    }

    private static void startChunkDebug(CommandSource sender) {
        if (chunkDebugEnabled) {
            sender.sendMessage(new LiteralText("Already enabled"));
            return;
        }

        sender.sendMessage(new LiteralText("Recording chunk events"));

        chunkDebugEnabled = true;
        entries.clear();

        addAlreadyLoadedChunks();
    }

    private static void addAlreadyLoadedChunks() {
        MinecraftServer server = MinecraftServer.getInstance();
        for (int i = 0; i < server.worlds.length; i++) {
            ServerWorld world = server.worlds[i];
			List<WorldChunk> loadedChunks = world.chunkCache.getLoadedChunks();
			for (WorldChunk chunk : loadedChunks) {
				entries.add(new ChunkDebugEntry(chunk.chunkX, chunk.chunkZ, getCurrentTick(), world.dimension.getId(), "ALREADY_LOADED", collectMetadata(null)));
			}
        }
    }

    private static void stopChunkDebug(CommandSource sender) throws CommandException {
        chunkDebugEnabled = false;
        String fileName = "chunkDebug-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(new Date()) + ".csv";
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            ChunkDebugEntry entry = entries.poll();
            while (entry != null) {
                entry.write(pw);
                entry = entries.poll();
            }

            pw.flush();
            pw.close();

            sender.sendMessage(new LiteralText("Writing to file: " + fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage());
        }
    }

    private static int getCurrentTick() {
        return MinecraftServer.getInstance().getTicks();
    }

    private static ChunkMetadata collectMetadata(String custom) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        ChunkMetadata metadata = new ChunkMetadata();
        metadata.custom = custom;
        metadata.stackTrace = stackTrace;
        return metadata;
    }

    private static int getDimensionFromWorld(World world) {
        return world.dimension.getId();
    }

    public static void onChunkLoaded(int x, int z, World world, String custom) {
		entries.add(new ChunkDebugEntry(x, z, getCurrentTick(), getDimensionFromWorld(world), "LOADED", collectMetadata(custom)));
    }

    public static void onChunkGenerated(int x, int z, World world, String custom) {
        entries.add(new ChunkDebugEntry(x, z, getCurrentTick(), getDimensionFromWorld(world), "GENERATED", collectMetadata(custom)));
    }

    public static void onChunkPopulated(int x, int z, World world, String custom) {
        entries.add(new ChunkDebugEntry(x, z, getCurrentTick(), getDimensionFromWorld(world), "POPULATED", collectMetadata(custom)));
    }

    public static void onChunkUnloadScheduled(int x, int z, World world, String custom) {
        entries.add(new ChunkDebugEntry(x, z, getCurrentTick(), getDimensionFromWorld(world), "UNLOAD_SCHEDULED", collectMetadata(custom)));
    }

    public static void onChunkUnloaded(int x, int z, World world, String custom) {
        entries.add(new ChunkDebugEntry(x, z, getCurrentTick(), getDimensionFromWorld(world), "UNLOADED", collectMetadata(custom)));
    }
}
