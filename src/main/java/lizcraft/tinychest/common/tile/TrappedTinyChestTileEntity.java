package lizcraft.tinychest.common.tile;

import lizcraft.tinychest.common.CommonContent;

public class TrappedTinyChestTileEntity extends TinyChestTileEntity 
{
	public TrappedTinyChestTileEntity() 
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
