package miguknamja.pollution.items;

import miguknamja.pollution.Pollution;
import miguknamja.pollution.PollutionEffects;
import miguknamja.pollution.PollutionUpdater;
import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.pollutersdb.PollutersDB;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PollutionProbe extends Item {

	public PollutionProbe() {
		setRegistryName("pollutionprobe");
		setUnlocalizedName(Pollution.MODID + ".pollutionprobe");
		GameRegistry.register(this);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		//playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + getPollutionString()));
		return super.onItemUse( stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if( !worldIn.isRemote ) { // execute server side only
			BlockPos chunkPos = playerIn.getPosition();
			PollutersDB.scan( worldIn, chunkPos );
			PollutionDataValue p = PollutionUpdater.calcPollution( worldIn, chunkPos );
			playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + p.getPollutionString()));
			PollutionEffects.apply(worldIn, chunkPos);
			//System.out.println( PollutersDB.toStr() );
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
}
