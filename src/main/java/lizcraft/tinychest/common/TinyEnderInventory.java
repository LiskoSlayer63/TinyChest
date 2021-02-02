package lizcraft.tinychest.common;

import lizcraft.tinychest.common.tile.TinyEnderChestTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class TinyEnderInventory extends Inventory
{
	private TinyEnderChestTileEntity associatedChest;
	
	public TinyEnderInventory()
	{
		super(5);
	}
	
	public void setChestTileEntity(TinyEnderChestTileEntity chestTileEntity)
	{
		this.associatedChest = chestTileEntity;
	}
	
	@Override
	public void read(ListNBT listnbt)
	{
		for(int i = 0; i < this.getSizeInventory(); i++)
			this.setInventorySlotContents(i, ItemStack.EMPTY);

		for(int k = 0; k < listnbt.size(); ++k) 
		{
			CompoundNBT compound = listnbt.getCompound(k);
			int j = compound.getByte("Slot") & 255;
			
			if (j >= 0 && j < this.getSizeInventory()) 
				this.setInventorySlotContents(j, ItemStack.read(compound));
		}
	}
	
	@Override
	public ListNBT write()
	{
		ListNBT listnbt = new ListNBT();

		for(int i = 0; i < this.getSizeInventory(); i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if (!stack.isEmpty()) 
			{
				CompoundNBT compound = new CompoundNBT();
				compound.putByte("Slot", (byte)i);
				stack.write(compound);
				listnbt.add(compound);
			}
		}
		
		return listnbt;
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return this.associatedChest != null && !this.associatedChest.canBeUsed(player) ? false : super.isUsableByPlayer(player);
	}
	
	@Override
	public void openInventory(PlayerEntity player)
	{
		if (this.associatedChest != null)
			this.associatedChest.openChest();
		super.openInventory(player);
	}
	
	@Override
	public void closeInventory(PlayerEntity player)
	{
		if (this.associatedChest != null)
			this.associatedChest.closeChest();
		super.closeInventory(player);
		this.associatedChest = null;
	}
}
