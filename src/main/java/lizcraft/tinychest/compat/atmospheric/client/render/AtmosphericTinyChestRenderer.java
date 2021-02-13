package lizcraft.tinychest.compat.atmospheric.client.render;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import lizcraft.tinychest.client.render.TinyChestRenderer;
import lizcraft.tinychest.compat.atmospheric.common.IAbnormalsChestBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class AtmosphericTinyChestRenderer<T extends TileEntity & IChestLid> extends TinyChestRenderer<T>
{
	public static Block itemBlock = null;
	
	public AtmosphericTinyChestRenderer(TileEntityRendererDispatcher rendererDispatcherIn) 
	{
		super(rendererDispatcherIn);
	}

	@Override
	protected IVertexBuilder getBuffer(IRenderTypeBuffer bufferIn, TileEntity tileEntity, ChestType type, boolean isHoliday)
	{
		return bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(this.getChestTexture(tileEntity, type, isHoliday)));
	}
	
	public ResourceLocation getChestTexture(TileEntity t, ChestType type, boolean isHoliday) 
	{
		Block inventoryBlock = itemBlock;
		if(inventoryBlock == null) 
			inventoryBlock = t.getBlockState().getBlock();
		IAbnormalsChestBlock block = (IAbnormalsChestBlock) inventoryBlock;
		
		String chestType = block.getChestName() + (block.isTrapped() ? "/trapped" : "/normal");
		String modid = block.getModid();
		
		if (isHoliday) 
		{
			chestType = "christmas"; 
			modid = "minecraft";
		}

		switch(type) 
		{
			default:
			case SINGLE:	return new ResourceLocation(modid, "textures/entity/chest/" + chestType +".png");
			case LEFT: 		return new ResourceLocation(modid, "textures/entity/chest/" + chestType + "_left.png");
			case RIGHT: 	return new ResourceLocation(modid, "textures/entity/chest/" + chestType + "_right.png");
		}
	}
}
