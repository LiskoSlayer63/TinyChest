package lizcraft.tinychest.common.tile;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.common.gui.TinyChestContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

@OnlyIn(
value = Dist.CLIENT,
_interface = IChestLid.class
)
public class TinyChestTileEntity extends TileEntity implements IChestLid, ITickableTileEntity, ICapabilityProvider, INamedContainerProvider, IInventory
{
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> new InvWrapper(this));
	
	private float lidAngle;
	private float prevLidAngle;
	
	private int numPlayersUsing;
	private int ticksSinceSync;
	
	public TinyChestTileEntity(TileEntityType<?> tileEntityTypeIn) 
	{
		super(tileEntityTypeIn);
	}

	public TinyChestTileEntity() 
	{
		super(CommonContent.TINYCHEST_TILEENTITYTYPE);
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) 
	{
		super.read(state, nbt);
		ItemStackHelper.loadAllItems(nbt, inventory);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) 
	{
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, inventory);
		return compound;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) 
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
			return this.handler.cast();
		return super.getCapability(capability, facing);
	}

	@Override
	public void tick() 
	{
		this.ticksSinceSync++;
	    this.numPlayersUsing = calculatePlayersUsingSync(this.world, this, this.ticksSinceSync, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.numPlayersUsing);
		this.prevLidAngle = this.lidAngle;
		
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
			this.playSound(SoundEvents.BLOCK_CHEST_OPEN);

	    if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) 
	    {
	    	float oldAngle = this.lidAngle;
	    	
	    	if (this.numPlayersUsing > 0)
	    		this.lidAngle += 0.1F;
	    	else
	    		this.lidAngle -= 0.1F;

	        if (this.lidAngle > 1.0F)
	            this.lidAngle = 1.0F;
	        
	        if (this.lidAngle < 0.5F && oldAngle >= 0.5F)
	            this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
	        
	        if (this.lidAngle < 0.0F)
	        	this.lidAngle = 0.0F;
	    }
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public float getLidAngle(float partialTicks) 
	{
		return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
	}

	@Override
	public boolean receiveClientEvent(int id, int type) 
	{
		if (id == 1) 
		{
			this.numPlayersUsing = type;
			return true;
		}
        return super.receiveClientEvent(id, type);
	}
	
	/*
	 * CONTAINERPROVIDER OVERRIDES
	 */

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) 
	{
		return new TinyChestContainer(id, playerInventory, this);
	}

	@Override
	public ITextComponent getDisplayName() 
	{
		return new TranslationTextComponent("block.tinychest.tinychest");
	}
	
	/*
	 * INVENTORY OVERRIDES
	 */

	@Override
	public void clear() 
	{
		for (ItemStack stack : this.inventory)
			if (!stack.isEmpty())
				stack = ItemStack.EMPTY;
	}

	@Override
	public int getSizeInventory() 
	{
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() 
	{
		for (ItemStack stack : this.inventory)
			if (!stack.isEmpty())
				return false;
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) 
	{
		if (index < this.getSizeInventory())
			return this.inventory.get(index);
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		if (index < this.getSizeInventory())
			return ItemStackHelper.getAndSplit(this.inventory, index, count);
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		if (index < this.getSizeInventory())
		{
			ItemStack stack = this.inventory.get(index);
			this.inventory.set(index, ItemStack.EMPTY);
			return stack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		if (index < this.getSizeInventory())
			this.inventory.set(index, stack);
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) 
	{
		if (this.world.getTileEntity(this.pos) != this) 
			return false;
		else 
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(PlayerEntity player) 
	{
		if (!player.isSpectator())
		{
			if (this.numPlayersUsing < 0) 
				this.numPlayersUsing = 0;

			++this.numPlayersUsing;
			this.onOpenOrClose();
		}
	}

	@Override
	public void closeInventory(PlayerEntity player) 
	{
		if (!player.isSpectator()) 
		{
			--this.numPlayersUsing;
	        this.onOpenOrClose();
	    }
	}
	
	@Override
	public void remove() 
	{
		this.updateContainingBlockInfo();
		super.remove();
	}
	
	protected void onOpenOrClose() 
	{
		Block block = this.getBlockState().getBlock();
	    if (block instanceof TinyChestBlock) 
	    {
	    	this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
	    	this.world.notifyNeighborsOfStateChange(this.pos, block);
	    }
	}
	
	private static int calculatePlayersUsingSync(World worldIn, IInventory inventory, int ticksSince, int posX, int posY, int posZ, int playersUsing) 
	{
		if (!worldIn.isRemote && playersUsing != 0 && (ticksSince + posX + posY + posZ) % 200 == 0)
			playersUsing = calculatePlayersUsing(worldIn, inventory, posX, posY, posZ);

	    return playersUsing;
	}

	public static int calculatePlayersUsing(World worldIn, IInventory inventory, int posX, int posY, int posZ) 
	{
		int count = 0;

	    for(PlayerEntity playerentity : worldIn.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(posX - 5.0D, posY - 5.0D, posZ - 5.0D, posX + 1 + 5.0D, posY + 1 + 5.0D, posZ + 1 + 5.0D))) 
	    {
	    	if (playerentity.openContainer instanceof TinyChestContainer) 
	    	{
	    		IInventory iinventory = ((TinyChestContainer)playerentity.openContainer).getIInventory();
	            if (iinventory == inventory)
	            	count++;
	        }
	    }

	    return count;
	}
	
	private void playSound(SoundEvent soundIn) 
	{
		this.world.playSound((PlayerEntity)null, this.pos, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
	}
}
