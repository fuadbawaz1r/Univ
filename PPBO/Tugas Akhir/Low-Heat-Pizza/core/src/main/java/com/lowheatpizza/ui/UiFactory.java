package com.lowheatpizza.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.lowheatpizza.managers.GameAssets;
import com.lowheatpizza.utils.AssetPaths;

public class UiFactory implements Disposable {
    private final Skin skin;
    private final Texture whiteTexture;
    private final BitmapFont defaultFont;
    private final BitmapFont dialogFont;
    private final BitmapFont buttonFont;
    private final BitmapFont hudLargeFont;
    private final BitmapFont hudSmallFont;
    private final Texture bubblePanelTexture;
    private final Texture bubbleTailTexture;
    private final Texture buttonTexture;
    private final Texture hudTexture;

    public UiFactory(GameAssets gameAssets) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whiteTexture = new Texture(pixmap);
        pixmap.dispose();

        skin = new Skin();
        TextureRegionDrawable white = new TextureRegionDrawable(whiteTexture);
        skin.add("white", white, Drawable.class);
        skin.add("white-region", new TextureRegion(whiteTexture), TextureRegion.class);

        // Load Fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetPaths.FONT_HANDWRITTEN));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        parameter.size = 32;
        defaultFont = generator.generateFont(parameter);
        skin.add("default-font", defaultFont);

        parameter.size = 56;
        dialogFont = generator.generateFont(parameter);
        skin.add("dialogFont", dialogFont);

        parameter.size = 46;
        buttonFont = generator.generateFont(parameter);
        skin.add("buttonFont", buttonFont);

        parameter.size = 42;
        hudLargeFont = generator.generateFont(parameter);
        skin.add("hudLargeFont", hudLargeFont);

        parameter.size = 24;
        hudSmallFont = generator.generateFont(parameter);
        skin.add("hudSmallFont", hudSmallFont);

        generator.dispose();

        bubblePanelTexture = createRoundedPanelTexture(72, 72, 18, Color.valueOf("FBF0DA"), Color.valueOf("6C3425"), 4);
        bubbleTailTexture = createSpeechBubbleTailTexture(50, 50, Color.valueOf("FBF0DA"), Color.valueOf("6C3425"), 4);
        buttonTexture = createRoundedPanelTexture(72, 72, 18, Color.valueOf("F8E8CB"), Color.valueOf("7A3B29"), 4);
        hudTexture = createRoundedPanelTexture(72, 72, 14, Color.valueOf("6F3124"), Color.valueOf("4B1F14"), 3);

        skin.add("speech_bubble_panel", new NinePatchDrawable(new NinePatch(bubblePanelTexture, 20, 20, 20, 20)), Drawable.class);
        skin.add("speech_bubble_tail", new TextureRegionDrawable(new TextureRegion(bubbleTailTexture)), Drawable.class);
        skin.add("button_response", new NinePatchDrawable(new NinePatch(buttonTexture, 20, 20, 20, 20)), Drawable.class);
        skin.add("hud_panel", new NinePatchDrawable(new NinePatch(hudTexture, 18, 18, 18, 18)), Drawable.class);

        Texture iconSunTex = gameAssets.getTexture(AssetPaths.UI_ICON_SUN);
        if (iconSunTex != null) {
            skin.add("icon_sun", new TextureRegionDrawable(new TextureRegion(iconSunTex, 321, 229, 104, 104)), Drawable.class);
        }
        Texture iconMoneyTex = gameAssets.getTexture(AssetPaths.UI_ICON_MONEY);
        if (iconMoneyTex != null) {
            skin.add("icon_money", new TextureRegionDrawable(new TextureRegion(iconMoneyTex, 360, 245, 87, 84)), Drawable.class);
        }

        Label.LabelStyle labelStyle = new Label.LabelStyle(defaultFont, Color.valueOf("FFF5E6"));
        skin.add("default", labelStyle);
        skin.add("title", new Label.LabelStyle(defaultFont, Color.valueOf("43211A")));
        skin.add("subtle", new Label.LabelStyle(defaultFont, Color.valueOf("FDE2CC")));
        skin.add("dark", new Label.LabelStyle(defaultFont, Color.valueOf("43211A")));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = defaultFont;
        textButtonStyle.up = white.tint(Color.valueOf("B54A36"));
        textButtonStyle.down = white.tint(Color.valueOf("8E3322"));
        textButtonStyle.over = white.tint(Color.valueOf("D96C4F"));
        textButtonStyle.disabled = white.tint(Color.valueOf("666666"));
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.disabledFontColor = Color.LIGHT_GRAY;
        skin.add("default", textButtonStyle);

