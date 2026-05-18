package com.lowheatpizza.managers;

import com.lowheatpizza.models.Customer;
import com.lowheatpizza.models.EvaluationResult;
import com.lowheatpizza.models.Order;
import com.lowheatpizza.models.Player;
import com.lowheatpizza.progress.IngredientManager;
import com.lowheatpizza.systems.OrderEvaluator;

public class GameSession {
    private GameConfigManager configManager;
    private Player player;
    private IngredientManager ingredientManager;
    private ToppingManager toppingManager;
    private CustomerManager customerManager;
    private OrderEvaluator orderEvaluator;
    private com.lowheatpizza.systems.LevelSystem levelSystem;
    private Order currentOrder;
    private Customer currentCustomer;
    private EvaluationResult lastEvaluation;
    private int currentDay;

    public void resetForNewGame() {
        configManager = new GameConfigManager();
        player = new Player(
            configManager.getStartingLevel(),
            configManager.getStartingExp(),
            configManager.getExpToNextLevel(configManager.getStartingLevel())
        );
        ingredientManager = new IngredientManager();
        toppingManager = new ToppingManager(configManager);
        toppingManager.resetStatuses();
        toppingManager.refreshUnlocks(player.getLevel());
        syncToppingsFromIngredients();
        customerManager = new CustomerManager(configManager);
        orderEvaluator = new OrderEvaluator();
        levelSystem = new com.lowheatpizza.systems.LevelSystem(configManager);
        currentOrder = null;
        currentCustomer = null;
        lastEvaluation = null;
        currentDay = 1;
    }

    public GameConfigManager getConfigManager() {
        return configManager;
    }

    public Player getPlayer() {
        return player;
    }

    public ToppingManager getToppingManager() {
        return toppingManager;
    }

    public IngredientManager getIngredientManager() {
        return ingredientManager;
    }

    public void syncToppingsFromIngredients() {
        toppingManager.syncFromIngredients(ingredientManager, player.getLevel());
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public OrderEvaluator getOrderEvaluator() {
        return orderEvaluator;
    }

    public com.lowheatpizza.systems.LevelSystem getLevelSystem() {
        return levelSystem;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public EvaluationResult getLastEvaluation() {
        return lastEvaluation;
    }

    public void setLastEvaluation(EvaluationResult lastEvaluation) {
        this.lastEvaluation = lastEvaluation;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void advanceDay() {
        currentDay++;
    }
}

