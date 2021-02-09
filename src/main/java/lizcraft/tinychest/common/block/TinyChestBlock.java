package lizcraft.tinychest.common.block;

import javax.annotation.Nullable;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import lizcraft.tinychest.utils.TinyItemHandlerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TinyChestBlock extends Block implements IWaterLoggable
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty DOUBLE_CHEST = CommonContent.DOUBLE_CHEST;
	
	protected static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(4.5D, 0.0D, 4.5D, 11.5D, 7.0D, 11.5D);
	protected static final VoxelShape DOUBLE_SHAPE_NORTH_SOUTH = Block.makeCuboidShape(0.5D, 0.0D, 4.5D, 15.5D, 7.0D, 11.5D);
	protected static final VoxelShape DOUBLE_SHAPE_WEST_EAST = Block.makeCuboidShape(4.5D, 0.0D, 0.5D, 11.5D, 7.0D, 15.5D);

	public TinyChestBlock(Properties properties) 
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false)).with(DOUBLE_CHEST, Boolean.valueOf(false)));
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) 
	{
		if (worldIn.isRemote) 
			return ActionResultType.SUCCESS;
		
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TinyChestTileEntity && player instanceof ServerPlayerEntity)
		{
			NetworkHooks.openGui((ServerPlayerEntity)player, (TinyChestTileEntity)tileentity, pos);
            player.addStat(this.getOpenStat());
            PiglinTasks.func_234478_a_(player, true);
		}
	  
		return ActionResultType.CONSUME;
	}
	
	protected Stat<ResourceLocation> getOpenStat() 
	{
		return Stats.CUSTOM.get(Stats.OPEN_CHEST);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
	{
		builder.add(FACING, WATERLOGGED, DOUBLE_CHEST);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		if (!state.get(DOUBLE_CHEST))
			return BASE_SHAPE;
		
		Direction direction = state.get(FACING);
		
		if (direction == Direction.NORTH || direction == Direction.SOUTH)
			return DOUBLE_SHAPE_NORTH_SOUTH;
		
		return DOUBLE_SHAPE_WEST_EAST;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) 
	{
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		BlockState blockstate = context.getWorld().getBlockState(context.getPos());
		
		if (blockstate.isIn(this))
			return blockstate.with(DOUBLE_CHEST, Boolean.valueOf(true));
		
		return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER)).with(DOUBLE_CHEST, Boolean.valueOf(false));
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) 
	{
		return false;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) 
	{
		return new TinyChestTileEntity();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) 
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) 
	{
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid)
	{
		if (state.get(DOUBLE_CHEST) && !player.isCreative())
			return true;

		return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}
	
	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) 
	{
		if (state.get(DOUBLE_CHEST))
		{
			BlockState newState = state.with(DOUBLE_CHEST, Boolean.valueOf(false));
			
			this.dropItemsAtRange(worldIn, pos, 5, 10);
			worldIn.setBlockState(pos, newState, Constants.BlockFlags.BLOCK_UPDATE);
			
			super.harvestBlock(worldIn, player, pos, newState, te, stack);
		}
		else
			super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) 
	{
		if (!state.isIn(newState.getBlock())) 
		{
			this.dropItemsAtRange(worldIn, pos, 0, 10);
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) 
	{
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) 
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) 
	{
		return useContext.getItem().getItem() == this.asItem() && !state.get(DOUBLE_CHEST) ? true : super.isReplaceable(state, useContext);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) 
	{
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) 
	{
		if (stateIn.get(WATERLOGGED))
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state) 
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) 
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TinyChestTileEntity)
		{
			LazyOptional<IItemHandler> capability = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
			if (capability.isPresent())
				return ItemHandlerHelper.calcRedstoneFromInventory(capability.orElse(null));
		}
		return 0;
	}
	
	protected void dropItemsAtRange(World worldIn, BlockPos pos, int min, int max)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TinyChestTileEntity) 
		{
			LazyOptional<IItemHandler> capability = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
			capability.ifPresent((handler) -> TinyItemHandlerHelper.dropInventoryItemsAtRange(worldIn, pos, handler, min, max));
		}
	}
}
