package team.lodestar.wayward_attributes.client;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.neoforged.api.distmarker.*;
import org.joml.*;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.wayward_attributes.*;


@OnlyIn(Dist.CLIENT)
public class AttributeTooltipComponent implements ClientTooltipComponent {

    private static final int ICON_SIZE = 8;
    private static final int ICON_PADDING = 3;

    protected final AttributeDisplay display;
    protected final FormattedCharSequence text;
    protected final int color;

    public AttributeTooltipComponent(AttributeDisplay display, FormattedCharSequence text, int color) {
        this.display = display;
        this.text = text;
        this.color = color;
    }
    
    @Override
    public int getWidth(Font font) {
        return font.width(this.text) + ICON_SIZE + ICON_PADDING;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        VFXBuilders.createScreen()
                .setShader(GameRenderer::getPositionTexColorShader)
                .setPositionWithWidth(x + 4, y, ICON_SIZE, ICON_SIZE)
                .setSprite(display.texture())
                .setColor(color)
                .setAlpha(1f)
                .blit(guiGraphics);
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        font.drawInBatch(this.text, x + ICON_SIZE + ICON_PADDING, y, -1, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }
}