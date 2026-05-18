package com.lowheatpizza.kitchen;

import com.lowheatpizza.progress.IngredientManager;
import com.lowheatpizza.progress.IngredientProgress;
import com.lowheatpizza.progress.IngredientStatus;
import com.lowheatpizza.progress.PlayerProgress;

public class KitchenController {
    public interface View {
        void refreshPizza();

        void refreshIngredientStations();

        void updateMoney();

        void showSmallWarning(String text);

        void showMessage(String text);

        void showBuyPopup(IngredientProgress ingredient);

        void playPurchaseSuccessSound();
    }

    private final KitchenState state;
    private final View view;
    private final IngredientManager ingredientManager;
    private final PlayerProgress playerProgress;

    public KitchenController(KitchenState state, View view, IngredientManager ingredientManager, PlayerProgress playerProgress) {
        this.state = state;
        this.view = view;
        this.ingredientManager = ingredientManager;
        this.playerProgress = playerProgress;
    }

    public void onIngredientClicked(String ingredientId) {
        if ("dough".equals(ingredientId) || "sauce".equals(ingredientId)) {
            addIngredientToPizza(ingredientId);
            return;
        }

        IngredientProgress ingredient = ingredientManager.findById(ingredientId);
        if (ingredient == null) {
            view.showSmallWarning("Ingredient not configured");
            return;
        }

        IngredientStatus status = ingredient.getStatus(playerProgress.getLevel());
        if (status == IngredientStatus.LOCKED_BY_LEVEL) {
            view.showSmallWarning("Reach level " + ingredient.unlockLevel + " to unlock");
            return;
        }
        if (status == IngredientStatus.AVAILABLE_TO_BUY) {
            view.showBuyPopup(ingredient);
            return;
        }

        addIngredientToPizza(ingredientId);
    }

    public void onBuyConfirmed(String ingredientId) {
        boolean success = ingredientManager.buyIngredient(ingredientId, playerProgress);
        if (!success) {
            view.showSmallWarning("Not enough money");
            return;
        }
        view.refreshIngredientStations();
        view.updateMoney();
        view.playPurchaseSuccessSound();
        view.showMessage("Topping purchased");
    }

    private void addIngredientToPizza(String ingredientId) {
        PizzaModel pizza = state.getPizza();
        ToppingType type = ToppingType.fromId(ingredientId);

        if (type == ToppingType.DOUGH) {
            if (!pizza.hasDough()) {
                pizza.addDough();
            }
            state.clearWarning();
            view.refreshPizza();
            return;
        }

        if (!pizza.hasDough()) {
            view.showSmallWarning("Put dough first");
            return;
        }

        switch (type) {
            case SAUCE:
                pizza.addSauce();
                break;
            case CHEESE:
                pizza.addCheese();
                break;
            case PEPPERONI:
                pizza.addTopping(ToppingType.PEPPERONI.getId());
                break;
            default:
                pizza.addTopping(ingredientId);
                break;
        }

        state.clearWarning();
        view.refreshPizza();
    }

    public void resetPizza() {
        state.getPizza().reset();
        state.clearWarning();
        view.refreshPizza();
    }
}

