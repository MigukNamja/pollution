package miguknamja.moda.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PolluterTileEntity extends TileEntity {

    private int pollutionLevel = 0;
    private int maxPollutionLevel = 20; // TODO : move this into a config

    public int decrement() {
    	if( pollutionLevel > 0 ) {
    	  pollutionLevel--;
          markDirty();
    	}
        return pollutionLevel;
    }

    public int increment() {
    	if( pollutionLevel < maxPollutionLevel ) {
    		pollutionLevel++;
    		markDirty();
    	}
        return pollutionLevel;
    }

    public int getPollution() {
        return pollutionLevel;
    }

    public double getPollutionPercent() {
        return 100.0 * pollutionLevel / maxPollutionLevel;
    }
    
    public String getPollutionString() {
    	return "Pollution Level: " + getPollutionPercent() + "%";
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
