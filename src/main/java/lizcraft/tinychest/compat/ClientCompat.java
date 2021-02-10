package lizcraft.tinychest.compat;

import lizcraft.tinychest.compat.quark.client.QuarkClient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

public class ClientCompat 
{
	public static void register(IEventBus eventBus)
	{
		if (ModList.get().isLoaded("quark"))
			QuarkClient.register(eventBus);
	}
}
