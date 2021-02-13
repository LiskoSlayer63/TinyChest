package lizcraft.tinychest.compat.atmospheric.common.block;

import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.compat.atmospheric.common.IAbnormalsChestBlock;
import lizcraft.tinychest.compat.atmospheric.common.tile.AtmosphericTinyChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class AtmosphericTinyChestBlock extends TinyChestBlock implements IAbnormalsChestBlock
{
	public final String type;
	
	public AtmosphericTinyChestBlock(String type, Properties properties) 
	{
		super(properties);
		this.type = type;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) 
	{
		return new AtmosphericTinyChestTileEntity();
	}
	
	@Override
	public String getChestName() 
	{
		return this.type;
	}

	@Override
	public String getModid() 
	{
		return "atmospheric";
	}

	@Override
	public boolean isTrapped() 
	{
		return false;
	}
}
