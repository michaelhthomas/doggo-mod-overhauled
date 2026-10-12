package net.lordmrk.dmo.client.gui.screen.ingame;

import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class DogBowlScreen extends AbstractContainerScreen<AbstractContainerMenu> {
	
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(DoggoModOverhauled.MODID, "textures/gui/dog_bowl.png");
	
	public DogBowlScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
		super(handler, inventory, title, 176, 130);
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void init() {
		super.init();
		// Center the title
		this.titleLabelX = (this.imageWidth - this.font.width(title)) / 2;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int x = (this.width - this.imageWidth) / 2;
		int y = (this.height - this.imageHeight) / 2;
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
	}
}
