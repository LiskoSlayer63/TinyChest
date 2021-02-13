package lizcraft.tinychest.compat.quark.common;

import java.util.HashMap;

import com.google.common.collect.ImmutableSet;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.compat.CommonCompat;
import lizcraft.tinychest.compat.quark.client.render.QuarkItemStackRenderer;
import lizcraft.tinychest.compat.quark.common.block.QuarkTinyChestBlock;
import lizcraft.tinychest.compat.quark.common.block.QuarkTinyTrappedChestBlock;
import lizcraft.tinychest.compat.quark.common.tile.QuarkTinyChestTileEntity;
import lizcraft.tinychest.compat.quark.common.tile.QuarkTinyTrappedChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class QuarkCommon 
{
	private static final String[] OVERWORLD_WOOD_TYPES = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "dark_oak" };
	private static final String[] NETHER_WOOD_TYPES = new String[] { "crimson", "warped" };
	private static final ImmutableSet<String> OVERWORLD_WOODS = ImmutableSet.copyOf(OVERWORLD_WOOD_TYPES);
	private static final ImmutableSet<String> NETHER_WOODS = ImmutableSet.copyOf(NETHER_WOOD_TYPES);
	
	public static HashMap<String, Block> TINYCHEST_BLOCKS = new HashMap<String, Block>();
	public static HashMap<String, Block> TRAPPED_TINYCHEST_BLOCKS = new HashMap<String, Block>();
	
	public static HashMap<String, Item> TINYCHEST_ITEMS = new HashMap<String, Item>();
	public static HashMap<String, Item> TRAPPED_TINYCHEST_ITEMS = new HashMap<String, Item>();
	
	public static TileEntityType<QuarkTinyChestTileEntity> TINYCHEST_TILEENTITYTYPE;
	public static TileEntityType<QuarkTinyTrappedChestTileEntity> TRAPPED_TINYCHEST_TILEENTITYTYPE;
	
	public static void register(IEventBus eventBus)
	{
		eventBus.register(QuarkCommon.class);
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
		
		for (Block block : TINYCHEST_BLOCKS.values())
			event.getRegistry().register(block);
		
		for (Block block : TRAPPED_TINYCHEST_BLOCKS.values())
			event.getRegistry().register(block);
	}
	
	@SubscribeEvent
	public static void onItemsRegistration(final RegistryEvent.Register<Item> event) 
	{
		for (Block block : TINYCHEST_BLOCKS.values())
		{
			Item.Properties prop = new Item.Properties().setISTER(() -> () -> QuarkItemStackRenderer.INSTANCE);
			
			if (CommonCompat.isEnabled("quark"))
				prop.group(ItemGroup.DECORATIONS);
			
			Item item = new BlockItem(block, prop).setRegistryName(block.getRegistryName());
			
			TINYCHEST_ITEMS.put(item.getRegistryName().getPath(), item);

			event.getRegistry().register(item);
		}
		
		for (Block block : TRAPPED_TINYCHEST_BLOCKS.values())
		{
			Item.Properties prop = new Item.Properties().setISTER(() -> () -> QuarkItemStackRenderer.INSTANCE);
			
			if (CommonCompat.isEnabled("quark"))
				prop.group(ItemGroup.REDSTONE);
			
			Item item = new BlockItem(block, prop).setRegistryName(block.getRegistryName());
			
			TRAPPED_TINYCHEST_ITEMS.put(item.getRegistryName().getPath(), item);

			event.getRegistry().register(item);
		}
	}
	
	@SubscribeEvent
	public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) 
	{
		TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(QuarkTinyChestTileEntity::new, TINYCHEST_BLOCKS.values().toArray(new Block[0])).build(null);
		TINYCHEST_TILEENTITYTYPE.setRegistryName("variant_tinychest");

		TRAPPED_TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(QuarkTinyTrappedChestTileEntity::new, TRAPPED_TINYCHEST_BLOCKS.values().toArray(new Block[0])).build(null);
		TRAPPED_TINYCHEST_TILEENTITYTYPE.setRegistryName("variant_trapped_tinychest");
		
		
		event.getRegistry().register(TINYCHEST_TILEENTITYTYPE);
		event.getRegistry().register(TRAPPED_TINYCHEST_TILEENTITYTYPE);
	}
	
	private static void addChest(String name, Block from) 
	{
		addChest(name, Block.Properties.from(from));
	}

	private static void addChest(String name, Block.Properties props) 
	{
		TINYCHEST_BLOCKS.put(name + "_tinychest", new QuarkTinyChestBlock(name, props).setRegistryName(name + "_tinychest"));
		TRAPPED_TINYCHEST_BLOCKS.put(name + "_trapped_tinychest", new QuarkTinyTrappedChestBlock(name, props).setRegistryName(name + "_trapped_tinychest"));
	}
}
