package miguknamja.utils;

public class DimensionIdKey {
	public DimensionIdKey(int dimensionId) {
		super();
		this.dimensionId = dimensionId;
	}

	/** The ID of the dimension */
    public final int dimensionId;

    public int hashCode() {
    	return dimensionId;
    }
 
    public boolean equals( Object o ) {
    	if( o != null && o instanceof DimensionIdKey ) {
    		DimensionIdKey other = (DimensionIdKey)o;
    		return( this.dimensionId == other.dimensionId );
    	} else {
    		return false;
    	}
    }
 }
