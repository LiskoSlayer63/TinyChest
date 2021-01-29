package lizcraft.tinychest.common.capability;

import lizcraft.tinychest.common.TinyEnderInventory;

public class TinyEnderInventoryProvider implements ITinyEnderInventory 
{
	private TinyEnderInventory inventory;

	@Override
	public void setInventory(TinyEnderInventory inventory) 
	{
		this.inventory = inventory;
	}

	@Override
	public TinyEnderInventory getInventory() 
	{
		return this.inventory;
	}
}
