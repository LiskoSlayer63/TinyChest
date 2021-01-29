package lizcraft.tinychest.common.tile;

import lizcraft.tinychest.common.CommonContent;

public class TinyTrappedChestTileEntity extends TinyChestTileEntity 
{
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
