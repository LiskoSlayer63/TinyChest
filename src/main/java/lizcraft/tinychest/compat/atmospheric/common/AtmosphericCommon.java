package lizcraft.tinychest.compat.atmospheric.common;

import java.util.HashMap;

import com.google.common.collect.ImmutableSet;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.compat.CommonCompat;
import lizcraft.tinychest.compat.atmospheric.client.render.AtmosphericItemStackRenderer;
import lizcraft.tinychest.compat.atmospheric.common.block.AtmosphericTinyChestBlock;
import lizcraft.tinychest.compat.atmospheric.common.block.AtmosphericTinyTrappedChestBlock;
import lizcraft.tinychest.compat.atmospheric.common.tile.AtmosphericTinyChestTileEntity;
import lizcraft.tinychest.compat.atmospheric.common.tile.AtmosphericTinyTrappedChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AtmosphericCommon 
{
	private static final String[] WOOD_TYPES = new String[] { "rosewood", "morado", "yucca", "kousa", "aspen", "grimwood" };
	private static final ImmutableSet<String> WOODS = ImmutableSet.copyOf(WOOD_TYPES);
	
	public static HashMap<String, Block> TINYCHEST_BLOCKS = new HashMap<String, Block>();
	public static HashMap<String, Block> TRAPPED_TINYCHEST_BLOCKS = new HashMap<String, Block>();
	
	public static HashMap<String, Item> TINYCHEST_ITEMS = new HashMap<String, Item>();
	public static HashMap<String, Item> TRAPPED_TINYCHEST_ITEMS = new HashMap<String, Item>();
	
	public static TileEntityType<AtmosphericTinyChestTileEntity> TINYCHEST_TILEENTITYTYPE;
	public static TileEntityType<AtmosphericTinyTrappedChestTileEntity> TRAPPED_TINYCHEST_TILEENTITYTYPE;
	
	public static void register(IEventBus eventBus)
	{
		eventBus.register(AtmosphericCommon.class);
	}
	
	@SubscribeEvent
	public static void onBlocksRegistration(final RegistryEvent.Register<Block> event) 
	{
		WOODS.forEach(s -> addChest(s, CommonContent.TINYCHEST_BLOCK));
		
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
			Item.Properties prop = new Item.Properties().setISTER(() -> () -> AtmosphericItemStackRenderer.INSTANCE);
			
			if (CommonCompat.isEnabled("atmospheric"))
				prop.group(ItemGroup.DECORATIONS);
			
			Item item = new BlockItem(block, prop).setRegistryName(block.getRegistryName());
			
			TINYCHEST_ITEMS.put(item.getRegistryName().getPath(), item);

			event.getRegistry().register(item);
		}
		
		for (Block block : TRAPPED_TINYCHEST_BLOCKS.values())
		{
			Item.Properties prop = new Item.Properties().setISTER(() -> () -> AtmosphericItemStackRenderer.INSTANCE);
			
			if (CommonCompat.isEnabled("atmospheric"))
				prop.group(ItemGroup.REDSTONE);
			
			Item item = new BlockItem(block, prop).setRegistryName(block.getRegistryName());
			
			TRAPPED_TINYCHEST_ITEMS.put(item.getRegistryName().getPath(), item);

			event.getRegistry().register(item);
		}
	}
	
	@SubscribeEvent
	public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) 
	{
		TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(AtmosphericTinyChestTileEntity::new, TINYCHEST_BLOCKS.values().toArray(new Block[0])).build(null);
		TINYCHEST_TILEENTITYTYPE.setRegistryName("atmospheric_tinychest");

		TRAPPED_TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(AtmosphericTinyTrappedChestTileEntity::new, TRAPPED_TINYCHEST_BLOCKS.values().toArray(new Block[0])).build(null);
		TRAPPED_TINYCHEST_TILEENTITYTYPE.setRegistryName("atmospheric_trapped_tinychest");
		
		
		event.getRegistry().register(TINYCHEST_TILEENTITYTYPE);
		event.getRegistry().register(TRAPPED_TINYCHEST_TILEENTITYTYPE);
	}
	
	private static void addChest(String name, Block from) 
	{
		addChest(name, Block.Properties.from(from));
	}

	private static void addChest(String name, Block.Properties props) 
	{
		TINYCHEST_BLOCKS.put(name + "_tinychest", new AtmosphericTinyChestBlock(name, props).setRegistryName(name + "_tinychest"));
		TRAPPED_TINYCHEST_BLOCKS.put(name + "_trapped_tinychest", new AtmosphericTinyTrappedChestBlock(name, props).setRegistryName(name + "_trapped_tinychest"));
	}
}
