package com.lowheatpizza.models;

public class Customer {
    private final String id;
    private final String name;
    private final String spritePath;
    private final int availableFromLevel;
    private final Order order;
    private final String dialogText;
    private final String clarificationText;

    public Customer(String id, String name, String spritePath, int availableFromLevel, Order order, String dialogText, String clarificationText) {
        this.id = id;
        this.name = name;
        this.spritePath = spritePath;
        this.availableFromLevel = availableFromLevel;
        this.order = order;
        this.dialogText = dialogText;
        this.clarificationText = clarificationText;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public int getAvailableFromLevel() {
        return availableFromLevel;
    }

    public Order getOrder() {
        return order;
    }

    public String getDialogText() {
        return dialogText;
    }

    public String getClarificationText() {
        return clarificationText;
    }
}

