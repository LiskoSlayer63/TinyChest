package lizcraft.tinychest.common.gui;

import lizcraft.tinychest.common.CommonContent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class TinyChestContainer extends Container
{
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

	public static final int TINYCHEST_SLOTS_COUNT = 5;
	
	public static final int PLAYER_INVENTORY_XPOS = 8;
	public static final int PLAYER_INVENTORY_YPOS = 51;
	
	public static final int TINYCHEST_SLOT_XPOS = 44;
	public static final int TINYCHEST_SLOT_YPOS = 20;
	
	private static final int SLOT_X_SPACING = 18;
	private static final int SLOT_Y_SPACING = 18;
	private static final int HOTBAR_XPOS = 8;
	private static final int HOTBAR_YPOS = 109;
	
	private final IInventory inventory;
	
	public TinyChestContainer(int id, PlayerInventory playerInventory, PacketBuffer extraData) 
	{
		this(id, playerInventory, new Inventory(6));
	}
	
	public TinyChestContainer(int id, PlayerInventory playerInventory, IInventory inventory)
	{
		super(CommonContent.TINYCHEST_CONTAINER, id);
		this.inventory = inventory;
		
		assertInventorySize(inventory, 5);
	    inventory.openInventory(playerInventory.player);

	    // Build tiny chest inventory
 		for(int slot = 0; slot < TINYCHEST_SLOTS_COUNT; slot++) 
 		{
 			int xPos = TINYCHEST_SLOT_XPOS + slot * SLOT_X_SPACING;
 			
 			this.addSlot(new Slot(inventory, slot, xPos, TINYCHEST_SLOT_YPOS));
 	    }

 		// Build player inventory
 	    for(int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; row++) 
 	    {
 	    	for(int col = 0; col < PLAYER_INVENTORY_COLUMN_COUNT; col++) 
 	    	{
 	    		int slot = HOTBAR_SLOT_COUNT + row * PLAYER_INVENTORY_COLUMN_COUNT + col;
 	    		int xPos = PLAYER_INVENTORY_XPOS + col * SLOT_X_SPACING;
 	    		int yPos = PLAYER_INVENTORY_YPOS + row * SLOT_Y_SPACING;
 	    		
 	    		this.addSlot(new Slot(playerInventory, slot, xPos, yPos));
 	        }
 	    }

 	    // Build player hotbar
 	    for(int slot = 0; slot < HOTBAR_SLOT_COUNT; slot++) 
 	    {
 	    	int xPos = HOTBAR_XPOS + slot * SLOT_X_SPACING;
 	    	
 	    	this.addSlot(new Slot(playerInventory, slot, xPos, HOTBAR_YPOS));
 	    }
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) 
	{
		return this.inventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) 
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) 
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < this.inventory.getSizeInventory()) 
			{
				if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) 
				{
					return ItemStack.EMPTY;
				}
			} 
			else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false)) 
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) 
			{
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		
		return itemstack;
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) 
	{
		super.onContainerClosed(playerIn);
		this.inventory.closeInventory(playerIn);
	}
	
	public IInventory getIInventory()
	{
		return this.inventory;
	}
}
