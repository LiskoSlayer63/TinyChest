package lizcraft.tinychest.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import lizcraft.tinychest.common.gui.TinyChestContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TinyChestContainerScreen extends ContainerScreen<TinyChestContainer> 
{
	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/hopper.png");
	
	public TinyChestContainerScreen(TinyChestContainer container, PlayerInventory inv, ITextComponent titleIn) 
	{
		super(container, inv, titleIn);
	    this.passEvents = false;
	    this.ySize = 133;
	    this.playerInventoryTitleY = this.ySize - 94;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) 
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) 
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	    this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);

	    int edgeSpacingX = (this.width - this.xSize) / 2;
	    int edgeSpacingY = (this.height - this.ySize) / 2;
	    this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
	}
}
