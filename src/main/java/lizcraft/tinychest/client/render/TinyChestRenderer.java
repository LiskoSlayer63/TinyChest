package lizcraft.tinychest.client.render;

import java.util.Calendar;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class TinyChestRenderer extends TileEntityRenderer<TinyChestTileEntity>
{
	private static final TinyChestModel MODEL = new TinyChestModel();
	
	private boolean isChristmas;

	public TinyChestRenderer(TileEntityRendererDispatcher rendererDispatcherIn) 
	{
		super(rendererDispatcherIn);
		
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
	         this.isChristmas = true;
	}
	
	@Override
	public void render(TinyChestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) 
	{
		World world = tileEntityIn.getWorld();
	    BlockState blockstate = world != null ? tileEntityIn.getBlockState() : CommonContent.TINYCHEST_BLOCK.getDefaultState().with(TinyChestBlock.FACING, Direction.SOUTH);
		
		matrixStackIn.push();
		
		float rotAngle = blockstate.get(ChestBlock.FACING).getHorizontalAngle();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-rotAngle));
        matrixStackIn.translate(-0.25D, -0.5D, -0.25D);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);

        float lidAngle = 1.0F - tileEntityIn.getLidAngle(partialTicks);
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        
		RenderMaterial renderMaterial = Atlases.getChestMaterial(tileEntityIn, ChestType.SINGLE, this.isChristmas);
        IVertexBuilder renderBuffer = renderMaterial.getBuffer(bufferIn, RenderType::getEntityCutout);
        
	    MODEL.render(matrixStackIn, renderBuffer, combinedLightIn, combinedOverlayIn, lidAngle);

	    matrixStackIn.pop();
	}
	
	
	/*
	 * SINGLE CHEST MODEL
	 */
	
	public static class TinyChestModel extends Model 
	{
		private final ModelRenderer BOTTOM;
		private final ModelRenderer LID;
		private final ModelRenderer LATCH;
	    
	    private float lidAngle = 0F;

		public TinyChestModel() 
		{
			super(RenderType::getEntityCutout);
			
			textureWidth = 64;
			textureHeight = 64;
			
			BOTTOM = new ModelRenderer(64, 64, 0, 19);
			BOTTOM.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
			
			LID = new ModelRenderer(64, 64, 0, 0);
			LID.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
			LID.rotationPointY = 9.0F;
			LID.rotationPointZ = 1.0F;
			
			LATCH = new ModelRenderer(64, 64, 0, 0);
			LATCH.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
			LATCH.rotationPointY = 8.0F;
		}
		
		public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float lidAngle)
		{
			this.lidAngle = lidAngle;
			render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		@Override
		public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) 
		{
			LID.rotateAngleX = -(lidAngle * ((float)Math.PI / 2F));
			LATCH.rotateAngleX = LID.rotateAngleX;
			BOTTOM.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			LID.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			LATCH.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
	}
}
