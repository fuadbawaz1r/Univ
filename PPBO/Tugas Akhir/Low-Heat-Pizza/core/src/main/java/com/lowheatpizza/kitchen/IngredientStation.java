package com.lowheatpizza.kitchen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IngredientStation extends WidgetGroup {
    public interface Listener {
        void clicked(String ingredientId);
    }

    private final String ingredientId;
    private final KitchenRenderer renderer;
    private final KitchenSlot slot;
    private final Image stationImage;
    private final Image lockImage;
    private final Label priceLabel;

    public IngredientStation(String ingredientId, Skin skin, KitchenRenderer renderer, KitchenSlot slot, Texture texture, Texture lockTexture, Listener listener) {
        this.ingredientId = ingredientId;
        this.renderer = renderer;
        this.slot = slot;
        Stack stack = new Stack();
        stationImage = new Image(texture);
        stationImage.setVisible(false);

        Table overlay = new Table();
        overlay.top().right().pad(8);
        priceLabel = new Label("", skin, "dark");
        priceLabel.setColor(Color.valueOf("4A2419"));
        overlay.add(priceLabel).right();

        Table lockOverlay = new Table();
        lockOverlay.center();
        lockImage = new Image(lockTexture);
        lockOverlay.add(lockImage).size(slot.width * 0.36f, slot.height * 0.62f).center();

        stack.add(stationImage);
        stack.add(overlay);
        stack.add(lockOverlay);
        addActor(stack);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.clicked(ingredientId);
            }
        });

        showPrice(false);
        showLocked(false);
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        if (stationImage.getDrawable() instanceof TextureRegionDrawable) {
            TextureRegionDrawable drawable = (TextureRegionDrawable) stationImage.getDrawable();
            Texture texture = drawable.getRegion().getTexture();
            com.badlogic.gdx.graphics.Color previous = batch.getColor().cpy();
            batch.setColor(1f, 1f, 1f, stationImage.getColor().a * parentAlpha);
            renderer.draw(batch, texture, slot);
            batch.setColor(previous);
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        for (int i = 0; i < getChildren().size; i++) {
            getChildren().get(i).setBounds(0f, 0f, getWidth(), getHeight());
        }
    }

    public void setTexture(Texture texture) {
        stationImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void setClickable(boolean clickable) {
        setTouchable(clickable ? Touchable.enabled : Touchable.disabled);
    }

    public void showPrice(boolean visible) {
        priceLabel.setVisible(visible);
    }

    public void setPrice(int price) {
        priceLabel.setText("$" + price);
    }

    public void showLocked(boolean visible) {
        lockImage.setVisible(visible);
        stationImage.setColor(1f, 1f, 1f, visible ? 0.42f : 1f);
    }

    public String getIngredientId() {
        return ingredientId;
    }
}

