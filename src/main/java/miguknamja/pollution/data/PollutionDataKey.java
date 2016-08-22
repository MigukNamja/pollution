package miguknamja.pollution.data;

import java.util.Scanner;

public class PollutionDataKey {
	public PollutionDataKey( int dim, int x, int z ) {
		super();
		dimensionId = dim;
		xPosition = x;
		zPosition = z;
	}

	public PollutionDataKey( String s ) {
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
    	if( o != null && o instanceof PollutionDataKey ) {
    		PollutionDataKey other = (PollutionDataKey)o;
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
}
