package miguknamja.moda.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PolluterTileEntity extends TileEntity {

    private int pollutionLevel = 0;

    public int decrement() {
    	pollutionLevel--;
        markDirty();
        return pollutionLevel;
    }

    public int increment() {
    	pollutionLevel++;
        markDirty();
        return pollutionLevel;
    }

    public int getPollution() {
        return pollutionLevel;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        pollutionLevel = compound.getInteger("pollutionLevel");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("pollutionLevel", pollutionLevel);
        return compound;
    }
}
