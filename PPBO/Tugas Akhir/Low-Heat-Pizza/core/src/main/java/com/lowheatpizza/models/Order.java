package com.lowheatpizza.models;

import com.badlogic.gdx.utils.Array;

public class Order {
    private final String id;
    private final int availableFromLevel;
    private final String dialogue;
    private final String clarificationDialogue;
    private final Array<String> requiredToppings;
    private final int rewardMoney;
    private final int rewardExp;

    public Order(
        String id,
        int availableFromLevel,
        String dialogue,
        String clarificationDialogue,
        Array<String> requiredToppings,
        int rewardMoney,
        int rewardExp
    ) {
        this.id = id;
        this.availableFromLevel = availableFromLevel;
        this.dialogue = dialogue;
        this.clarificationDialogue = clarificationDialogue;
        this.requiredToppings = new Array<>(requiredToppings);
        this.rewardMoney = rewardMoney;
        this.rewardExp = rewardExp;
    }

    public String getId() {
        return id;
    }

    public int getAvailableFromLevel() {
        return availableFromLevel;
    }

    public String getDialogue() {
        return dialogue;
    }

    public String getClarificationDialogue() {
        return clarificationDialogue;
    }

    public boolean isNeedSauce() {
        return requiredToppings.contains("sauce", false);
    }

    public boolean isNeedCheese() {
        return requiredToppings.contains("cheese", false);
    }

    public Array<String> getRequiredToppings() {
        return requiredToppings;
    }

    public int getRewardMoney() {
        return rewardMoney;
    }

    public int getRewardExp() {
        return rewardExp;
    }

    public String getOrderText() {
        if (requiredToppings.size == 0) {
            return "plain pizza";
        }
        StringBuilder builder = new StringBuilder("pizza with ");
        for (int i = 0; i < requiredToppings.size; i++) {
            builder.append(formatToppingName(requiredToppings.get(i)));
            if (i < requiredToppings.size - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private String formatToppingName(String toppingId) {
        String[] words = toppingId.split("_");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                builder.append(word.substring(1));
            }
        }
        return builder.toString();
    }
}

