package com.lowheatpizza.kitchen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class KitchenRenderer {
    private final KitchenLayout layout;
    private final boolean debug;

    public KitchenRenderer(KitchenLayout layout, boolean debug) {
        this.layout = layout;
        this.debug = debug;
    }

    public String getBackgroundPath() {
        return debug ? layout.backgroundNumbered : layout.backgroundClean;
    }

    public void drawFit(Batch batch, Texture texture, KitchenSlot slot) {
        batch.draw(texture, slot.x, slot.y, slot.width, slot.height);
    }

    public void drawContain(Batch batch, Texture texture, KitchenSlot slot) {
        float scale = Math.min(slot.width / texture.getWidth(), slot.height / texture.getHeight());
        float drawWidth = texture.getWidth() * scale;
        float drawHeight = texture.getHeight() * scale;
        float drawX = slot.x + (slot.width - drawWidth) / 2f;
        float drawY = slot.y + (slot.height - drawHeight) / 2f;
        batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }

    public void draw(Batch batch, Texture texture, KitchenSlot slot) {
        if ("fit".equalsIgnoreCase(slot.drawMode)) {
            drawFit(batch, texture, slot);
        } else {
            drawContain(batch, texture, slot);
        }
    }
}

