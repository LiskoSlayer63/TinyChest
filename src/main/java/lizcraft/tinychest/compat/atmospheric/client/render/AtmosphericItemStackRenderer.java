package lizcraft.tinychest.compat.atmospheric.client.render;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import lizcraft.tinychest.compat.atmospheric.common.AtmosphericCommon;
import lizcraft.tinychest.compat.atmospheric.common.tile.AtmosphericTinyChestTileEntity;
import lizcraft.tinychest.compat.atmospheric.common.tile.AtmosphericTinyTrappedChestTileEntity;
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

public class AtmosphericItemStackRenderer  extends ItemStackTileEntityRenderer
{
public static final ItemStackTileEntityRenderer INSTANCE = new AtmosphericItemStackRenderer();
	
	private final Supplier<AtmosphericTinyChestTileEntity> tinyChest = Lazy.of(AtmosphericTinyChestTileEntity::new);
	private final Supplier<AtmosphericTinyTrappedChestTileEntity> trapTinyChest = Lazy.of(AtmosphericTinyTrappedChestTileEntity::new);
	
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) 
	{
		Item item = stack.getItem();
		
		if (item instanceof BlockItem) 
		{
			Block block = ((BlockItem)item).getBlock();
			TileEntity tileEntity;
			
			if (AtmosphericCommon.TINYCHEST_BLOCKS.containsValue(block))
				tileEntity = this.tinyChest.get();
			else if (AtmosphericCommon.TRAPPED_TINYCHEST_BLOCKS.containsValue(block))
				tileEntity = this.trapTinyChest.get();
			else
				return;

			AtmosphericTinyChestRenderer.itemBlock = block;
			TileEntityRendererDispatcher.instance.renderItem(tileEntity, matrixStack, buffer, combinedLight, combinedOverlay);
			AtmosphericTinyChestRenderer.itemBlock = null;
		}
	}
}
