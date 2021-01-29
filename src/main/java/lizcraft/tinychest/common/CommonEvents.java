package lizcraft.tinychest.common;

import lizcraft.tinychest.common.capability.TinyEnderInventoryCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
}
