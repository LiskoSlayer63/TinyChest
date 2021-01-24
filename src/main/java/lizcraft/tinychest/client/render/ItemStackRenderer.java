package lizcraft.tinychest.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class ItemStackRenderer extends ItemStackTileEntityRenderer 
{
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) 
	{
		TinyChestTileEntity tileentity = new TinyChestTileEntity();
		TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
	}
}
