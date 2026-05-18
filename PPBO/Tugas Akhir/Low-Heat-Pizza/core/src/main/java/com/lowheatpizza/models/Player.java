package com.lowheatpizza.models;

public class Player {
    private int money;
    private int level;
    private int exp;
    private int expToNextLevel;

    public Player(int startingLevel, int startingExp, int expToNextLevel) {
        money = 0;
        level = startingLevel;
        exp = startingExp;
        this.expToNextLevel = expToNextLevel;
    }

    public void addExp(int amount) {
        exp += amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public boolean canAfford(int price) {
        return money >= price;
    }

    public boolean spendMoney(int amount) {
        if (!canAfford(amount)) {
            return false;
        }
        money -= amount;
        return true;
    }

    public void setProgress(int money, int level, int exp, int expToNextLevel) {
        this.money = money;
        this.level = level;
        this.exp = exp;
        this.expToNextLevel = expToNextLevel;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setExpToNextLevel(int expToNextLevel) {
        this.expToNextLevel = expToNextLevel;
    }

    public int getMoney() {
        return money;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }
}

