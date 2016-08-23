package miguknamja.pollution;

import java.util.Map;

import miguknamja.pollution.data.PollutionDataKey;
import miguknamja.pollution.data.PollutionDataValue;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/*
 * This class implements the following actions:
 * 1. Scanning for and identifying pollution sources, i.e. "polluters"
 * 2. Calculating pollution output per polluter per update period, i.e. every X number of ticks
 * 3. Calculating the new pollution value for a chunk, based upon output per polluter in that chunk
 * 4. Spreading of pollution across chunks
 * 
 * What this class does *not* do is implement actions for what happens with the pollution, i.e. the pollution penalties
 */
import net.minecraft.world.chunk.Chunk;

public class PollutionUpdater {
    
	/* Map (list) of polluters in this chunk */
	private Map<BlockPos, TileEntity> polluters;
	
	/* The update period for performing scans */
	private int ticksPerUpdatePeriod;

	public PollutionUpdater() {
		super();
		this.polluters = null;
		this.ticksPerUpdatePeriod = 20; // TODO : Read this from a config		
	}
	
	public PollutionUpdater(int ticksPerUpdatePeriod) {
		this();
		this.ticksPerUpdatePeriod = ticksPerUpdatePeriod;
	}
	
	/* Scan for pollution sources in this chunk and place them in 'polluters' */
	public void scan( World world, BlockPos blockPos ) {
		if( world.isRemote )
			return; /* only run on the server */

		Chunk chunk = world.getChunkFromBlockCoords(blockPos);
		Map<BlockPos, TileEntity> tileEntities = chunk.getTileEntityMap();
		for( Map.Entry<BlockPos, TileEntity> entry : tileEntities.entrySet()){
			TileEntity te = entry.getValue();
			BlockPos pos = entry.getKey();
			if( isPolluter( te ) ) {
				System.out.println( "Found Polluter " + te.toString() + " at " + pos.toString() );
			}
		}
	}
	
	private boolean isPolluter( TileEntity te ) {
		return true;
		/*
		if( te.getClass() eq "TileEntityFurnace" ) {
			return true;
		} else {
			return false;
		}
		*/
	}

	public Map<BlockPos, TileEntity> getPolluters() {
		return polluters;
	}

	public void setPolluters(Map<BlockPos, TileEntity> polluters) {
		this.polluters = polluters;
	}

	public int getTicksPerUpdatePeriod() {
		return ticksPerUpdatePeriod;
	}

	public void setTicksPerUpdatePeriod(int ticksPerUpdatePeriod) {
		this.ticksPerUpdatePeriod = ticksPerUpdatePeriod;
	}
}
