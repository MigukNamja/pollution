package miguknamja.utils;

import java.util.Scanner;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkKey {
	public ChunkKey( int dim, int x, int z ) {
		super();
		dimensionId = dim;
		xPosition = x;
		zPosition = z;
	}

	public ChunkKey( String s ) {
    	super();

    	Scanner scanner = new Scanner( s );
    	scanner.useDelimiter(",|\\s+");
		dimensionId = scanner.nextInt();
		xPosition = scanner.nextInt();
		zPosition = scanner.nextInt();
		scanner.close();
	}
	
    /** The ID of the dimension */
    public final int dimensionId;
    
    /** The x coordinate of the chunk. */
    public final int xPosition;
    
    /** The z coordinate of the chunk. */
    public final int zPosition;
    
	@Override
    public int hashCode() {
    	//int hash = Math.abs( dimensionId % 100) + Math.abs(xPosition * 100) + Math.abs(zPosition * 100000);
    	int hash = toString().hashCode();
    	return hash;
    }

	@Override
    public boolean equals( Object o ) {
    	if( o != null && o instanceof ChunkKey ) {
    		ChunkKey other = (ChunkKey)o;
    		return( this.dimensionId == other.dimensionId &&
    				this.xPosition == other.xPosition &&    			
    				this.zPosition == other.zPosition );
    	} else {
    		return false;
    	}
	}

	@Override
	public String toString() {
		return new String( dimensionId + "," + xPosition + "," + zPosition );
	}

	public static ChunkKey getKey(World world, BlockPos blockPos) {
		Chunk chunk = world.getChunkFromBlockCoords(blockPos);
		int dim = world.provider.getDimension();
		ChunkKey key = new ChunkKey( dim, chunk.xPosition, chunk.zPosition );
		return key;
	}
}