        TextButton.TextButtonStyle secondaryButtonStyle = new TextButton.TextButtonStyle();
        secondaryButtonStyle.font = defaultFont;
        secondaryButtonStyle.up = white.tint(Color.valueOf("F6EBDD"));
        secondaryButtonStyle.down = white.tint(Color.valueOf("E0CFB7"));
        secondaryButtonStyle.over = white.tint(Color.valueOf("FFF5E8"));
        secondaryButtonStyle.disabled = white.tint(Color.valueOf("B8A79D"));
        secondaryButtonStyle.fontColor = Color.valueOf("43211A");
        secondaryButtonStyle.disabledFontColor = Color.valueOf("7A6B63");
        skin.add("secondary", secondaryButtonStyle);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = white.tint(Color.valueOf("3A241A"));
        scrollStyle.vScroll = white.tint(Color.valueOf("8E3322"));
        scrollStyle.vScrollKnob = white.tint(Color.valueOf("D96C4F"));
        skin.add("default", scrollStyle);
    }

    public Skin getSkin() {
        return skin;
    }

    private Texture createRoundedPanelTexture(int width, int height, int radius, Color fillColor, Color borderColor, int borderWidth) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean inOuter = insideRoundedRect(x, y, width, height, radius);
                if (!inOuter) {
                    continue;
                }

                boolean inInner = insideRoundedRect(x, y, width - (borderWidth * 2), height - (borderWidth * 2), Math.max(1, radius - borderWidth))
                    && x >= borderWidth
                    && x < width - borderWidth
                    && y >= borderWidth
                    && y < height - borderWidth;

                pixmap.setColor(inInner ? fillColor : borderColor);
                pixmap.drawPixel(x, y);
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createSpeechBubbleTailTexture(int width, int height, Color fillColor, Color borderColor, int borderWidth) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        int tailTipX = 2;
        int tailTipY = height - 2;
        int tailBaseTopX = width;
        int tailBaseTopY = 4;
        int tailBaseBotX = width;
        int tailBaseBotY = height - 14;

        pixmap.setColor(borderColor);
        pixmap.fillTriangle(tailTipX, tailTipY, tailBaseTopX, tailBaseTopY, tailBaseBotX, tailBaseBotY);
        pixmap.setColor(fillColor);
        pixmap.fillTriangle(
            tailTipX + borderWidth + 4,
            tailTipY - 2,
            tailBaseTopX,
            tailBaseTopY + borderWidth + 2,
            tailBaseBotX,
            tailBaseBotY - borderWidth - 2
        );

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void drawRoundedShape(Pixmap pixmap, int width, int height, int radius, Color fillColor, Color borderColor, int borderWidth) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean inOuter = insideRoundedRect(x, y, width, height, radius);
                if (!inOuter) {
                    continue;
                }

                boolean inInner = insideRoundedRect(x, y, width - (borderWidth * 2), height - (borderWidth * 2), Math.max(1, radius - borderWidth))
                    && x >= borderWidth
                    && x < width - borderWidth
                    && y >= borderWidth
                    && y < height - borderWidth;

                pixmap.setColor(inInner ? fillColor : borderColor);
                pixmap.drawPixel(x, y);
            }
        }
    }

    private boolean insideRoundedRect(int x, int y, int width, int height, int radius) {
        int clampedX = Math.max(radius, Math.min(x, width - radius - 1));
        int clampedY = Math.max(radius, Math.min(y, height - radius - 1));
        int dx = x - clampedX;
        int dy = y - clampedY;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public void dispose() {
        skin.dispose();
        whiteTexture.dispose();
        bubblePanelTexture.dispose();
        bubbleTailTexture.dispose();
        buttonTexture.dispose();
        hudTexture.dispose();
        defaultFont.dispose();
        dialogFont.dispose();
        buttonFont.dispose();
        hudLargeFont.dispose();
        hudSmallFont.dispose();
    }
}

