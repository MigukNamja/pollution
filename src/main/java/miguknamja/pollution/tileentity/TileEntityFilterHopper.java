package miguknamja.pollution.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraftforge.items.IItemHandler;

public class TileEntityFilterHopper extends TileEntityHopper {

	public TileEntityFilterHopper() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return super.writeToNBT(compound);
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return super.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return super.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return super.decrStackSize(index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return super.removeStackFromSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		super.setInventorySlotContents(index, stack);
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? super.getName() : "filterhopper";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return super.hasCustomName();
	}

	@Override
	public void setCustomName(String customNameIn) {
		// TODO Auto-generated method stub
		super.setCustomName(customNameIn);
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return super.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return super.isUseableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		super.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		super.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return super.isItemValidForSlot(index, stack);
	}

	@Override
	public void update() {
		// TODO Add code to filter pollution from block below
        super.update();
	}

	@Override
	public boolean updateHopper() {
		// TODO Auto-generated method stub
		return super.updateHopper();
	}

	@Override
	public double getXPos() {
		// TODO Auto-generated method stub
		return super.getXPos();
	}

	@Override
	public double getYPos() {
		// TODO Auto-generated method stub
		return super.getYPos();
	}

	@Override
	public double getZPos() {
		// TODO Auto-generated method stub
		return super.getZPos();
	}

	@Override
	public void setTransferCooldown(int ticks) {
		// TODO Auto-generated method stub
		super.setTransferCooldown(ticks);
	}

	@Override
	public boolean isOnTransferCooldown() {
		// TODO Auto-generated method stub
		return super.isOnTransferCooldown();
	}

	@Override
	public boolean mayTransfer() {
		// TODO Auto-generated method stub
		return super.mayTransfer();
	}

	@Override
	public String getGuiID() {
		// TODO Auto-generated method stub
		return super.getGuiID();
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return super.createContainer(playerInventory, playerIn);
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return super.getField(id);
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		super.setField(id, value);
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return super.getFieldCount();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
	}

	@Override
	protected IItemHandler createUnSidedHandler() {
		// TODO Auto-generated method stub
		return super.createUnSidedHandler();
	}

}
