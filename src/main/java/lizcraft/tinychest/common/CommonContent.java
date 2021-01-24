package lizcraft.tinychest.common;

import lizcraft.tinychest.client.render.ItemStackRenderer;
import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.common.gui.TinyChestContainer;
import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonContent 
{
	public static Block TINYCHEST_BLOCK;
	
	public static BlockItem TINYCHEST_ITEM;

	public static TileEntityType<TinyChestTileEntity> TINYCHEST_TILEENTITYTYPE;
	
	public static ContainerType<TinyChestContainer> TINYCHEST_CONTAINER;
	
	public static void init()
	{
		// Initialize blocks
		
		TINYCHEST_BLOCK = new TinyChestBlock(
				AbstractBlock.Properties.create(Material.WOOD, MaterialColor.WOOD).
				sound(SoundType.WOOD).
				hardnessAndResistance(1.5F)
				).setRegistryName("tinychest");

		
		// Initialize items
		
		TINYCHEST_ITEM = (BlockItem) new BlockItem(TINYCHEST_BLOCK, new Item.Properties().
				group(ItemGroup.DECORATIONS).
				setISTER(() -> ItemStackRenderer::new)
				).setRegistryName(TINYCHEST_BLOCK.getRegistryName());
		
		
		// Initialize entity types
		
		TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(TinyChestTileEntity::new, TINYCHEST_BLOCK).build(null);
		TINYCHEST_TILEENTITYTYPE.setRegistryName(TINYCHEST_BLOCK.getRegistryName());

		
		// Initialize containers
		
		TINYCHEST_CONTAINER = IForgeContainerType.create(TinyChestContainer::new);
		TINYCHEST_CONTAINER.setRegistryName(TINYCHEST_BLOCK.getRegistryName());
	}
	
	public static void register(IEventBus eventBus)
	{
		eventBus.register(CommonContent.class);
	}
	
	@SubscribeEvent
	public static void onBlocksRegistration(final RegistryEvent.Register<Block> event) 
	{
    	event.getRegistry().register(TINYCHEST_BLOCK);
	}

	@SubscribeEvent
	public static void onItemsRegistration(final RegistryEvent.Register<Item> event) 
	{
		event.getRegistry().register(TINYCHEST_ITEM);
	}

	@SubscribeEvent
	public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) 
	{
		event.getRegistry().register(TINYCHEST_TILEENTITYTYPE);
	}

	@SubscribeEvent
	public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event)
	{
		event.getRegistry().register(TINYCHEST_CONTAINER);
	}
}
