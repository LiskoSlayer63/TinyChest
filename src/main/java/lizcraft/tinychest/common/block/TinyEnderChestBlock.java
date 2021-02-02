package lizcraft.tinychest.common.block;

import java.util.Random;

import lizcraft.tinychest.common.TinyEnderInventory;
import lizcraft.tinychest.common.capability.ITinyEnderInventory;
import lizcraft.tinychest.common.capability.TinyEnderInventoryCapability;
import lizcraft.tinychest.common.tile.TinyEnderChestTileEntity;
import lizcraft.tinychest.common.gui.TinyChestContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

public class TinyEnderChestBlock extends Block implements IWaterLoggable
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(4.5D, 0.0D, 4.5D, 11.5D, 7.0D, 11.5D);
	private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("block.tinychest.ender_tinychest");
	
	public TinyEnderChestBlock(Properties properties) 
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) 
	{
		if (worldIn.isRemote) 
			return ActionResultType.SUCCESS;
		
		LazyOptional<ITinyEnderInventory> capability = player.getCapability(TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY);
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TinyEnderChestTileEntity)
		{
			TinyEnderInventory tinyEnderInventory = capability.orElseThrow(() -> new IllegalStateException("TinyEnderInventoryCapability was not present!")).getInventory();
			TinyEnderChestTileEntity tinyEnderChestTileEntity = (TinyEnderChestTileEntity)tileEntity;
			
			tinyEnderInventory.setChestTileEntity(tinyEnderChestTileEntity);
			
			player.openContainer(new SimpleNamedContainerProvider((id, inventory, playerIn) -> {
				return new TinyChestContainer(id, inventory, tinyEnderInventory);
			}, CONTAINER_NAME));
			
			player.addStat(Stats.OPEN_ENDERCHEST);
			PiglinTasks.func_234478_a_(player, true);
		}

		return ActionResultType.CONSUME;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) 
	{
		for(int i = 0; i < 2; i++) 
		{
	         int xMod = rand.nextInt(2) * 2 - 1;
	         int zMod = rand.nextInt(2) * 2 - 1;
	         
	         double posX = (double)pos.getX() + 0.5D + 0.125D * (double)xMod;
	         double posY = (double)((float)pos.getY() + rand.nextFloat() / 2.0F);
	         double posZ = (double)pos.getZ() + 0.5D + 0.125D * (double)zMod;
	         
	         double speedX = (double)(rand.nextFloat() * (float)xMod);
	         double speedY = ((double)rand.nextFloat() - 0.5D) * 0.125D;
	         double speedZ = (double)(rand.nextFloat() * (float)zMod);
	         
	         worldIn.addParticle(ParticleTypes.PORTAL, posX, posY, posZ, speedX, speedY, speedZ);
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
	{
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		return BASE_SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) 
	{
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		
		return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));
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
		return new TinyEnderChestTileEntity();
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
}
