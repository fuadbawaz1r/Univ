package com.lowheatpizza.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class HudPanel extends Table {

    public HudPanel(Skin skin, String smallText, String largeText) {
        this(skin, smallText, largeText, null, 0f, false, false);
    }

    public HudPanel(Skin skin, String smallText, String largeText, Drawable iconDrawable, float iconSize) {
        this(skin, smallText, largeText, iconDrawable, iconSize, false, false);
    }

    public HudPanel(Skin skin, String smallText, String largeText, Drawable iconDrawable, float iconSize, boolean centered) {
        this(skin, smallText, largeText, iconDrawable, iconSize, centered, false);
    }

    public HudPanel(Skin skin, String smallText, String largeText, Drawable iconDrawable, float iconSize, boolean centered, boolean useInnerBox) {
        super();
        setBackground(skin.getDrawable("hud_panel"));
        if (useInnerBox) {
            pad(22, 28, 22, 28);
        } else if (centered) {
            pad(6, 16, 6, 16);
        } else {
            pad(6, 20, 6, 20);
        }

        String textColor = useInnerBox ? "4A2419" : "FFF5E6";
        Label.LabelStyle smallStyle = new Label.LabelStyle(skin.getFont("hudSmallFont"), Color.valueOf(textColor));
        Label.LabelStyle largeStyle = new Label.LabelStyle(skin.getFont("hudLargeFont"), Color.valueOf(textColor));

        if (useInnerBox) {
            Stack stack = new Stack();
            
            Table textGroup = new Table();
            textGroup.setBackground(skin.getDrawable("speech_bubble_panel"));
            textGroup.pad(4, 16, 4, 16);
            if (iconDrawable != null) {
                textGroup.padLeft(iconSize * 0.6f); // Make room for overlapping icon
            }
            textGroup.defaults().center();
            
            if (smallText != null && !smallText.isEmpty()) {
                Label smallLabel = new Label(smallText, smallStyle);
                smallLabel.setAlignment(centered ? Align.center : Align.left);
                textGroup.add(smallLabel).center().expandX().padBottom(-2f).row();
            }

            if (largeText != null && !largeText.isEmpty()) {
                Label largeLabel = new Label(largeText, largeStyle);
                largeLabel.setAlignment(centered ? Align.center : Align.left);
                textGroup.add(largeLabel).center().expandX();
            }
            
            stack.add(textGroup);
            
            if (iconDrawable != null) {
                Table iconTable = new Table();
                Image icon = new Image(iconDrawable);
                iconTable.add(icon).size(iconSize).left().padLeft(-16f);
                iconTable.left();
                stack.add(iconTable);
            }
            
            add(stack).expand().fill();
        } else {
            if (iconDrawable != null) {
                Image icon = new Image(iconDrawable);
                add(icon).size(iconSize).padRight(4).center();
            }

            Table textGroup = new Table();
            textGroup.defaults().center();
            if (smallText != null && !smallText.isEmpty()) {
                Label smallLabel = new Label(smallText, smallStyle);
                smallLabel.setAlignment(centered ? Align.center : Align.left);
                textGroup.add(smallLabel).center().expandX().padBottom(-2f).row();
            }

            if (largeText != null && !largeText.isEmpty()) {
                Label largeLabel = new Label(largeText, largeStyle);
                largeLabel.setAlignment(centered ? Align.center : Align.left);
                textGroup.add(largeLabel).center().expandX();
            }

            add(textGroup).center().expandX().fillX();
        }
    }
}

