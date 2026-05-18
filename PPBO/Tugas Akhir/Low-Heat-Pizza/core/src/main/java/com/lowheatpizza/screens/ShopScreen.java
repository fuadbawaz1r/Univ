package com.lowheatpizza.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lowheatpizza.LowHeatPizzaGame;
import com.lowheatpizza.managers.GameSession;
import com.lowheatpizza.models.Customer;
import com.lowheatpizza.utils.AssetPaths;
import com.lowheatpizza.ui.DialogBubble;
import com.lowheatpizza.ui.HudPanel;

public class ShopScreen extends BaseScreen {
    private final GameSession session;
    private DialogBubble dialogBubble;

    public ShopScreen(LowHeatPizzaGame game) {
        super(game, null);
        this.session = game.getSession();
        ensureCustomer();
        buildScene();
    }

    @Override
    public void show() {
        super.show();
        game.getAudioManager().playLoop(AssetPaths.AUDIO_GAMEPLAY, 0.3f);
        game.getAudioManager().playSound(AssetPaths.AUDIO_CUSTOMER_ARRIVE, 0.75f);
    }

    private void ensureCustomer() {
        if (session.getCurrentCustomer() == null) {
            Customer customer = session.getCustomerManager().nextCustomer(session.getPlayer(), session.getToppingManager());
            session.setCurrentCustomer(customer);
            session.setCurrentOrder(customer.getOrder());
        }
    }

    private void buildScene() {
        addBackground();
        addCustomer();
        addForegroundOverlay();

        addSpeechBubble();
        addResponseButtons();
        addHud();
    }

    private void addBackground() {
        Image background = buildImage(AssetPaths.SHOP_BG, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        background.setBounds(0f, 0f, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        worldStage.addActor(background);
    }

    private void addCustomer() {
        Customer customer = session.getCurrentCustomer();
        Image customerActor = buildImage(customer.getSpritePath(), VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        customerActor.setBounds(0f, 0f, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        worldStage.addActor(customerActor);
    }

    private void addForegroundOverlay() {
        Image overlay = buildImage(AssetPaths.SHOP_FOREGROUND, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        overlay.setBounds(0f, 0f, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        overlay.setTouchable(Touchable.disabled);
        worldStage.addActor(overlay);
    }

    private void addSpeechBubble() {
        dialogBubble = new DialogBubble(skin);
        dialogBubble.setBounds(660f, 650f, 1180f, 246f);
        dialogBubble.setText(session.getCurrentCustomer().getDialogText());
        uiStage.addActor(dialogBubble);
    }

    private void addResponseButtons() {
        Customer customer = session.getCurrentCustomer();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("buttonFont");
        style.fontColor = Color.valueOf("4A2419");
        style.up = skin.getDrawable("button_response");

        TextButton okayButton = new TextButton("Okay", style);
        okayButton.setBounds(830f, 465f, 390f, 120f);
        okayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                transitionTo(new KitchenScreen(game));
            }
        });
        uiStage.addActor(okayButton);

        TextButton whatButton = new TextButton("What?", style);
        whatButton.setBounds(1280f, 465f, 390f, 120f);
        whatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                dialogBubble.setText(customer.getClarificationText());
            }
        });
        uiStage.addActor(whatButton);
    }

    private void addHud() {
        HudPanel dayPanel = new HudPanel(
            skin,
            "DAY " + session.getCurrentDay(),
            "12:00 PM",
            skin.getDrawable("icon_sun"),
            80f
        );
        dayPanel.setBounds(12f, 955f, 330f, 112f);
        uiStage.addActor(dayPanel);

        HudPanel levelPanel = new HudPanel(skin, "LEVEL", String.valueOf(session.getPlayer().getLevel()), null, 0f, true);
        levelPanel.setBounds(360f, 955f, 160f, 112f);
        uiStage.addActor(levelPanel);

        HudPanel expPanel = new HudPanel(skin, "EXP", session.getPlayer().getExp() + "/" + session.getPlayer().getExpToNextLevel(), null, 0f, true);
        expPanel.setBounds(540f, 955f, 210f, 112f);
        uiStage.addActor(expPanel);

        HudPanel moneyPanel = new HudPanel(
            skin,
            null,
            "$" + session.getPlayer().getMoney(),
            skin.getDrawable("icon_money"),
            80f,
            false,
            true
        );
        moneyPanel.setBounds(1440f, 955f, 306f, 112f);
        uiStage.addActor(moneyPanel);

        TextButton.TextButtonStyle pauseStyle = new TextButton.TextButtonStyle();
        pauseStyle.font = skin.getFont("buttonFont");
        pauseStyle.fontColor = Color.valueOf("FFF5E6");
        pauseStyle.up = skin.getDrawable("hud_panel");
        pauseStyle.down = skin.getDrawable("hud_panel");

        TextButton pauseButton = new TextButton("II", pauseStyle);
        pauseButton.getLabel().setFontScale(1.05f);
        pauseButton.setBounds(1770f, 955f, 138f, 112f);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                game.getSaveManager().save(session);
                transitionTo(new MainMenuScreen(game));
            }
        });
        uiStage.addActor(pauseButton);
    }
}

