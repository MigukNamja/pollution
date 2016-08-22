package miguknamja.pollution.data;

public class PollutionDataKey {
	public PollutionDataKey( int dim, int x, int z ) {
		dimensionId = dim;
		xPosition = x;
		zPosition = z;
	}
	
    /** The ID of the dimension */
    public final int dimensionId;
    
    /** The x coordinate of the chunk. */
    public final int xPosition;
    
    /** The z coordinate of the chunk. */
    public final int zPosition;
    
    public int hashCode() {
    	String string = new String( dimensionId + "." + xPosition + "." + zPosition );
    	//int hash = Math.abs( dimensionId % 100) + Math.abs(xPosition * 100) + Math.abs(zPosition * 100000);
    	int hash = string.hashCode();
    	return hash;
    }

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
}
