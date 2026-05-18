package com.lowheatpizza.models;

public class EvaluationResult {
    private final int moneyReward;
    private final int expReward;
    private final boolean perfectOrder;
    private final String message;

    public EvaluationResult(int moneyReward, int expReward, boolean perfectOrder, String message) {
        this.moneyReward = moneyReward;
        this.expReward = expReward;
        this.perfectOrder = perfectOrder;
        this.message = message;
    }

    public int getMoneyReward() {
        return moneyReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public boolean isPerfectOrder() {
        return perfectOrder;
    }

    public String getMessage() {
        return message;
    }
}

