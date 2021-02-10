package lizcraft.tinychest.compat;

import lizcraft.tinychest.compat.quark.common.QuarkCommon;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

public class CommonCompat 
{
	public static void register(IEventBus eventBus)
	{
		if (ModList.get().isLoaded("quark"))
			QuarkCommon.register(eventBus);
	}
}
