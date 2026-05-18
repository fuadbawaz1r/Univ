package com.lowheatpizza.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.lowheatpizza.LowHeatPizzaGame;
import com.lowheatpizza.kitchen.IngredientStation;
import com.lowheatpizza.kitchen.KitchenController;
import com.lowheatpizza.kitchen.KitchenLayout;
import com.lowheatpizza.kitchen.KitchenLayoutLoader;
import com.lowheatpizza.kitchen.KitchenRenderer;
import com.lowheatpizza.kitchen.KitchenSlot;
import com.lowheatpizza.kitchen.KitchenState;
import com.lowheatpizza.kitchen.PizzaModel;
import com.lowheatpizza.kitchen.PrepArea;
import com.lowheatpizza.models.EvaluationResult;
import com.lowheatpizza.progress.IngredientProgress;
import com.lowheatpizza.progress.IngredientStatus;
import com.lowheatpizza.progress.PlayerProgress;
import com.lowheatpizza.systems.EconomySystem;
import com.lowheatpizza.ui.BuyIngredientPopup;
import com.lowheatpizza.ui.HudPanel;
import com.lowheatpizza.ui.UnlockPopup;
import com.lowheatpizza.utils.AssetPaths;

public class KitchenScreen extends BaseScreen implements KitchenController.View {
    private final EconomySystem economySystem = new EconomySystem();
    private final KitchenLayout layout;
    private final KitchenRenderer renderer;
    private final KitchenState state = new KitchenState();
    private final KitchenController controller;
    private final ObjectMap<String, IngredientStation> stations = new ObjectMap<>();
    private Label warningLabel;
    private PrepArea mainPrepArea;
    private HudPanel moneyPanel;
    private float ovenTimer = 0f;
    private boolean ovenActive = false;

    public KitchenScreen(LowHeatPizzaGame game) {
        super(game, null);
        layout = new KitchenLayoutLoader().load();
        renderer = new KitchenRenderer(layout, layout.debug || Boolean.getBoolean("lowheat.kitchen.debug"));
        controller = new KitchenController(
            state,
            this,
            game.getSession().getIngredientManager(),
            new PlayerProgress(game.getSession().getPlayer())
        );
        buildScene();
    }

    @Override
    public void show() {
        super.show();
        game.getAudioManager().playLoop(AssetPaths.AUDIO_GAMEPLAY, 0.3f);
    }

    private void buildScene() {
        addBackground();
        addPrepAreas();
        addStations();
        addHud();
        addOrderTicket();
        addWarning();
        addDoneAndBackButtons();
    }

