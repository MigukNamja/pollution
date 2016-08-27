package miguknamja.utils;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkMath {
	
	public static Chunk chunkNorthOf( World world, Chunk chunk ) {
		return world.getChunkFromChunkCoords(chunk.xPosition, chunk.zPosition - 1);
	}
	
	public static Chunk chunkSouthOf( World world, Chunk chunk ) {
		return world.getChunkFromChunkCoords(chunk.xPosition, chunk.zPosition + 1);
	}
	
	public static Chunk chunkEastOf( World world, Chunk chunk ) {
		return world.getChunkFromChunkCoords(chunk.xPosition + 1, chunk.zPosition);
	}
	
	public static Chunk chunkWestOf( World world, Chunk chunk ) {
		return world.getChunkFromChunkCoords(chunk.xPosition - 1, chunk.zPosition);
	}
}
