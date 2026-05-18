package com.lowheatpizza.kitchen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.lowheatpizza.LowHeatPizzaGame;
import com.lowheatpizza.progress.IngredientManager;
import com.lowheatpizza.progress.IngredientProgress;
import com.lowheatpizza.utils.AssetPaths;

public class PrepArea extends Actor {
    private final LowHeatPizzaGame game;
    private final PizzaModel pizza;
    private final IngredientManager ingredientManager;
    private final Array<Vector2> pepperoniPositions;
    private final boolean afterOven;
    private final Runnable startOvenCallback;
    private float dragX = 0f;
    private float dragY = 0f;
    private boolean dragging = false;

    public PrepArea(LowHeatPizzaGame game, PizzaModel pizza, IngredientManager ingredientManager, Array<Vector2> pepperoniPositions, boolean afterOven, Runnable startOvenCallback) {
        this.game = game;
        this.pizza = pizza;
        this.ingredientManager = ingredientManager;
        this.pepperoniPositions = pepperoniPositions;
        this.afterOven = afterOven;
        this.startOvenCallback = startOvenCallback;

        addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            private float startX;
            private float startY;

            @Override
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                if (afterOven || !pizza.hasDough() || pizza.isBaked()) {
                    return false;
                }
                dragging = true;
                toFront();
                startX = x;
                startY = y;
                dragX = 0f;
                dragY = 0f;
                return true;
            }

            @Override
            public void touchDragged(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer) {
                dragX = x - startX;
                dragY = y - startY;
            }

            @Override
            public void touchUp(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                dragging = false;
                float dropWorldX = getX() + x;
                if (dropWorldX < 450f) {
                    if (startOvenCallback != null) {
                        startOvenCallback.run();
                    }
                }
                dragX = 0f;
                dragY = 0f;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (pizza.isBaking()) {
            return;
        }
        if (!afterOven && pizza.isBaked()) {
            return;
        }
        if (afterOven && !pizza.isBaked()) {
            return;
        }

        Color previous = batch.getColor().cpy();
        batch.setColor(1f, 1f, 1f, parentAlpha);

        float centerX = getX() + getWidth() / 2f - 104f + dragX;
        float centerY = getY() + getHeight() / 2f - 55f + dragY;
        float pizzaSize = Math.min(getWidth(), getHeight()) * 0.66f;
        float boardMaxWidth = pizzaSize * 1.25f;
        float boardMaxHeight = boardMaxWidth * (375f / 318f);
        float boardCenterY = centerY - boardMaxHeight * 0.076f;

        drawContained(batch, AssetPaths.KITCHEN_PIZZA_BOARD, centerX, boardCenterY, boardMaxWidth, boardMaxHeight);

        if (!pizza.hasDough()) {
            batch.setColor(previous);
            return;
        }

        if (pizza.isBaked()) {
            batch.setColor(0.92f, 0.82f, 0.72f, parentAlpha);
        } else {
            batch.setColor(1f, 1f, 1f, parentAlpha);
        }
        drawCentered(batch, AssetPaths.DOUGH, centerX, centerY, pizzaSize);

        if (pizza.hasSauce()) {
            if (pizza.isBaked()) {
                batch.setColor(0.96f, 0.92f, 0.92f, parentAlpha);
            } else {
                batch.setColor(1f, 1f, 1f, parentAlpha);
            }
            drawCentered(batch, AssetPaths.SAUCE_LAYER, centerX, centerY, pizzaSize * 0.92f);
        }

        if (pizza.hasCheese()) {
            if (pizza.isBaked()) {
                batch.setColor(0.98f, 0.94f, 0.84f, parentAlpha);
            } else {
                batch.setColor(1f, 1f, 1f, parentAlpha);
            }
            drawCentered(batch, AssetPaths.CHEESE_LAYER, centerX, centerY, pizzaSize * 0.88f);
        }

        batch.setColor(1f, 1f, 1f, parentAlpha);
        drawToppings(batch, centerX, centerY);

        batch.setColor(previous);
    }

    private void drawCentered(Batch batch, String assetPath, float centerX, float centerY, float size) {
        Texture texture = game.getAssets().getTexture(assetPath);
        batch.draw(texture, centerX - size / 2f, centerY - size / 2f, size, size);
    }

    private void drawContained(Batch batch, String assetPath, float centerX, float centerY, float maxWidth, float maxHeight) {
        Texture texture = game.getAssets().getTexture(assetPath);
        float scale = Math.min(maxWidth / texture.getWidth(), maxHeight / texture.getHeight());
        float drawWidth = texture.getWidth() * scale;
        float drawHeight = texture.getHeight() * scale;
        batch.draw(texture, centerX - drawWidth / 2f, centerY - drawHeight / 2f, drawWidth, drawHeight);
    }

    private void drawToppings(Batch batch, float centerX, float centerY) {
        int toppingTypeIndex = 0;
        for (String toppingId : pizza.getToppings()) {
            IngredientProgress ingredient = ingredientManager.findById(toppingId);
            if (ingredient == null) {
                continue;
            }
            Texture texture = game.getAssets().getTexture(ingredient.toppingAsset);
            int count = Math.min(pepperoniPositions.size, 6);
            
            // Calculate a unique rotation angle offset for each topping type to scatter them beautifully
            float angleOffsetRad = (float) Math.toRadians(toppingTypeIndex * 60f);
            float cos = (float) Math.cos(angleOffsetRad);
            float sin = (float) Math.sin(angleOffsetRad);
            
            if ("mushroom".equals(toppingId) || "onion".equals(toppingId) || "fish".equals(toppingId)) {
                for (int index = 0; index < count; index++) {
                    Vector2 slot = pepperoniPositions.get(index);
                    
                    // Rotate the base coordinates
                    float rx = slot.x * cos - slot.y * sin;
                    float ry = slot.x * sin + slot.y * cos;
                    
                    float size = 48f;
                    
                    // Draw 1st slice
                    batch.draw(texture, centerX + rx - size / 2f, centerY + ry - size / 2f, size, size);
                    
                    // Draw 2nd slice (offset 1)
                    float off1X = rx * 0.5f - 25f;
                    float off1Y = ry * 0.5f + 25f;
                    batch.draw(texture, centerX + off1X - size / 2f, centerY + off1Y - size / 2f, size, size);
                    
                    // Draw 3rd slice (offset 2)
                    float off2X = rx * 0.6f + 25f;
                    float off2Y = ry * 0.6f - 25f;
                    batch.draw(texture, centerX + off2X - size / 2f, centerY + off2Y - size / 2f, size, size);
                }
            } else {
                for (int index = 0; index < count; index++) {
                    Vector2 slot = pepperoniPositions.get(index);
                    
                    // Rotate the base coordinates
                    float rx = slot.x * cos - slot.y * sin;
                    float ry = slot.x * sin + slot.y * cos;
                    
                    float size = 48f;
                    batch.draw(texture, centerX + rx - size / 2f, centerY + ry - size / 2f, size, size);
                }
            }
            toppingTypeIndex++;
        }
    }
}

