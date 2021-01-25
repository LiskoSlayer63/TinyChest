package lizcraft.tinychest.common.block;

import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import lizcraft.tinychest.common.tile.TrappedTinyChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

public class TrappedTinyChestBlock extends TinyChestBlock
{
	public TrappedTinyChestBlock(Properties properties) 
	{
		super(properties);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) 
	{
		return new TrappedTinyChestTileEntity();
	}
	
	@Override
	public boolean canProvidePower(BlockState state) 
	{
		return true;
	}

	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) 
	{
		return MathHelper.clamp(TinyChestTileEntity.getPlayersUsing(blockAccess, pos), 0, 15);
	}

	@Override
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) 
	{
		return side == Direction.UP ? blockState.getWeakPower(blockAccess, pos, side) : 0;
	}
}
