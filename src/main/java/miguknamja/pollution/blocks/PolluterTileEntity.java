package miguknamja.pollution.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PolluterTileEntity extends TileEntity {

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        //pollutionLevel = compound.getInteger("pollutionLevel");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        //compound.setInteger("pollutionLevel", pollutionLevel);
        return compound;
    }
}
