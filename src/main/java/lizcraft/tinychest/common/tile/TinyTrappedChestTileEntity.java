package lizcraft.tinychest.common.tile;

import lizcraft.tinychest.common.CommonContent;
import net.minecraft.tileentity.TileEntityType;

public class TinyTrappedChestTileEntity extends TinyChestTileEntity 
{
	public TinyTrappedChestTileEntity(TileEntityType<?> tileEntityTypeIn) 
	{
		super(tileEntityTypeIn);
	}
	
	public TinyTrappedChestTileEntity() 
	{
		super(CommonContent.TRAPPED_TINYCHEST_TILEENTITYTYPE);
	}
	
	@Override
	protected void onOpenOrClose() 
	{
		super.onOpenOrClose();
		this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockState().getBlock());
	}
}
