package miguknamja.pollution.items;

import miguknamja.pollution.PollutionUpdater;
import miguknamja.pollution.data.PollutionDataValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PollutionProbe extends PollutionItemBase {

    public PollutionProbe() {
		super("pollutionprobe");
		setMaxStackSize(1);	    
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if( !worldIn.isRemote ) { // execute server side only
			//playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "r-click"));
			//playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + getPollutionString()));
		}
		return super.onItemUse( stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if( !worldIn.isRemote ) { // execute server side only
			BlockPos pos = playerIn.getPosition();
			Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
			PollutionDataValue pdv = PollutionUpdater.calcPollution( worldIn, chunk );
			playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + pdv.getPollutionString()));
			
			//System.out.println( PollutersDB.toStr() );
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
	
	@Override
	public void initCrafting(){
		GameRegistry.addRecipe(new ItemStack(this), new Object[] {"###", "#^#", "#c#", '#', Items.IRON_INGOT, '^', Items.COMPASS, 'c', Items.COMPARATOR});
	}
}
