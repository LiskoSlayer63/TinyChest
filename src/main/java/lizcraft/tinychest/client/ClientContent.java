package lizcraft.tinychest.client;

import lizcraft.tinychest.client.gui.TinyChestContainerScreen;
import lizcraft.tinychest.client.render.TinyChestRenderer;
import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.compat.ClientCompat;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientContent 
{
	public static void register(IEventBus eventBus)
	{
		eventBus.register(ClientContent.class);
		
		ClientCompat.register(eventBus);
	}
	
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) 
	{
		ClientRegistry.bindTileEntityRenderer(CommonContent.TINYCHEST_TILEENTITYTYPE, TinyChestRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CommonContent.TRAPPED_TINYCHEST_TILEENTITYTYPE, TinyChestRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CommonContent.ENDER_TINYCHEST_TILEENTITYTYPE, TinyChestRenderer::new);
		
		ScreenManager.registerFactory(CommonContent.TINYCHEST_CONTAINER, TinyChestContainerScreen::new);
	}
}
