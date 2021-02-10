package lizcraft.tinychest.compat.quark.client.render;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import lizcraft.tinychest.compat.quark.common.QuarkCommon;
import lizcraft.tinychest.compat.quark.common.tile.VariantTinyChestTileEntity;
import lizcraft.tinychest.compat.quark.common.tile.VariantTinyTrappedChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Lazy;

public class VariantItemStackRenderer extends ItemStackTileEntityRenderer
{
	public static final ItemStackTileEntityRenderer INSTANCE = new VariantItemStackRenderer();
	
	private final Supplier<VariantTinyChestTileEntity> tinyChest = Lazy.of(VariantTinyChestTileEntity::new);
	private final Supplier<VariantTinyTrappedChestTileEntity> trapTinyChest = Lazy.of(VariantTinyTrappedChestTileEntity::new);
	
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) 
	{
		Item item = stack.getItem();
		
		if (item instanceof BlockItem) 
		{
			Block block = ((BlockItem)item).getBlock();
			TileEntity tileEntity;
			
			if (QuarkCommon.VARIANT_TINYCHEST_BLOCKS.containsValue(block))
				tileEntity = this.tinyChest.get();
			else if (QuarkCommon.VARIANT_TRAPPED_TINYCHEST_BLOCKS.containsValue(block))
				tileEntity = this.trapTinyChest.get();
			else
				return;

			VariantTinyChestRenderer.invBlock = block;
			TileEntityRendererDispatcher.instance.renderItem(tileEntity, matrixStack, buffer, combinedLight, combinedOverlay);
			VariantTinyChestRenderer.invBlock = null;
		}
	}
}
