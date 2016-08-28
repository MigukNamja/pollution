package miguknamja.pollution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.data.PollutionWorldData;
import miguknamja.utils.ChunkKey;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PollutionDissipation {
	/**
	 * Dissipate pollution from all polluted chunks
	 * 
	 * @param world
	 * @return
	 */
	public static void dissipate( World world ) {
		// Get a *copy* of the pollutedChunks since we will updating all chunks and don't want to be modifying the collection while we're iterating over it
		ConcurrentHashMap<ChunkKey, PollutionDataValue> pollutedChunks = new ConcurrentHashMap<ChunkKey, PollutionDataValue>( PollutionWorldData.getPollutedChunks( world ) );

		// Iterate over copy and update pollution in all these chunks, which may also cause pollution to spread to adjacent chunks
		for( Map.Entry<ChunkKey, PollutionDataValue> entry : pollutedChunks.entrySet() ){
			Chunk chunk = entry.getKey().getChunk(world);
			dissipate( world, chunk );			
		}
	}

	/**
	 * Dissipate pollution from this chunk
	 * 
	 * @param world
	 * @param chunk
	 * @return
	 */
	public static PollutionDataValue dissipate( World world, Chunk chunk ) {
		return PollutionWorldData.decreasePercentOfCurrent( Config.dissipationFactor / 100.0, world, chunk );
	}
}