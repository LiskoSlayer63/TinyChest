package lizcraft.tinychest.compat;

import java.util.ArrayList;
import java.util.List;

import lizcraft.tinychest.compat.atmospheric.common.AtmosphericCommon;
import lizcraft.tinychest.compat.quark.common.QuarkCommon;
import lizcraft.tinychest.utils.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
public class CommonCompat 
{
	private static List<String> compatList = new ArrayList<String>();
	
	public static void register(IEventBus eventBus)
	{
		if (ModList.get().isLoaded("quark"))
		{
			Logger.info("Enabling Quark compatibility");
			setEnabled("quark");
		}
		
		if (ModList.get().isLoaded("atmospheric"))
		{
			Logger.info("Enabling Atmospheric compatibility");
			setEnabled("atmospheric");
		}
		
		QuarkCommon.register(eventBus);
		AtmosphericCommon.register(eventBus);
	}
	
	public static boolean isEnabled(String modid)
	{
		return compatList.contains(modid.toLowerCase());
	}
	
	public static boolean isEnabled()
	{
		return !compatList.isEmpty();
	}
	
	private static void setEnabled(String modid)
	{
		String lowid = modid.toLowerCase();
		
		if (!compatList.contains(lowid))
			compatList.add(lowid);
		
		MixedTinyChestRecipe.register();
	}
}
