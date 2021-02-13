package lizcraft.tinychest.compat.atmospheric.client;

import lizcraft.tinychest.compat.atmospheric.client.render.AtmosphericTinyChestRenderer;
import lizcraft.tinychest.compat.atmospheric.common.AtmosphericCommon;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class AtmosphericClient 
{
	public static void register(IEventBus eventBus)
	{
		eventBus.register(AtmosphericClient.class);
	}
	
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) 
	{
		ClientRegistry.bindTileEntityRenderer(AtmosphericCommon.TINYCHEST_TILEENTITYTYPE, AtmosphericTinyChestRenderer::new);
		ClientRegistry.bindTileEntityRenderer(AtmosphericCommon.TRAPPED_TINYCHEST_TILEENTITYTYPE, AtmosphericTinyChestRenderer::new);
	}
}
