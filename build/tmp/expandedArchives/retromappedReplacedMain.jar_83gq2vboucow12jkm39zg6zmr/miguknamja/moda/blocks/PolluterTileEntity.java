package miguknamja.moda.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PolluterTileEntity extends TileEntity {

    private int pollutionLevel = 0;
    private int maxPollutionLevel = 20; // TODO : move this into a config

    public int decrement() {
    	if( pollutionLevel > 0 ) {
    	  pollutionLevel--;
          func_70296_d();
    	}
        return pollutionLevel;
    }

    public int increment() {
    	if( pollutionLevel < maxPollutionLevel ) {
    		pollutionLevel++;
    		func_70296_d();
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
    public void func_145839_a(NBTTagCompound compound) {
        super.func_145839_a(compound);
        pollutionLevel = compound.func_74762_e("pollutionLevel");
    }

    @Override
    public NBTTagCompound func_189515_b(NBTTagCompound compound) {
        super.func_189515_b(compound);
        compound.func_74768_a("pollutionLevel", pollutionLevel);
        return compound;
    }
}
