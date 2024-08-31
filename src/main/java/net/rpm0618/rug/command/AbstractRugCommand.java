package net.rpm0618.rug.command;

import com.google.common.collect.Lists;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRugCommand extends AbstractCommand {
	public static List<String> suggestChunkCoordinate(String[] args, int start, BlockPos pos) {
		int chunkX = pos.getX() >> 4;
		int chunkZ = pos.getZ() >> 4;

		if (start == args.length - 1) {
			return Lists.newArrayList(String.valueOf(chunkX));
		} else if (start == args.length - 2) {
			return Lists.newArrayList(String.valueOf(chunkZ));
		}

		return new ArrayList<>();
	}
}
