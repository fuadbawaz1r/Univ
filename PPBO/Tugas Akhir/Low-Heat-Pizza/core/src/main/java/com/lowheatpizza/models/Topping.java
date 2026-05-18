package com.lowheatpizza.models;

public class Topping {
    private final String id;
    private final String name;
    private final int price;
    private final int requiredLevel;
    private final ToppingStatus initialStatus;
    private final String assetPath;
    private ToppingStatus status;

    public Topping(String id, String name, int price, int requiredLevel, ToppingStatus status, String assetPath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.initialStatus = status;
        this.status = status;
        this.assetPath = assetPath;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public ToppingStatus getStatus() {
        return status;
    }

    public void setStatus(ToppingStatus status) {
        this.status = status;
    }

    public ToppingStatus getInitialStatus() {
        return initialStatus;
    }

    public String getAssetPath() {
        return assetPath;
    }
}

