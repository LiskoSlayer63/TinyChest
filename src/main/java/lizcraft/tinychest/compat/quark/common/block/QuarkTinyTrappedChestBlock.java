package lizcraft.tinychest.compat.quark.common.block;

import lizcraft.tinychest.common.block.TinyTrappedChestBlock;
import lizcraft.tinychest.compat.quark.common.IQuarkChestTextureProvider;
import lizcraft.tinychest.compat.quark.common.tile.QuarkTinyTrappedChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class QuarkTinyTrappedChestBlock extends TinyTrappedChestBlock implements IQuarkChestTextureProvider
{
	private String path;
	
	public QuarkTinyTrappedChestBlock(String type, Properties properties) 
	{
		super(properties);
		path = type + "/";
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) 
	{
		return new QuarkTinyTrappedChestTileEntity();
	}

	@Override
	public String getChestTexturePath() 
	{
		return "model/chest/" + path;
	}

	@Override
	public boolean isTrap() 
	{
		return true;
	}
}
