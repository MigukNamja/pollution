package miguknamja.pollution.data;

import net.minecraft.tileentity.TileEntity;

/*
 * This class encapsulates the information we track per polluter,
 * such as its TileEntity super (parent) class, the fuel(s) being burned/consumed, etc.
 */
public class DataPerPolluter {
	public TileEntity te;
	
	public DataPerPolluter(TileEntity te) {
		super();
		this.te = te;
	}
	
	@Override
	public String toString() {
		String s = te.getBlockType().getUnlocalizedName();
		return s;
	}
}
