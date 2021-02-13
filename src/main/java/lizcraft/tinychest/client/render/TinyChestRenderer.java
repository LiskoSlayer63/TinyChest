package lizcraft.tinychest.client.render;

import java.util.Calendar;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import lizcraft.tinychest.common.CommonContent;
import lizcraft.tinychest.common.block.TinyChestBlock;
import lizcraft.tinychest.common.tile.TinyChestTileEntity;
import lizcraft.tinychest.common.tile.TinyEnderChestTileEntity;
import lizcraft.tinychest.common.tile.TinyTrappedChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class TinyChestRenderer<T extends TileEntity & IChestLid> extends TileEntityRenderer<T>
{
	private final ModelRenderer singleLid;
	private final ModelRenderer singleBottom;
	private final ModelRenderer singleLatch;
	private final ModelRenderer rightLid;
	private final ModelRenderer rightBottom;
	private final ModelRenderer rightLatch;
	private final ModelRenderer leftLid;
	private final ModelRenderer leftBottom;
	private final ModelRenderer leftLatch;
	private boolean isChristmas;

	public TinyChestRenderer(TileEntityRendererDispatcher rendererDispatcherIn) 
	{
		super(rendererDispatcherIn);
		
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
	         this.isChristmas = true;
		
		this.singleBottom = new ModelRenderer(64, 64, 0, 19);
		this.singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
		
		this.singleLid = new ModelRenderer(64, 64, 0, 0);
		this.singleLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
		this.singleLid.rotationPointY = 9.0F;
		this.singleLid.rotationPointZ = 1.0F;
		
		this.singleLatch = new ModelRenderer(64, 64, 0, 0);
		this.singleLatch.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
		this.singleLatch.rotationPointY = 8.0F;
		
		this.rightBottom = new ModelRenderer(64, 64, 0, 19);
		this.rightBottom.addBox(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, 0.0F);
		
		this.rightLid = new ModelRenderer(64, 64, 0, 0);
		this.rightLid.addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
		this.rightLid.rotationPointY = 9.0F;
		this.rightLid.rotationPointZ = 1.0F;
		
		this.rightLatch = new ModelRenderer(64, 64, 0, 0);
		this.rightLatch.addBox(15.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
		this.rightLatch.rotationPointY = 8.0F;
		
		this.leftBottom = new ModelRenderer(64, 64, 0, 19);
		this.leftBottom.addBox(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, 0.0F);
		
		this.leftLid = new ModelRenderer(64, 64, 0, 0);
		this.leftLid.addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
		this.leftLid.rotationPointY = 9.0F;
		this.leftLid.rotationPointZ = 1.0F;
		
		this.leftLatch = new ModelRenderer(64, 64, 0, 0);
		this.leftLatch.addBox(0.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
		this.leftLatch.rotationPointY = 8.0F;
	}
	
	@Override
	public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) 
	{
		World world = tileEntityIn.getWorld();
	    BlockState blockstate = world != null ? tileEntityIn.getBlockState() : CommonContent.TINYCHEST_BLOCK.getDefaultState().with(TinyChestBlock.FACING, Direction.SOUTH);
		
		matrixStackIn.push();
		
		boolean isDouble = tileEntityIn instanceof TinyChestTileEntity ? blockstate.get(TinyChestBlock.DOUBLE_CHEST) : false;
		float rotAngle = blockstate.get(TinyChestBlock.FACING).getHorizontalAngle();

        float lidAngle = 1.0F - tileEntityIn.getLidAngle(partialTicks);
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;

        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-rotAngle));
	    matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        
        if (!isDouble)
        {
		    matrixStackIn.translate(-0.5D, -1.0D, -0.5D);
		    
			IVertexBuilder renderBuffer = this.getBuffer(bufferIn, tileEntityIn, ChestType.SINGLE, this.isChristmas);
		    
		    this.renderModels(matrixStackIn, renderBuffer, this.singleLid, this.singleLatch, this.singleBottom, lidAngle, combinedLightIn, combinedOverlayIn);
        } 
        else
        {
		    matrixStackIn.translate(-1.0D, -1.0D, -0.5D);

		    IVertexBuilder renderBuffer = this.getBuffer(bufferIn, tileEntityIn, ChestType.RIGHT, this.isChristmas);
		    
		    this.renderModels(matrixStackIn, renderBuffer, this.rightLid, this.rightLatch, this.rightBottom, lidAngle, combinedLightIn, combinedOverlayIn);
		    
		    matrixStackIn.translate(1.0D, 0.0D, 0.0D);

		    renderBuffer = this.getBuffer(bufferIn, tileEntityIn, ChestType.LEFT, this.isChristmas);
		    
		    this.renderModels(matrixStackIn, renderBuffer, this.leftLid, this.leftLatch, this.leftBottom, lidAngle, combinedLightIn, combinedOverlayIn);
        }
        
	    matrixStackIn.pop();
	}
	
	private void renderModels(MatrixStack matrixStackIn, IVertexBuilder bufferIn, ModelRenderer chestLid, ModelRenderer chestLatch, ModelRenderer chestBottom, float lidAngle, int packedLightIn, int packedOverlayIn)
	{
		chestLid.rotateAngleX = -(lidAngle * ((float)Math.PI / 2F));
		chestLatch.rotateAngleX = chestLid.rotateAngleX;
		
		chestBottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		chestLid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		chestLatch.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}
	
	protected IVertexBuilder getBuffer(IRenderTypeBuffer bufferIn, TileEntity tileEntity, ChestType type, boolean isHoliday)
	{
		RenderMaterial renderMaterial = this.getRenderMaterial(tileEntity, type, isHoliday);
	    return renderMaterial.getBuffer(bufferIn, RenderType::getEntityCutout);
	}
	
	protected RenderMaterial getRenderMaterial(TileEntity tileEntity, ChestType type, boolean isHoliday)
	{
		if (isHoliday)
			return getRenderMaterial(type, Atlases.CHEST_XMAS_MATERIAL, Atlases.CHEST_XMAS_LEFT_MATERIAL, Atlases.CHEST_XMAS_RIGHT_MATERIAL);
		else if (tileEntity instanceof TinyTrappedChestTileEntity)
			return getRenderMaterial(type, Atlases.CHEST_TRAPPED_MATERIAL, Atlases.CHEST_TRAPPED_LEFT_MATERIAL, Atlases.CHEST_TRAPPED_RIGHT_MATERIAL);
		else if (tileEntity instanceof TinyEnderChestTileEntity)
			return Atlases.ENDER_CHEST_MATERIAL;
		else
			return getRenderMaterial(type, Atlases.CHEST_MATERIAL, Atlases.CHEST_LEFT_MATERIAL, Atlases.CHEST_RIGHT_MATERIAL);
	}
	
	protected static RenderMaterial getRenderMaterial(ChestType type, RenderMaterial single, RenderMaterial left, RenderMaterial right)
	{
		switch (type)
		{
			case LEFT:
				return left;
			case RIGHT:
				return right;
			default:
				return single;
		}
	}
}
