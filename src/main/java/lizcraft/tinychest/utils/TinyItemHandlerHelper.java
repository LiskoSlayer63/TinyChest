package lizcraft.tinychest.utils;

import java.util.Random;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class TinyItemHandlerHelper 
{
	private static final Random RANDOM = new Random();
	
	public static void dropInventoryItemsAtRange(World worldIn, BlockPos pos, IItemHandler inventory, int min, int max)
	{
		dropInventoryItemsAtRange(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory, min, max);
	}
	
	public static void dropInventoryItemsAtRange(World worldIn, int posX, int posY, int posZ, IItemHandler inventory, int min, int max)
	{
		for (int i = min; i < max; i++)
			spawnItemStack(worldIn, posX, posY, posZ, inventory.extractItem(i, Integer.MAX_VALUE, false));
	}
	
	public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory)
	{
		dropInventoryItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory);
	}
	
	public static void dropInventoryItems(World worldIn, int posX, int posY, int posZ, IItemHandler inventory)
	{
		for (int i = 0; i < inventory.getSlots(); i++)
			spawnItemStack(worldIn, posX, posY, posZ, inventory.getStackInSlot(i));
	}
	
	public static void spawnItemStack(World worldIn, double posX, double posY, double posZ, ItemStack stack) 
	{
		double width = (double)EntityType.ITEM.getWidth();
		double d1 = 1.0D - width;
		double d2 = width / 3.0D; // Original: 2.0D
		double x = Math.floor(posX) + RANDOM.nextDouble() * d1 + d2;
		double y = Math.floor(posY) + RANDOM.nextDouble() * d1;
		double z = Math.floor(posZ) + RANDOM.nextDouble() * d1 + d2;

		while(!stack.isEmpty()) 
		{
			ItemEntity itementity = new ItemEntity(worldIn, x, y, z, stack.split(RANDOM.nextInt(21) + 10));
			double multi = 0.025D; // Original: 0.05D
			itementity.setMotion(RANDOM.nextGaussian() * multi, RANDOM.nextGaussian() * multi + 0.2D, RANDOM.nextGaussian() * multi);
			worldIn.addEntity(itementity);
		}
	}
}
