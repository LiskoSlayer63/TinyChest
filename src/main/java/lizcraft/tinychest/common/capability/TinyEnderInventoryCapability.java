package lizcraft.tinychest.common.capability;

import lizcraft.tinychest.common.TinyEnderInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class TinyEnderInventoryCapability 
{
	@CapabilityInject(ITinyEnderInventory.class)
	public static final Capability<ITinyEnderInventory> TINY_ENDERINVENTORY_CAPABILITY = null;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ITinyEnderInventory.class, new Storage(), TinyEnderInventoryProvider::new);
	}
	
	public static class Storage implements IStorage<ITinyEnderInventory>
	{
		@Override
		public INBT writeNBT(Capability<ITinyEnderInventory> capability, ITinyEnderInventory instance, Direction side) 
		{
			CompoundNBT compound = new CompoundNBT();
			TinyEnderInventory inventory = instance.getInventory();
			
			if (inventory != null)
				compound.put("TinyChestEnderItems", inventory.write());
			
			return compound;
		}

		@Override
		public void readNBT(Capability<ITinyEnderInventory> capability, ITinyEnderInventory instance, Direction side, INBT nbt) 
		{
			CompoundNBT compound = (CompoundNBT)nbt;
			TinyEnderInventory inventory = instance.getInventory();
			
			if (inventory == null)
				inventory = new TinyEnderInventory();
			
			if (compound.contains("TinyChestEnderItems", 9))
				inventory.read(compound.getList("TinyChestEnderItems", 10));
		}
	}
}
