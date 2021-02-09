package lizcraft.tinychest.common.gui;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.ITinyChestContainer;
import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TinyChestContainer extends Container
{
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

	private static final int TINYCHEST_SLOTS_COUNT = 5;
	private static final int TINYCHEST_LARGE_COUNT = 10;
	
	private static final int PLAYER_INVENTORY_XPOS = 8;
	private static final int PLAYER_INVENTORY_YPOS = 51;
	
	private static final int TINYCHEST_SLOT_XPOS = 44;
	private static final int TINYCHEST_SLOT_YPOS = 20;
	
	private static final int SLOT_SPACING = 18;
	
	private static final int HOTBAR_XPOS = 8;
	private static final int HOTBAR_YPOS = 109;
	
	private final IItemHandler inventory;
	private final ITinyChestContainer container;
	
	public TinyChestContainer(int id, PlayerInventory playerInventory, PacketBuffer extraData) 
	{
		super(CommonContent.TINYCHEST_CONTAINER, id);
		this.container = null;
		
		if (extraData != null)
		{
			World world = Minecraft.getInstance().world;
			BlockPos pos = extraData.readBlockPos();
			TileEntity tileEntity = world.getTileEntity(pos);
			
			if (tileEntity instanceof TinyChestTileEntity && tileEntity.getBlockState().get(TinyChestBlock.DOUBLE_CHEST))
				this.inventory = new ItemStackHandler(TINYCHEST_LARGE_COUNT);
			else
				this.inventory = new ItemStackHandler(TINYCHEST_SLOTS_COUNT);
		}
		else
			this.inventory = new ItemStackHandler(TINYCHEST_SLOTS_COUNT);
		
		this.buildContainer(playerInventory);
	}
	
	public TinyChestContainer(int id, PlayerInventory playerInventory, IItemHandler inventory, ITinyChestContainer container)
	{
		super(CommonContent.TINYCHEST_CONTAINER, id);
		this.inventory = inventory;
		this.container = container;

		if (this.container != null)
			this.container.openContainer(playerInventory.player);
		
		this.buildContainer(playerInventory);
	}
	
	private void buildContainer(PlayerInventory playerInventory)
	{
		// Build tiny chest inventory
	    int chest_rows = this.inventory.getSlots() / TINYCHEST_SLOTS_COUNT; // Calculate row count (normal or large chest)
	    for (int row = 0; row < chest_rows; row++)
	    {
			for (int col = 0; col < TINYCHEST_SLOTS_COUNT; col++) 
			{
				int slot = row * TINYCHEST_SLOTS_COUNT + col;
				int xPos = TINYCHEST_SLOT_XPOS + col * SLOT_SPACING;
				int yPos = TINYCHEST_SLOT_YPOS + row * SLOT_SPACING;
				
				this.addSlot(new SlotItemHandler(inventory, slot, xPos, yPos));
		    }
	    }

 		// Build player inventory
 	    for (int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; row++) 
 	    {
 	    	for(int col = 0; col < PLAYER_INVENTORY_COLUMN_COUNT; col++) 
 	    	{
 	    		int slot = HOTBAR_SLOT_COUNT + row * PLAYER_INVENTORY_COLUMN_COUNT + col;
 	    		int xPos = PLAYER_INVENTORY_XPOS + col * SLOT_SPACING;
 	    		int yPos = PLAYER_INVENTORY_YPOS + (row + chest_rows - 1) * SLOT_SPACING;
 	    		
 	    		this.addSlot(new Slot(playerInventory, slot, xPos, yPos));
 	        }
 	    }

 	    // Build player hotbar
 	    for (int slot = 0; slot < HOTBAR_SLOT_COUNT; slot++) 
 	    {
 	    	int xPos = HOTBAR_XPOS + slot * SLOT_SPACING;
 	    	int yPos = HOTBAR_YPOS + (chest_rows - 1) * SLOT_SPACING;
 	    	
 	    	this.addSlot(new Slot(playerInventory, slot, xPos, yPos));
 	    }
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) 
	{
		return this.container != null ? this.container.canBeUsed(playerIn) : true;
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
			if (index < this.inventory.getSlots()) 
			{
				if (!this.mergeItemStack(itemstack1, this.inventory.getSlots(), this.inventorySlots.size(), true)) 
				{
					return ItemStack.EMPTY;
				}
			} 
			else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSlots(), false)) 
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
		
		if (this.container != null)
			this.container.closeContainer(playerIn);
	}
	
	public boolean isLarge()
	{
		return this.inventory.getSlots() == TINYCHEST_LARGE_COUNT;
	}
}
