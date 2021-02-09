package lizcraft.tinychest.common.tile;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.ITinyChestContainer;
import lizcraft.tinychest.common.block.TinyEnderChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(
value = Dist.CLIENT,
_interface = IChestLid.class
)
public class TinyEnderChestTileEntity extends TileEntity implements ITinyChestContainer, IChestLid, ITickableTileEntity
{
	protected float lidAngle;
	protected float prevLidAngle;
	
	protected int numPlayersUsing;
	private int ticksSinceSync;
	
	public TinyEnderChestTileEntity(TileEntityType<?> tileEntityTypeIn) 
	{
		super(tileEntityTypeIn);
	}

	public TinyEnderChestTileEntity() 
	{
		super(CommonContent.ENDER_TINYCHEST_TILEENTITYTYPE);
	}

	@Override
	public void tick() 
	{
		if (++this.ticksSinceSync % 20 * 4 == 0) 
			this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
		
		this.prevLidAngle = this.lidAngle;
		
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
			this.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN);

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
	            this.playSound(SoundEvents.BLOCK_ENDER_CHEST_CLOSE);
	        
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
	public void remove() 
	{
		this.updateContainingBlockInfo();
		super.remove();
	}

	@Override
	public void openContainer(PlayerEntity player) 
	{
		this.numPlayersUsing++;
		this.onOpenOrClose();
	}

	@Override
	public void closeContainer(PlayerEntity player) 
	{
		this.numPlayersUsing--;
		this.onOpenOrClose();
	}

	@Override
	public boolean canBeUsed(PlayerEntity player) 
	{
		if (this.world.getTileEntity(this.pos) != this) 
			return false;
		else 
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	protected void onOpenOrClose() 
	{
		Block block = this.getBlockState().getBlock();
	    if (block instanceof TinyEnderChestBlock)
	    	this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
	}
	
	private void playSound(SoundEvent soundIn) 
	{
		this.world.playSound((PlayerEntity)null, this.pos, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 1.2F);
	}
}
