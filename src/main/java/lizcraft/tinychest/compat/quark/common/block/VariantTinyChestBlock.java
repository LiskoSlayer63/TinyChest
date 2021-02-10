package lizcraft.tinychest.compat.quark.common.block;

import vazkii.quark.content.building.module.VariantChestsModule;

import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.compat.quark.common.tile.VariantTinyChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class VariantTinyChestBlock extends TinyChestBlock implements VariantChestsModule.IChestTextureProvider
{
	private String path;
	
	public VariantTinyChestBlock(String type, Properties properties) 
	{
		super(properties);
		path = type + "/";
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) 
	{
		return new VariantTinyChestTileEntity();
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
