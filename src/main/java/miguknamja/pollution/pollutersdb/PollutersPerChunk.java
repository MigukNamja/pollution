package miguknamja.pollution.pollutersdb;

/*
 * A collection of polluters in a chunk
 * 
 * When pollution is calculated and updated per chunk, it uses
 * this list to get the list of polluters in a chunk, where chunk
 * is identified by BlockPos.
 */

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;

public class PollutersPerChunk {
	public HashMap<BlockPos, DataPerPolluter> polluters;

	public PollutersPerChunk() {
		super();
		this.polluters = new HashMap<BlockPos, DataPerPolluter>();
	}
}
