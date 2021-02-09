package lizcraft.tinychest.common.tile;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.ITinyChestContainer;
import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.common.gui.TinyChestContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@OnlyIn(
value = Dist.CLIENT,
_interface = IChestLid.class
)
public class TinyChestTileEntity extends TileEntity implements ITinyChestContainer, IChestLid, ITickableTileEntity, ICapabilityProvider, INamedContainerProvider
{
	private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("container.tinychest.tinychest");
	private static final ITextComponent LARGE_CONTAINER_NAME = new TranslationTextComponent("container.tinychest.large_tinychest");
	protected final ItemStackHandler inventory = new ItemStackHandler(10)
	{
		@Override
		public int getSlots() 
		{
			Block block = getBlockState().getBlock();
			if (block instanceof TinyChestBlock && getBlockState().get(TinyChestBlock.DOUBLE_CHEST))
				return 10;
			return  5;
		}
	};
	protected final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> inventory);
	
	protected float lidAngle;
	protected float prevLidAngle;
	
	protected int numPlayersUsing;
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
		inventory.deserializeNBT(nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) 
	{
		super.write(compound);
		compound.put("Items", inventory.serializeNBT().get("Items"));
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
		if (++this.ticksSinceSync % 20 * 4 == 0) 
			this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
		
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

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) 
	{
		return new TinyChestContainer(id, playerInventory, this.inventory, this);
	}

	@Override
	public ITextComponent getDisplayName() 
	{
		return this.inventory.getSlots() == 10 ? LARGE_CONTAINER_NAME : CONTAINER_NAME;
	}
	
	@Override
	public void remove() 
	{
		super.remove();
		this.updateContainingBlockInfo();
	}
	
	@Override
	public boolean canBeUsed(PlayerEntity player) 
	{
		if (this.world.getTileEntity(this.pos) != this) 
			return false;
		else 
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openContainer(PlayerEntity player)
	{
		if (!player.isSpectator())
		{
			this.numPlayersUsing++;
			this.onOpenOrClose();
		}
	}

	@Override
	public void closeContainer(PlayerEntity player) 
	{
		if (!player.isSpectator())
		{
			this.numPlayersUsing--;
			this.onOpenOrClose();
		}
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
	
	public static int getPlayersUsing(IBlockReader reader, BlockPos posIn) 
	{
		BlockState blockstate = reader.getBlockState(posIn);
		if (blockstate.hasTileEntity()) 
		{
			TileEntity tileentity = reader.getTileEntity(posIn);
			if (tileentity instanceof TinyChestTileEntity)
				return ((TinyChestTileEntity)tileentity).numPlayersUsing;
		}

		return 0;
	}
	
	private void playSound(SoundEvent soundIn) 
	{
		this.world.playSound((PlayerEntity)null, this.pos, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 1.2F);
	}
}
