package lizcraft.tinychest;


import org.apache.logging.log4j.LogManager;

import lizcraft.tinychest.client.ClientContent;
import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.CommonEvents;
import lizcraft.tinychest.utils.Logger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TinyChest.MOD_ID)
public class TinyChest 
{
	public static final String MOD_ID = "tinychest";
	
	public TinyChest()
	{
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		final IEventBus eventBus = MinecraftForge.EVENT_BUS;
		
		Logger.init(LogManager.getLogger(TinyChest.class));

		CommonContent.register(modEventBus);
		CommonEvents.register(eventBus);
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientContent.register(modEventBus));
	}
}
