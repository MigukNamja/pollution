package miguknamja.moda.blocks;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import miguknamja.moda.ModA;
import miguknamja.moda.compat.waila.WailaInfoProvider;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool ENABLED = PropertyBool.create("enabled");

	public PolluterBlock() {
		super(Material.GROUND);
		setUnlocalizedName(ModA.MODID + ".polluterblock");
		setRegistryName("polluterblock");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());

		//setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();
		if (te instanceof PolluterTileEntity) {
			PolluterTileEntity polluterTileEntity = (PolluterTileEntity) te;
			currenttip.add(TextFormatting.GRAY + "Pollution: " + polluterTileEntity.getPollution());
		}
		return currenttip;
	}

	/*
	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		TileEntity te = world.getTileEntity(data.getPos());
		if (te instanceof DataTileEntity) {
			DataTileEntity dataTileEntity = (DataTileEntity) te;
			probeInfo.horizontal()
			.item(new ItemStack(Items.CLOCK))
			.text(TextFormatting.GREEN + "Counter: " + dataTileEntity.getCounter());
			probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(0xffff0000))
			.entity(EntityList.getEntityStringFromClass(EntityHorse.class))
			.progress(dataTileEntity.getCounter() % 100, 100, probeInfo.defaultProgressStyle().suffix("%"));
		}
	}
	*/

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new PolluterTileEntity();
	}

	private PolluterTileEntity getTE(World world, BlockPos pos) {
		return (PolluterTileEntity) world.getTileEntity(pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			// We only count on the server side.

			if (side == state.getValue(FACING)) {
				int counter;
				if (hitY < .5f) {
					counter = getTE(world, pos).decrement();
				} else {
					counter = getTE(world, pos).increment();
				}
				player.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Counter: " + counter));
			}
		}
		// Return true also on the client to make sure that MC knows we handled this and will not try to place
		// a block on the client
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState()
				.withProperty(FACING, EnumFacing.getFront(meta & 7))
				.withProperty(ENABLED, (meta & 8) != 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex() + (state.getValue(ENABLED) ? 8 : 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ENABLED);
	}


	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn) {
		int powered = world.isBlockIndirectlyGettingPowered(pos);
		world.setBlockState(pos, state.withProperty(ENABLED, powered > 0), 3);
	}

	public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
		return EnumFacing.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
	}




}
