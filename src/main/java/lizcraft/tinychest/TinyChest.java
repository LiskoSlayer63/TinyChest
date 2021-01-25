package lizcraft.tinychest;


import org.apache.logging.log4j.LogManager;

import lizcraft.tinychest.client.ClientContent;
import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.utils.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TinyChest.MOD_ID)
public class TinyChest 
{
	public static final String MOD_ID = "tinychest";
	
	public TinyChest()
	{
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		Logger.init(LogManager.getLogger(TinyChest.class));
		
		CommonContent.register(modEventBus);
		ClientContent.register(modEventBus);
	}
}
