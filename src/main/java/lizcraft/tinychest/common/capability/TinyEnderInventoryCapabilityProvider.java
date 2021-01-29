package lizcraft.tinychest.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TinyEnderInventoryCapabilityProvider implements ICapabilitySerializable<CompoundNBT>
{
	private final TinyEnderInventoryProvider instance = new TinyEnderInventoryProvider();
    private final LazyOptional<ITinyEnderInventory> handler = LazyOptional.of(() -> instance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
	{
		if (cap == TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY)
			return handler.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() 
	{
		if (TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY == null)
			return new CompoundNBT();
		return (CompoundNBT) TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) 
	{
		if (TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY != null)
			TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY.readNBT(instance, null, nbt);
	}
}
