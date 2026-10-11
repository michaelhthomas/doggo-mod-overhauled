package net.lordmrk.dmo.client.gui.screen.ingame;

import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class DogBowlScreen extends AbstractContainerScreen<AbstractContainerMenu> {
	
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/gui/dog_bowl.png");
	
	public DogBowlScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
		this.imageHeight = 130;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
		//RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
		context.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
	}
	
	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
	}
	
	@Override
    protected void init() {
        super.init();
        // Center the title
        this.titleLabelX = (this.imageWidth - this.font.width(title)) / 2;
    }
}
