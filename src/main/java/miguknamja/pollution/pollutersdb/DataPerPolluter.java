package miguknamja.pollution.pollutersdb;


import java.util.HashSet;
import java.util.Set;

import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

/*
 * This class encapsulates the information we track per polluter,
 * such as its TileEntity super (parent) class, the fuel(s) being burned/consumed, etc.
 */
public class DataPerPolluter {
	public TileEntity te;
	public Set<Slot> fuels;
	
	public DataPerPolluter(TileEntity te) {
		super();
		this.te = te;
		this.fuels = new HashSet<Slot>();
	}
	
	@Override
	public String toString() {
		String s = te.getBlockType().getUnlocalizedName();
		return s;
	}
}
