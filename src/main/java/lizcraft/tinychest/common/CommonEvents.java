package lizcraft.tinychest.common;

import lizcraft.tinychest.common.capability.ITinyEnderInventory;
import lizcraft.tinychest.common.capability.TinyEnderInventoryCapability;
import lizcraft.tinychest.common.capability.TinyEnderInventoryCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEvents 
{
	public static void register(IEventBus eventBus)
	{
		eventBus.register(CommonEvents.class);
	}
	
	@SubscribeEvent
	public static void attachPlayerCapabilities(final AttachCapabilitiesEvent<Entity> event)
	{
		Entity entity = event.getObject();
		
		if (!(entity instanceof PlayerEntity))
			return;
		
		event.addCapability(CommonContent.ENDER_TINYCHEST_CAPABILITY, new TinyEnderInventoryCapabilityProvider());
	}
	
	@SubscribeEvent
	public static void onPlayerClone(final PlayerEvent.Clone event)
	{
		if (!event.isWasDeath() || event.isCanceled())
			return;
		
		PlayerEntity original = event.getOriginal();
		PlayerEntity player = event.getPlayer();
		
		if (original == null || player == null || player instanceof FakePlayer)
			return;
		
		LazyOptional<ITinyEnderInventory> origCap = original.getCapability(TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY);
		LazyOptional<ITinyEnderInventory> newCap = player.getCapability(TinyEnderInventoryCapability.TINY_ENDERINVENTORY_CAPABILITY);
		
		if (origCap.isPresent() && newCap.isPresent())
			newCap.resolve().get().setInventory(origCap.resolve().get().getInventory());
	}
}