    private void addBackground() {
        Image background = buildImage(renderer.getBackgroundPath(), VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        background.setBounds(0f, 0f, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        worldStage.addActor(background);
    }

    private void addPrepAreas() {
        for (KitchenLayout.PrepAreaConfig config : layout.prepAreas) {
            KitchenSlot slot = layout.getSlot(config.slot);
            boolean afterOven = "secondary_prep".equals(config.id);
            PrepArea prepArea = new PrepArea(
                game,
                state.getPizza(),
                game.getSession().getIngredientManager(),
                layout.pepperoniPositions,
                afterOven,
                this::startOvenProcess
            );
            prepArea.setBounds(slot.x, slot.y, slot.width, slot.height);
            uiStage.addActor(prepArea);
            if ("main_prep".equals(config.id)) {
                mainPrepArea = prepArea;
            }
        }
    }

    private void addStations() {
        for (KitchenLayout.StationConfig config : layout.stations) {
            KitchenSlot slot = layout.getSlot(config.slot);
            Texture texture = config.asset == null || config.asset.isEmpty() ? null : game.getAssets().getTexture(config.asset);
            IngredientStation station = new IngredientStation(
                config.type,
                skin,
                renderer,
                slot,
                texture,
                game.getAssets().getTexture(AssetPaths.KITCHEN_PADLOCK),
                id -> {
                    if (ovenActive) {
                        showSmallWarning("Baking in progress!");
                        return;
                    }
                    if (state.getPizza().isBaked()) {
                        showSmallWarning("Pizza already baked!");
                        return;
                    }
                    int playerLevel = game.getSession().getPlayer().getLevel();
                    com.lowheatpizza.progress.IngredientProgress ingredient = game.getSession().getIngredientManager().findById(id);
                    if ("dough".equals(id) || "sauce".equals(id) || (ingredient != null && ingredient.getStatus(playerLevel) == com.lowheatpizza.progress.IngredientStatus.OWNED)) {
                        game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_TOPPING, 0.85f);
                    }
                    controller.onIngredientClicked(id);
                }
            );
            station.setBounds(slot.x, slot.y, slot.width, slot.height);
            uiStage.addActor(station);
            stations.put(config.type, station);
        }
        refreshIngredientStations();
    }

    private void addHud() {
        HudPanel dayPanel = new HudPanel(
            skin,
            "DAY " + game.getSession().getCurrentDay(),
            "12:00 PM",
            skin.getDrawable("icon_sun"),
            80f
        );
        dayPanel.setBounds(12f, 955f, 330f, 112f);
        uiStage.addActor(dayPanel);

        HudPanel levelPanel = new HudPanel(skin, "LEVEL", String.valueOf(game.getSession().getPlayer().getLevel()), null, 0f, true);
        levelPanel.setBounds(360f, 955f, 160f, 112f);
        uiStage.addActor(levelPanel);

        HudPanel expPanel = new HudPanel(skin, "EXP", game.getSession().getPlayer().getExp() + "/" + game.getSession().getPlayer().getExpToNextLevel(), null, 0f, true);
        expPanel.setBounds(540f, 955f, 210f, 112f);
        uiStage.addActor(expPanel);

        moneyPanel = new HudPanel(
            skin,
            null,
            "$" + game.getSession().getPlayer().getMoney(),
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
                game.getSaveManager().save(game.getSession());
                transitionTo(new MainMenuScreen(game));
            }
        });
        uiStage.addActor(pauseButton);
    }

    private void addOrderTicket() {
        Table ticket = new Table();
        ticket.setBackground(skin.getDrawable("speech_bubble_panel"));
        ticket.pad(10, 24, 10, 24);
        ticket.defaults().left().padRight(18);

        Label titleLabel = buildLabel("Order", "title");
        titleLabel.setFontScale(1.35f);
        ticket.add(titleLabel);

        Label nameLabel = buildLabel(game.getSession().getCurrentCustomer().getName(), "dark");
        nameLabel.setFontScale(1.35f);
        ticket.add(nameLabel);

        Label orderLabel = buildLabel(game.getSession().getCurrentOrder().getOrderText(), "dark");
        orderLabel.setFontScale(1.35f);
        orderLabel.setWrap(true);
        ticket.add(orderLabel).width(500);

        ticket.setBounds(720f, 850f, 760f, 100f);
        uiStage.addActor(ticket);
    }

    private void addWarning() {
        warningLabel = buildLabel("", "title");
        warningLabel.setColor(Color.valueOf("7A3025"));
        warningLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        warningLabel.setBounds(620f, 130f, 780f, 50f);
        uiStage.addActor(warningLabel);
    }

    private void addDoneAndBackButtons() {
        TextButton doneButton = buildKitchenActionButton("Done");
        doneButton.setBounds(1275f, 28f, 265f, 78f);
        doneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                if (ovenActive) {
                    showSmallWarning("Baking in progress!");
                    return;
                }
                finishOrder();
            }
        });
        uiStage.addActor(doneButton);

        TextButton backButton = buildKitchenActionButton("Back");
        backButton.setBounds(955f, 28f, 265f, 78f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                if (ovenActive) {
                    showSmallWarning("Baking in progress!");
                    return;
                }
                transitionTo(new ShopScreen(game));
            }
        });
        uiStage.addActor(backButton);

        TextButton resetButton = buildKitchenActionButton("Reset");
        resetButton.setBounds(635f, 28f, 265f, 78f);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                if (ovenActive) {
                    showSmallWarning("Baking in progress!");
                    return;
                }
                controller.resetPizza();
            }
        });
        uiStage.addActor(resetButton);
    }

    private TextButton buildKitchenActionButton(String text) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("buttonFont");
        style.fontColor = Color.valueOf("4A2419");
        style.downFontColor = Color.valueOf("4A2419");
        style.up = skin.getDrawable("button_response");
        style.down = skin.getDrawable("button_response");
        style.over = skin.getDrawable("button_response");
        TextButton button = new TextButton(text, style);
        button.getLabel().setFontScale(0.62f);
        return button;
    }

    private void finishOrder() {
        PizzaModel kitchenPizza = state.getPizza();
        if (!kitchenPizza.hasDough()) {
            showSmallWarning("Put dough first");
            return;
        }

        int previousLevel = game.getSession().getPlayer().getLevel();
        EvaluationResult result = game.getSession().getOrderEvaluator().evaluate(
            game.getSession().getCurrentOrder(),
            kitchenPizza.toGamePizza()
        );
        economySystem.grantMoney(game.getSession().getPlayer(), result.getMoneyReward());
        int levelsGained = game.getSession().getLevelSystem().grantExp(game.getSession().getPlayer(), result.getExpReward());
        game.getSession().syncToppingsFromIngredients();
        Array<IngredientProgress> newlyUnlocked = game.getSession().getIngredientManager().getNewlyUnlockedIngredients(
            previousLevel,
            game.getSession().getPlayer().getLevel()
        );
        game.getSession().setLastEvaluation(new EvaluationResult(
            result.getMoneyReward(),
            result.getExpReward(),
            result.isPerfectOrder(),
            levelsGained > 0
                ? result.getMessage() + " Level up! Sekarang level " + game.getSession().getPlayer().getLevel() + "."
                : result.getMessage()
        ));
        game.getSession().setCurrentCustomer(null);
        game.getSession().setCurrentOrder(null);
        game.getSession().advanceDay();
        game.getSaveManager().save(game.getSession());
        if (newlyUnlocked.size > 0) {
            showUnlockPopup(newlyUnlocked);
        } else {
            transitionTo(new ShopScreen(game));
        }
    }

    @Override
    public void refreshPizza() {
        warningLabel.setText(state.getWarningText());
    }

    @Override
    public void refreshIngredientStations() {
        int playerLevel = game.getSession().getPlayer().getLevel();
        for (ObjectMap.Entry<String, IngredientStation> entry : stations.entries()) {
            String ingredientId = entry.key;
            IngredientStation station = entry.value;
            if ("dough".equals(ingredientId) || "sauce".equals(ingredientId)) {
                station.setClickable(true);
                station.showLocked(false);
                station.showPrice(false);
                continue;
            }

            IngredientProgress ingredient = game.getSession().getIngredientManager().findById(ingredientId);
            if (ingredient == null) {
                station.setClickable(false);
                station.showLocked(true);
                station.showPrice(false);
                continue;
            }

            IngredientStatus status = ingredient.getStatus(playerLevel);
            if (status == IngredientStatus.LOCKED_BY_LEVEL) {
                station.setTexture(game.getAssets().getTexture(ingredient.lockedAsset));
                station.setClickable(true);
                station.showLocked(true);
                station.showPrice(false);
            } else if (status == IngredientStatus.AVAILABLE_TO_BUY) {
                station.setTexture(game.getAssets().getTexture(ingredient.stationAsset));
                station.setClickable(true);
                station.showLocked(false);
                station.showPrice(true);
                station.setPrice(ingredient.price);
            } else {
                station.setTexture(game.getAssets().getTexture(ingredient.stationAsset));
                station.setClickable(true);
                station.showLocked(false);
                station.showPrice(false);
            }
        }
    }

    @Override
    public void updateMoney() {
        if (moneyPanel != null) {
            moneyPanel.remove();
        }
        moneyPanel = new HudPanel(
            skin,
            null,
            "$" + game.getSession().getPlayer().getMoney(),
            skin.getDrawable("icon_money"),
            80f,
            false,
            true
        );
        moneyPanel.setBounds(1440f, 955f, 306f, 112f);
        uiStage.addActor(moneyPanel);
        game.getSession().syncToppingsFromIngredients();
        game.getSaveManager().save(game.getSession());
    }

    @Override
    public void showSmallWarning(String text) {
        state.setWarningText(text);
        warningLabel.setText(text);
    }

    @Override
    public void showMessage(String text) {
        state.setWarningText(text);
        warningLabel.setText(text);
    }

    @Override
    public void showBuyPopup(IngredientProgress ingredient) {
        BuyIngredientPopup popup = new BuyIngredientPopup(skin, ingredient, () -> controller.onBuyConfirmed(ingredient.id));
        popup.setBounds(720f, 410f, 480f, 230f);
        uiStage.addActor(popup);
    }

    @Override
    public void playPurchaseSuccessSound() {
        game.getAudioManager().playSound(AssetPaths.AUDIO_UNLOCK_TOPPING, 0.9f);
    }

    private void showUnlockPopup(Array<IngredientProgress> ingredients) {
        game.getAudioManager().playSound(AssetPaths.AUDIO_UNLOCK_TOPPING, 0.9f);
        UnlockPopup popup = new UnlockPopup(skin, ingredients, () -> transitionTo(new ShopScreen(game)));
        popup.setBounds(690f, 390f, 540f, 300f);
        uiStage.addActor(popup);
    }

    public void startOvenProcess() {
        if (!state.getPizza().hasDough()) {
            showSmallWarning("Put dough first");
            return;
        }
        if (state.getPizza().isBaked()) {
            showSmallWarning("Pizza already baked!");
            return;
        }
        if (ovenActive) return;
        ovenActive = true;
        ovenTimer = 3.0f;
        state.getPizza().setBaking(true);
        refreshPizza();
        game.getAudioManager().playSound(AssetPaths.AUDIO_OVEN_BAKE, 0.9f);
    }

    @Override
    public void render(float delta) {
        if (ovenActive) {
            ovenTimer -= delta;
            if (ovenTimer <= 0f) {
                ovenActive = false;
                state.getPizza().setBaking(false);
                state.getPizza().bake();
                showSmallWarning("Pizza is ready!");
                refreshPizza();
            } else {
                showSmallWarning("Baking in oven: " + (int) Math.ceil(ovenTimer) + "s");
            }
        }
        super.render(delta);
    }
}

