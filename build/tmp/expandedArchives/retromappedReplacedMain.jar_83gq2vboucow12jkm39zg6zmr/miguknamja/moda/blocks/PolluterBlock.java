package miguknamja.moda.blocks;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import miguknamja.moda.ModA;
import miguknamja.moda.compat.waila.WailaInfoProvider;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

public class PolluterBlock extends Block implements ITileEntityProvider, WailaInfoProvider {

    public static final PropertyDirection FACING = PropertyDirection.func_177712_a("facing", EnumFacing.Plane.HORIZONTAL);

    public PolluterBlock() {
        super(Material.field_151576_e);
        func_149663_c(ModA.MODID + ".polluterblock");
        setRegistryName("polluterblock");
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(PolluterTileEntity.class, ModA.MODID + "_polluterblock");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (te instanceof PolluterTileEntity) {
        	PolluterTileEntity polluterTileEntity = (PolluterTileEntity) te;
            currenttip.add(TextFormatting.GRAY + polluterTileEntity.getPollutionString());
        }
        return currenttip;
    }

    @Override
    public TileEntity func_149915_a(World worldIn, int meta) {
        return new PolluterTileEntity();
    }

    private PolluterTileEntity getTE(World world, BlockPos pos) {
        return (PolluterTileEntity) world.func_175625_s(pos);
    }

    @Override
    public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.field_72995_K) {
            // We only count on the server side.

            if (side == state.func_177229_b(FACING)) {
            	PolluterTileEntity polluterTileEntity = getTE(world, pos);
                
                if (hitY < .5f) {
                	polluterTileEntity.decrement();
                } else {
                	polluterTileEntity.increment();
                }
                player.func_146105_b(new TextComponentString(TextFormatting.GREEN + polluterTileEntity.getPollutionString()));
            }
        }
        // Return true also on the client to make sure that MC knows we handled this and will not try to place
        // a block on the client
        return true;
    }

    @Override
    public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.func_180501_a(pos, state.func_177226_a(FACING, placer.func_174811_aO().func_176734_d()), 2);
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        // Since we only allow horizontal rotation we need only 2 bits for facing. North, South, West, East start at index 2 so we have to add 2 here.
        return func_176223_P().func_177226_a(FACING, EnumFacing.func_82600_a((meta & 3) + 2));
    }

    @Override
    public int func_176201_c(IBlockState state) {
        // Since we only allow horizontal rotation we need only 2 bits for facing. North, South, West, East start at index 2 so we have to subtract 2 here.
        return state.func_177229_b(FACING).func_176745_a()-2;
    }

    @Override
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, FACING);
    }

}
