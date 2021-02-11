package lizcraft.tinychest.compat.quark.common;

import java.util.HashMap;

import com.google.common.collect.ImmutableSet;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.compat.quark.client.render.VariantItemStackRenderer;
import lizcraft.tinychest.compat.quark.common.block.VariantTinyChestBlock;
import lizcraft.tinychest.compat.quark.common.block.VariantTinyTrappedChestBlock;
import lizcraft.tinychest.compat.quark.common.tile.VariantTinyChestTileEntity;
import lizcraft.tinychest.compat.quark.common.tile.VariantTinyTrappedChestTileEntity;
import lizcraft.tinychest.utils.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.base.handler.MiscUtil;

public class QuarkCommon 
{
	private static final ImmutableSet<String> OVERWORLD_WOODS = ImmutableSet.copyOf(MiscUtil.OVERWORLD_WOOD_TYPES);
	private static final ImmutableSet<String> NETHER_WOODS = ImmutableSet.copyOf(MiscUtil.NETHER_WOOD_TYPES);
	
	public static HashMap<String, Block> VARIANT_TINYCHEST_BLOCKS = new HashMap<String, Block>();
	public static HashMap<String, Block> VARIANT_TRAPPED_TINYCHEST_BLOCKS = new HashMap<String, Block>();
	
	public static HashMap<String, Item> VARIANT_TINYCHEST_ITEMS = new HashMap<String, Item>();
	public static HashMap<String, Item> VARIANT_TRAPPED_TINYCHEST_ITEMS = new HashMap<String, Item>();
	
	public static TileEntityType<VariantTinyChestTileEntity> VARIANT_TINYCHEST_TILEENTITYTYPE;
	public static TileEntityType<VariantTinyTrappedChestTileEntity> VARIANT_TRAPPED_TINYCHEST_TILEENTITYTYPE;
	
	public static void register(IEventBus eventBus)
	{
		Logger.info("Enabling Quark compatibility");
		eventBus.register(QuarkCommon.class);
		
		ForgeRegistries.RECIPE_SERIALIZERS.register(MixedTinyChestRecipe.SERIALIZER);
	}
	
	@SubscribeEvent
	public static void onBlocksRegistration(final RegistryEvent.Register<Block> event) 
	{
		OVERWORLD_WOODS.forEach(s -> addChest(s, CommonContent.TINYCHEST_BLOCK));
		NETHER_WOODS.forEach(s -> addChest(s, CommonContent.TINYCHEST_BLOCK));
		
		addChest("nether_brick", Blocks.NETHER_BRICKS);
		addChest("purpur", Blocks.PURPUR_BLOCK);
		addChest("prismarine", Blocks.PRISMARINE);
		addChest("mushroom", Blocks.RED_MUSHROOM_BLOCK);
		
		for (Block block : VARIANT_TINYCHEST_BLOCKS.values())
			event.getRegistry().register(block);
		
		for (Block block : VARIANT_TRAPPED_TINYCHEST_BLOCKS.values())
			event.getRegistry().register(block);
	}
	
	@SubscribeEvent
	public static void onItemsRegistration(final RegistryEvent.Register<Item> event) 
	{
		for (Block block : VARIANT_TINYCHEST_BLOCKS.values())
		{
			Item item = new BlockItem(block, new Item.Properties().
				group(ItemGroup.DECORATIONS).
				setISTER(() -> () -> VariantItemStackRenderer.INSTANCE)
				).setRegistryName(block.getRegistryName());
			
			VARIANT_TINYCHEST_ITEMS.put(item.getRegistryName().getPath(), item);

			event.getRegistry().register(item);
		}
		
		for (Block block : VARIANT_TRAPPED_TINYCHEST_BLOCKS.values())
		{
			Item item = new BlockItem(block, new Item.Properties().
				group(ItemGroup.REDSTONE).
				setISTER(() -> () -> VariantItemStackRenderer.INSTANCE)
				).setRegistryName(block.getRegistryName());
			
			VARIANT_TRAPPED_TINYCHEST_ITEMS.put(item.getRegistryName().getPath(), item);

			event.getRegistry().register(item);
		}
	}
	
	@SubscribeEvent
	public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) 
	{
		VARIANT_TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(VariantTinyChestTileEntity::new, VARIANT_TINYCHEST_BLOCKS.values().toArray(new Block[0])).build(null);
		VARIANT_TINYCHEST_TILEENTITYTYPE.setRegistryName("variant_tinychest");

		VARIANT_TRAPPED_TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(VariantTinyTrappedChestTileEntity::new, VARIANT_TRAPPED_TINYCHEST_BLOCKS.values().toArray(new Block[0])).build(null);
		VARIANT_TRAPPED_TINYCHEST_TILEENTITYTYPE.setRegistryName("variant_trapped_tinychest");
		
		
		event.getRegistry().register(VARIANT_TINYCHEST_TILEENTITYTYPE);
		event.getRegistry().register(VARIANT_TRAPPED_TINYCHEST_TILEENTITYTYPE);
	}
	
	private static void addChest(String name, Block from) 
	{
		addChest(name, Block.Properties.from(from));
	}

	private static void addChest(String name, Block.Properties props) 
	{
		VARIANT_TINYCHEST_BLOCKS.put(name + "_tinychest", new VariantTinyChestBlock(name, props).setRegistryName(name + "_tinychest"));
		VARIANT_TRAPPED_TINYCHEST_BLOCKS.put(name + "_trapped_tinychest", new VariantTinyTrappedChestBlock(name, props).setRegistryName(name + "_trapped_tinychest"));
	}
}
