package lizcraft.tinychest.compat;

import lizcraft.tinychest.compat.atmospheric.client.AtmosphericClient;
import lizcraft.tinychest.compat.quark.client.QuarkClient;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientCompat 
{
	public static void register(IEventBus eventBus)
	{
		if (CommonCompat.isEnabled("quark"))
			QuarkClient.register(eventBus);
		
		if (CommonCompat.isEnabled("atmospheric"))
			AtmosphericClient.register(eventBus);
	}
}
