package com.lowheatpizza.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.lowheatpizza.LowHeatPizzaGame;
import com.lowheatpizza.models.Pizza;
import com.lowheatpizza.models.Topping;

public class PizzaPreviewActor extends Actor {
    private final LowHeatPizzaGame game;
    private final Pizza pizza;
    private final Array<Vector2> toppingSlots = new Array<>();

    public PizzaPreviewActor(LowHeatPizzaGame game, Pizza pizza) {
        this.game = game;
        this.pizza = pizza;
        setSize(320f, 320f);
        buildToppingSlots();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color previous = batch.getColor().cpy();
        batch.setColor(1f, 1f, 1f, parentAlpha);

        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;
        float doughSize = Math.min(getWidth(), getHeight()) * 0.82f;

        drawLayer(batch, game.getAssets().getTexture("images/kitchen/dough.png"), centerX, centerY, doughSize, doughSize, 1f);

        if (pizza.hasSauce()) {
            drawLayer(batch, game.getAssets().getTexture("images/kitchen/sauce_layer.png"), centerX, centerY, doughSize * 0.92f, doughSize * 0.92f, 0.92f);
        }
        if (pizza.hasCheese()) {
            drawLayer(batch, game.getAssets().getTexture("images/kitchen/cheese_layer.png"), centerX, centerY, doughSize * 0.88f, doughSize * 0.88f, 0.95f);
        }

        int slotIndex = 0;
        for (String toppingId : pizza.getToppings()) {
            Topping topping = game.getSession().getToppingManager().getById(toppingId);
            if (topping == null) {
                continue;
            }

            Texture texture = game.getAssets().getTexture(topping.getAssetPath());
            for (int copies = 0; copies < 5; copies++) {
                Vector2 slot = toppingSlots.get(slotIndex % toppingSlots.size);
                float size = 34f + (copies % 2) * 6f;
                batch.draw(
                    texture,
                    centerX + slot.x - size / 2f,
                    centerY + slot.y - size / 2f,
                    size / 2f,
                    size / 2f,
                    size,
                    size,
                    1f,
                    1f,
                    (slotIndex * 19f) % 360f,
                    0,
                    0,
                    texture.getWidth(),
                    texture.getHeight(),
                    false,
                    false
                );
                slotIndex++;
            }
        }

        if (pizza.isBaked()) {
            batch.setColor(1f, 0.78f, 0.45f, parentAlpha * 0.2f);
            batch.draw(game.getAssets().getTexture("images/kitchen/cheese_layer.png"), centerX - doughSize / 2f, centerY - doughSize / 2f, doughSize, doughSize);
        }

        batch.setColor(previous);
    }

    private void drawLayer(Batch batch, Texture texture, float centerX, float centerY, float width, float height, float alpha) {
        batch.setColor(1f, 1f, 1f, getColor().a * alpha);
        batch.draw(texture, centerX - width / 2f, centerY - height / 2f, width, height);
    }

    private void buildToppingSlots() {
        toppingSlots.clear();
        for (int i = 0; i < 14; i++) {
            float angleDeg = (360f / 14f) * i;
            float radius = (i % 2 == 0) ? 74f : 50f;
            toppingSlots.add(new Vector2(
                MathUtils.cosDeg(angleDeg) * radius,
                MathUtils.sinDeg(angleDeg) * radius
            ));
        }
    }
}

