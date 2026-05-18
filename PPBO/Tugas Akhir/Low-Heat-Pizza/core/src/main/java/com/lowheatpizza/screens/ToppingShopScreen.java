package com.lowheatpizza.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lowheatpizza.LowHeatPizzaGame;
import com.lowheatpizza.progress.IngredientProgress;
import com.lowheatpizza.progress.IngredientStatus;
import com.lowheatpizza.progress.PlayerProgress;
import com.lowheatpizza.utils.AssetPaths;

public class ToppingShopScreen extends BaseScreen {
    public ToppingShopScreen(LowHeatPizzaGame game) {
        super(game, AssetPaths.SHOP_BG);
        buildUi();
    }

    @Override
    public void show() {
        super.show();
        game.getAudioManager().playLoop(AssetPaths.AUDIO_GAMEPLAY, 0.3f);
    }

    private void buildUi() {
        root.pad(18);
        Table layout = new Table();
        layout.defaults().pad(10);
        root.add(layout).expand().fill();

        layout.add(buildChip("TOPPING SHOP")).left().row();
        layout.add(buildLabel(
            "Money: " + game.getSession().getPlayer().getMoney() + " | Level: " + game.getSession().getPlayer().getLevel(),
            "subtle"
        )).left().row();

        Table listTable = createPanel("FFF3E1", 0.95f, 16);
        listTable.defaults().pad(8).left();
        for (IngredientProgress ingredient : game.getSession().getIngredientManager().getIngredients()) {
            addToppingRow(listTable, ingredient);
        }

        ScrollPane scrollPane = new ScrollPane(listTable, skin);
        layout.add(scrollPane).width(760).height(360).row();

        TextButton backButton = new TextButton("Back To Shop", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                game.getSaveManager().save(game.getSession());
                transitionTo(new ShopScreen(game));
            }
        });
        layout.add(backButton).width(220).left();
    }

    private void addToppingRow(Table listTable, IngredientProgress ingredient) {
        IngredientStatus status = ingredient.getStatus(game.getSession().getPlayer().getLevel());
        TextButton button = new TextButton(buildButtonText(status), skin, status == IngredientStatus.AVAILABLE_TO_BUY ? "default" : "secondary");
        button.setDisabled(status != IngredientStatus.AVAILABLE_TO_BUY || !game.getSession().getPlayer().canAfford(ingredient.price));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getAudioManager().playSound(AssetPaths.AUDIO_CLICK_BUTTON, 0.75f);
                if (game.getSession().getIngredientManager().buyIngredient(ingredient.id, new PlayerProgress(game.getSession().getPlayer()))) {
                    game.getSession().syncToppingsFromIngredients();
                    game.getSaveManager().save(game.getSession());
                    transitionTo(new ToppingShopScreen(game));
                }
            }
        });

        listTable.add(buildLabel(ingredient.displayName, "dark")).width(140);
        listTable.add(buildLabel("Req Lv " + ingredient.unlockLevel, "dark")).width(120);
        listTable.add(buildLabel("Price " + ingredient.price, "dark")).width(120);
        listTable.add(buildLabel("Status: " + status, "dark")).width(220);
        listTable.add(button).width(180).row();
    }

    private String buildButtonText(IngredientStatus status) {
        if (status == IngredientStatus.OWNED) {
            return "Owned";
        }
        if (status == IngredientStatus.LOCKED_BY_LEVEL) {
            return "Locked";
        }
        return "Buy";
    }

    private Table buildChip(String text) {
        Table chip = createPanel("7A3025", 0.82f, 10);
        chip.add(buildLabel(text, "subtle"));
        return chip;
    }
}

