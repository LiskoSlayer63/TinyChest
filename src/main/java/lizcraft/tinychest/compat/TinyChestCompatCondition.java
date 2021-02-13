package lizcraft.tinychest.compat;

import com.google.gson.JsonObject;

import lizcraft.tinychest.TinyChest;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class TinyChestCompatCondition implements ICondition
{
	public static final Serializer SERIALIZER = new Serializer();

	private static final ResourceLocation NAME = new ResourceLocation(TinyChest.MOD_ID, "compat");
	private String modid;
	
	public TinyChestCompatCondition(String modid)
	{
		this.modid = modid;
	}
	
	@Override
	public ResourceLocation getID() 
	{
		return NAME;
	}

	@Override
	public boolean test() 
	{
		return modid.equals("_any") ? CommonCompat.isEnabled() : CommonCompat.isEnabled(this.modid);
	}

	public static class Serializer implements IConditionSerializer<TinyChestCompatCondition>
	{
		@Override
		public void write(JsonObject json, TinyChestCompatCondition value)
		{
			json.addProperty("modid", value.modid);
		}

		@Override
		public TinyChestCompatCondition read(JsonObject json)
		{
			return new TinyChestCompatCondition(JSONUtils.getString(json, "modid"));
		}

		@Override
		public ResourceLocation getID()
		{
			return TinyChestCompatCondition.NAME;
		}
	}
}
