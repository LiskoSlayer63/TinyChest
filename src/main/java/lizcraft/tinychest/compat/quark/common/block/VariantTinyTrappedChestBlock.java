package lizcraft.tinychest.compat.quark.common.block;

import lizcraft.tinychest.common.block.TinyTrappedChestBlock;
import lizcraft.tinychest.compat.quark.common.tile.VariantTinyTrappedChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import vazkii.quark.content.building.module.VariantChestsModule;

public class VariantTinyTrappedChestBlock extends TinyTrappedChestBlock implements VariantChestsModule.IChestTextureProvider
{
	private String path;
	
	public VariantTinyTrappedChestBlock(String type, Properties properties) 
	{
		super(properties);
		path = type + "/";
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) 
	{
		return new VariantTinyTrappedChestTileEntity();
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
