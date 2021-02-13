package lizcraft.tinychest.compat.quark.common.block;

import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.compat.quark.common.IQuarkChestTextureProvider;
import lizcraft.tinychest.compat.quark.common.tile.QuarkTinyChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class QuarkTinyChestBlock extends TinyChestBlock implements IQuarkChestTextureProvider
{
	private String path;
	
	public QuarkTinyChestBlock(String type, Properties properties) 
	{
		super(properties);
		path = type + "/";
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) 
	{
		return new QuarkTinyChestTileEntity();
	}

	@Override
	public String getChestTexturePath() 
	{
		return "model/chest/" + path;
	}

	@Override
	public boolean isTrap() 
	{
		return false;
	}
}
