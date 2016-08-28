package miguknamja.pollution.items;

import miguknamja.pollution.Config;
import miguknamja.pollution.data.PollutionWorldData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.registry.GameRegistry;


@SuppressWarnings("unused")
public class HandFan extends PollutionItemBase {

	public HandFan() {
		super("handfan");
		setMaxStackSize(1);	    
	}

    /**
     * Called when a entity tries to play the 'swing' animation.
     */
    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
    	World worldIn = entityLiving.getEntityWorld();
    	if( !worldIn.isRemote ) { // execute server side only
    		BlockPos pos = entityLiving.getPosition();
    		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
    		PollutionWorldData.changePercent(-Config.handheldFanCleanupFactor, worldIn, chunk); // handheldFanCleanupFactor is positive. However, to decrease pollution, we need to make it negative
			String pollution = PollutionWorldData.getPollutionString(worldIn, chunk);
    		if( entityLiving instanceof EntityPlayer )
    		{
				EntityPlayer playerIn = (EntityPlayer)entityLiving;
    			if( !worldIn.isRemote ) { // execute server side only
    				//playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.WHITE + "Pollution is now " + pollution));
    			}
    		}
    	}
		return super.onEntitySwing( entityLiving, stack );
    }
    
    @Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if( !worldIn.isRemote ) { // execute server side only
			//playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.WHITE + "Hand fan item use"));
		}
		return super.onItemUse( stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if( !worldIn.isRemote ) { // execute server side only
			//playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.WHITE + "Hand fan right click"));
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
	
	@Override
	public void initCrafting(){
		GameRegistry.addRecipe(new ItemStack(this), new Object[] {" ~ ", "~#~", "/~ ", '~', Items.STRING, '#', Blocks.WOOL, '/', Items.STICK});
	}
}
