package ninjaphenix.chest_helmets.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class ChestScreen extends HandledScreen<ChestScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/generic_54.png");

    public ChestScreen(ChestScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 168;
        playerInventoryTitleY = backgroundHeight - 94;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack stack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, ChestScreen.TEXTURE);

        this.drawTexture(stack, x, y, 0, 0, backgroundWidth, 71);
        this.drawTexture(stack, x, y + 71, 0, 126, backgroundWidth, 96);
    }
}
