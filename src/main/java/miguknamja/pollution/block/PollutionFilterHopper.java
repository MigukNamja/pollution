package miguknamja.pollution.block;

import java.util.List;

import miguknamja.pollution.tileentity.TileEntityFilterHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PollutionFilterHopper extends BlockHopper {

	public PollutionFilterHopper() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		// TODO Auto-generated method stub
		return super.getBoundingBox(state, source, pos);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		// TODO Auto-generated method stub
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		// TODO Auto-generated method stub
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
	@Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityFilterHopper();
    }

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		// TODO Auto-generated method stub
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public boolean isFullyOpaque(IBlockState state) {
		// TODO Auto-generated method stub
		return super.isFullyOpaque(state);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		// TODO Auto-generated method stub
		super.neighborChanged(state, worldIn, pos, blockIn);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO Auto-generated method stub
		return super.getRenderType(state);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		// TODO Auto-generated method stub
		return super.isFullCube(state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		// TODO Auto-generated method stub
		return super.isOpaqueCube(state);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		// TODO Auto-generated method stub
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		// TODO Auto-generated method stub
		return super.hasComparatorInputOverride(state);
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return super.getComparatorInputOverride(blockState, worldIn, pos);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		// TODO Auto-generated method stub
		return super.getBlockLayer();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		// TODO Auto-generated method stub
		return super.getStateFromMeta(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		// TODO Auto-generated method stub
		return super.getMetaFromState(state);
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		// TODO Auto-generated method stub
		return super.withRotation(state, rot);
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		// TODO Auto-generated method stub
		return super.withMirror(state, mirrorIn);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		// TODO Auto-generated method stub
		return super.createBlockState();
	}

}
