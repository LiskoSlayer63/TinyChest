package lizcraft.tinychest.compat;

import com.google.gson.JsonObject;

import lizcraft.tinychest.TinyChest;
import lizcraft.tinychest.common.CommonContent;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/*
 * This is based heavily on the Quarks MixedChestRecipe.class
 */
public class MixedTinyChestRecipe implements ICraftingRecipe, IShapedRecipe<CraftingInventory>
{
	public static final Serializer SERIALIZER = new Serializer();
	private static boolean isRegistered = false;
	
	private final ResourceLocation resourceLoc;
	private NonNullList<Ingredient> ingredients;
	
	public static void register()
	{
		if (!isRegistered)
		{
			isRegistered = true;
			ForgeRegistries.RECIPE_SERIALIZERS.register(SERIALIZER);
		}
	}
	
	public MixedTinyChestRecipe(ResourceLocation resourceLoc)
	{
		this.resourceLoc = resourceLoc;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) 
	{
		return new ItemStack(CommonContent.TINYCHEST_BLOCK);
	}

	@Override
	public boolean canFit(int width, int height) 
	{
		return width == 3 && height == 3;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return new ItemStack(CommonContent.TINYCHEST_BLOCK);
	}

	@Override
	public ResourceLocation getId() 
	{
		return this.resourceLoc;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() 
	{
		return SERIALIZER;
	}

	@Override
	public int getRecipeWidth() 
	{
		return 3;
	}

	@Override
	public int getRecipeHeight() 
	{
		return 3;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() 
	{
		if(ingredients == null) 
		{
			NonNullList<Ingredient> list = NonNullList.withSize(9, Ingredient.EMPTY);
			Ingredient ingr = Ingredient.fromItems(Blocks.OAK_PLANKS);
			
			for(int i = 0; i < 9; i++)
				if (i % 2 != 0)
					list.set(i, ingr);
			
			ingredients = list;
		}
		
		return ingredients;
	}
	
	@Override
	public boolean isDynamic() 
	{
		return true;
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) 
	{
		ItemStack prev = ItemStack.EMPTY;
		boolean foundDifference = false;
		
		for (int i = 0; i < 9; i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			
			if (i % 2 == 0 && !stack.isEmpty() || i % 2 != 0 && !stack.getItem().isIn(ItemTags.PLANKS))
				return false;
			
			if (!prev.isEmpty() && !stack.isEmpty() && (!ItemStack.areItemsEqual(prev, stack) || !isTinyChestMaterial(stack.getItem())))
				foundDifference = true;
			
			if (!stack.isEmpty())
				prev = stack;
		}
		
		return foundDifference;
	}
	
	private static boolean isTinyChestMaterial(Item item) 
	{
		ResourceLocation res = item.getRegistryName();
		if(res.getNamespace().equals("minecraft"))
			return true;
		
		ResourceLocation check = new ResourceLocation(TinyChest.MOD_ID, res.getPath().replace("_planks", "_tinychest"));
		return ForgeRegistries.ITEMS.containsKey(check);
	}


	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MixedTinyChestRecipe>
	{
		public Serializer()
		{
			setRegistryName(TinyChest.MOD_ID, "mixed_tinychest");
		}
		
		@Override
		public MixedTinyChestRecipe read(ResourceLocation recipeId, JsonObject json) 
		{
			return new MixedTinyChestRecipe(recipeId);
		}

		@Override
		public MixedTinyChestRecipe read(ResourceLocation recipeId, PacketBuffer buffer) 
		{
			return new MixedTinyChestRecipe(recipeId);
		}

		@Override
		public void write(PacketBuffer buffer, MixedTinyChestRecipe recipe) 
		{
			
		}
	}
}
