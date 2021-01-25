package lizcraft.tinychest.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import lizcraft.tinychest.common.tile.TrappedTinyChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ItemStackRenderer extends ItemStackTileEntityRenderer 
{
	public static final ItemStackTileEntityRenderer INSTANCE = new ItemStackRenderer();
	
	private final TinyChestTileEntity tinyChest = new TinyChestTileEntity();
	private final TinyChestTileEntity trapTinyChest = new TrappedTinyChestTileEntity();
	
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) 
	{
		Item item = stack.getItem();
		
		if (item instanceof BlockItem) 
		{
			Block block = ((BlockItem)item).getBlock();
			TileEntity tileEntity;
			
			if (block == CommonContent.TINYCHEST_BLOCK)
				tileEntity = this.tinyChest;
			else if (block == CommonContent.TRAPPED_TINYCHEST_BLOCK)
				tileEntity = this.trapTinyChest;
			else
				return;
			
			TileEntityRendererDispatcher.instance.renderItem(tileEntity, matrixStack, buffer, combinedLight, combinedOverlay);
		}
	}
}