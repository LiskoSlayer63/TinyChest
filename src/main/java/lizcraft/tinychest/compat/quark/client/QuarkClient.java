package lizcraft.tinychest.compat.quark.client;

import lizcraft.tinychest.compat.quark.client.render.QuarkTinyChestRenderer;
import lizcraft.tinychest.compat.quark.common.QuarkCommon;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class QuarkClient 
{
	public static void register(IEventBus eventBus)
	{
		eventBus.register(QuarkClient.class);
	}
	
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) 
	{
		ClientRegistry.bindTileEntityRenderer(QuarkCommon.TINYCHEST_TILEENTITYTYPE, QuarkTinyChestRenderer::new);
		ClientRegistry.bindTileEntityRenderer(QuarkCommon.TRAPPED_TINYCHEST_TILEENTITYTYPE, QuarkTinyChestRenderer::new);

		for (Block block : QuarkCommon.TINYCHEST_BLOCKS.values())
			QuarkTinyChestRenderer.addBlock(block);

		for (Block block : QuarkCommon.TRAPPED_TINYCHEST_BLOCKS.values())
			QuarkTinyChestRenderer.addBlock(block);
	}
}
