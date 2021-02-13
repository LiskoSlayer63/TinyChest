package lizcraft.tinychest.compat.quark.client.render;

import java.util.HashMap;
import java.util.Map;

import lizcraft.tinychest.client.render.TinyChestRenderer;
import lizcraft.tinychest.compat.quark.common.IQuarkChestTextureProvider;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class QuarkTinyChestRenderer<T extends TileEntity & IChestLid> extends TinyChestRenderer<T>
{
	private static Map<Block, ChestTextureBatch> chestTextures = new HashMap<>();
	public static Block invBlock = null; 
	
	public QuarkTinyChestRenderer(TileEntityRendererDispatcher rendererDispatcherIn) 
	{
		super(rendererDispatcherIn);
	}

	@Override
	protected RenderMaterial getRenderMaterial(TileEntity tileEntity, ChestType type, boolean isHoliday)
	{
		if (isHoliday)
			return getRenderMaterial(type, Atlases.CHEST_XMAS_MATERIAL, Atlases.CHEST_XMAS_LEFT_MATERIAL, Atlases.CHEST_XMAS_RIGHT_MATERIAL);
		else
			return getRenderMaterial(tileEntity, type);
	}
	
	private static RenderMaterial getRenderMaterial(TileEntity chest, ChestType type)
	{
		Block block = invBlock;
		if(block == null)
			block = chest.getBlockState().getBlock();
		
		ChestTextureBatch batch = chestTextures.get(block);
		if(batch == null)
			return null;
		
		switch(type) 
		{
			case LEFT: 
				return batch.left;
			case RIGHT: 
				return batch.right;
			default: 
				return batch.normal;
		}
	}
	
	public static void addBlock(Block chest)
	{
		if(chest instanceof IQuarkChestTextureProvider) 
		{
			IQuarkChestTextureProvider prov = (IQuarkChestTextureProvider) chest;

			String path = prov.getChestTexturePath();
			
			if (prov.isTrap())
				addTexture(chest, path, "trap", "trap_left", "trap_right");
			else
				addTexture(chest, path, "normal", "left", "right");
		}
	}
	
	private static void addTexture(Block chest, String path, String normal, String left, String right)
	{
		ResourceLocation resNormal = new ResourceLocation("quark", path + normal);
		ResourceLocation resLeft = new ResourceLocation("quark", path + left);
		ResourceLocation resRight = new ResourceLocation("quark", path + right);
		
		ChestTextureBatch batch = new ChestTextureBatch(Atlases.CHEST_ATLAS, resNormal, resLeft, resRight);
		chestTextures.put(chest, batch);
	}
	
	private static class ChestTextureBatch 
	{
		public final RenderMaterial normal, left, right;

		public ChestTextureBatch(ResourceLocation atlas, ResourceLocation normal, ResourceLocation left, ResourceLocation right) 
		{
			this.normal = new RenderMaterial(atlas, normal);
			this.left = new RenderMaterial(atlas, left);
			this.right = new RenderMaterial(atlas, right);
		}
	}
}
