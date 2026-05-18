package com.lowheatpizza.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;

public class DialogBubble extends WidgetGroup {
    private static final float TAIL_SLOT_WIDTH = 40f;
    private static final float TAIL_WIDTH = 50f;
    private static final float TAIL_HEIGHT = 50f;

    private final Table bubblePanel;
    private final Image tail;
    private final Label textLabel;

    public DialogBubble(Skin skin) {
        bubblePanel = new Table();
        bubblePanel.setBackground(skin.getDrawable("speech_bubble_panel"));
        bubblePanel.pad(34, 70, 34, 52);

        Label.LabelStyle style = new Label.LabelStyle(skin.getFont("dialogFont"), Color.valueOf("4A2419"));
        textLabel = new Label("", style);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.topLeft);
        textLabel.setFontScale(0.86f);
        bubblePanel.add(textLabel).expand().fill().top().left();

        tail = new Image(skin.getDrawable("speech_bubble_tail"));

        addActor(bubblePanel);
        addActor(tail);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        bubblePanel.setBounds(TAIL_SLOT_WIDTH, 0f, getWidth() - TAIL_SLOT_WIDTH, getHeight());
        tail.setBounds(TAIL_SLOT_WIDTH - TAIL_WIDTH + 4, 30f, TAIL_WIDTH, TAIL_HEIGHT);
    }

    public void setText(String text) {
        textLabel.setText(text);
    }
}

