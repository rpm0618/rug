# rug
A technical utility mod for Minecraft 1.8 (Like a carpet, but worse)

Very alpha quality

## Comamnds
- `/as`: Show the number of ticks until the next autosave
- `/chunkDebug`: Create logs of chunk events, or connect directly to a (viewer)[https://github.com/rpm0618/mc_utils]. The viewer must be running on the same computer as the minecraft server.
  - `/chunkDebug start`: Start recording chunk events
  - `/chunkDebug stop`: Stop recording chunk events and write them to a file
  - `/chunkDebug connect [port]`: Connect to the viewer linked above and show realtime chunk events
  - `/chunkDebug disconnect`: Stop sending realtime chunk events

## Development

If you use Nix, `nix develop` should provide all the required dependencies (expcept minecraft itself, MCP expects minecraft 1.8.8 to have been launched at least once)

1. `setup.sh`: Download MCP, decompile, and apply patches
2. `build.sh`: Build and reobfuscate patches, package into `dist/rug_server.zip`
3. `generate_patch.sh`: Generate patches from changes made to src
4. `pack_jar.sh`: Merge the patches in `dist/rug_server.zip` into a minecraft_server.jar to create `dist/rug_server.jar`. **DO NOT DISTRIBUTE**
