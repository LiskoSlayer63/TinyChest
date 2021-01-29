package lizcraft.tinychest.common;

import lizcraft.tinychest.TinyChest;
import lizcraft.tinychest.client.render.ItemStackRenderer;
import lizcraft.tinychest.common.block.TinyEnderChestBlock;
import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.common.block.TinyTrappedChestBlock;
import lizcraft.tinychest.common.capability.TinyEnderInventoryCapability;
import lizcraft.tinychest.common.gui.TinyChestContainer;
import lizcraft.tinychest.common.tile.TinyEnderChestTileEntity;
import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import lizcraft.tinychest.common.tile.TinyTrappedChestTileEntity;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonContent 
{
	public static final ResourceLocation ENDER_TINYCHEST_CAPABILITY = new ResourceLocation(TinyChest.MOD_ID, "ender_inventory");
	
	public static Block TINYCHEST_BLOCK;
	public static Block TRAPPED_TINYCHEST_BLOCK;
	public static Block ENDER_TINYCHEST_BLOCK;
	
	public static Item TINYCHEST_ITEM;
	public static Item TRAPPED_TINYCHEST_ITEM;
	public static Item ENDER_TINYCHEST_ITEM;

	public static TileEntityType<TinyChestTileEntity> TINYCHEST_TILEENTITYTYPE;
	public static TileEntityType<TinyTrappedChestTileEntity> TRAPPED_TINYCHEST_TILEENTITYTYPE;
	public static TileEntityType<TinyEnderChestTileEntity> ENDER_TINYCHEST_TILEENTITYTYPE;
	
	public static ContainerType<TinyChestContainer> TINYCHEST_CONTAINER;
	
	public static void register(IEventBus eventBus)
	{
		eventBus.register(CommonContent.class);
	}
	
	@SubscribeEvent
	public static void onCommonSetup(final FMLCommonSetupEvent event)
	{
		TinyEnderInventoryCapability.register();
	}
	
	@SubscribeEvent
	public static void onBlocksRegistration(final RegistryEvent.Register<Block> event) 
	{
		TINYCHEST_BLOCK = new TinyChestBlock(
				AbstractBlock.Properties.create(Material.WOOD, MaterialColor.WOOD).
				sound(SoundType.WOOD).
				hardnessAndResistance(1.5F)
				).setRegistryName("tinychest");
		
		TRAPPED_TINYCHEST_BLOCK = new TinyTrappedChestBlock(
				AbstractBlock.Properties.create(Material.WOOD, MaterialColor.WOOD).
				sound(SoundType.WOOD).
				hardnessAndResistance(1.5F)
				).setRegistryName("trapped_tinychest");
		
		ENDER_TINYCHEST_BLOCK = new TinyEnderChestBlock(
				AbstractBlock.Properties.create(Material.ROCK).
				setRequiresTool().
				hardnessAndResistance(22.5F, 600.0F).
				setLightLevel((state) -> 3)
				).setRegistryName("ender_tinychest");
		
		
		event.getRegistry().register(TINYCHEST_BLOCK);
		event.getRegistry().register(TRAPPED_TINYCHEST_BLOCK);
		event.getRegistry().register(ENDER_TINYCHEST_BLOCK);
	}

	@SubscribeEvent
	public static void onItemsRegistration(final RegistryEvent.Register<Item> event) 
	{
		TINYCHEST_ITEM = new BlockItem(TINYCHEST_BLOCK, new Item.Properties().
				group(ItemGroup.DECORATIONS).
				setISTER(() -> () -> ItemStackRenderer.INSTANCE)
				).setRegistryName(TINYCHEST_BLOCK.getRegistryName());
		
		TRAPPED_TINYCHEST_ITEM = new BlockItem(TRAPPED_TINYCHEST_BLOCK, new Item.Properties().
				group(ItemGroup.REDSTONE).
				setISTER(() -> () -> ItemStackRenderer.INSTANCE)
				).setRegistryName(TRAPPED_TINYCHEST_BLOCK.getRegistryName());
		
		ENDER_TINYCHEST_ITEM = new BlockItem(ENDER_TINYCHEST_BLOCK, new Item.Properties().
				group(ItemGroup.DECORATIONS).
				setISTER(() -> () -> ItemStackRenderer.INSTANCE)
				).setRegistryName(ENDER_TINYCHEST_BLOCK.getRegistryName());
		
		
		event.getRegistry().register(TINYCHEST_ITEM);
		event.getRegistry().register(TRAPPED_TINYCHEST_ITEM);
		event.getRegistry().register(ENDER_TINYCHEST_ITEM);
	}

	@SubscribeEvent
	public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) 
	{
		TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(TinyChestTileEntity::new, TINYCHEST_BLOCK).build(null);
		TINYCHEST_TILEENTITYTYPE.setRegistryName(TINYCHEST_BLOCK.getRegistryName());

		TRAPPED_TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(TinyTrappedChestTileEntity::new, TRAPPED_TINYCHEST_BLOCK).build(null);
		TRAPPED_TINYCHEST_TILEENTITYTYPE.setRegistryName(TRAPPED_TINYCHEST_BLOCK.getRegistryName());

		ENDER_TINYCHEST_TILEENTITYTYPE = TileEntityType.Builder.create(TinyEnderChestTileEntity::new, ENDER_TINYCHEST_BLOCK).build(null);
		ENDER_TINYCHEST_TILEENTITYTYPE.setRegistryName(ENDER_TINYCHEST_BLOCK.getRegistryName());
		
		
		event.getRegistry().register(TINYCHEST_TILEENTITYTYPE);
		event.getRegistry().register(TRAPPED_TINYCHEST_TILEENTITYTYPE);
		event.getRegistry().register(ENDER_TINYCHEST_TILEENTITYTYPE);
	}

	@SubscribeEvent
	public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event)
	{
		TINYCHEST_CONTAINER = IForgeContainerType.create(TinyChestContainer::new);
		TINYCHEST_CONTAINER.setRegistryName(TINYCHEST_BLOCK.getRegistryName());
		
		
		event.getRegistry().register(TINYCHEST_CONTAINER);
	}
}
