package com.lowheatpizza.kitchen;

public class KitchenState {
    private final PizzaModel pizza = new PizzaModel();
    private String warningText = "";

    public PizzaModel getPizza() {
        return pizza;
    }

    public String getWarningText() {
        return warningText;
    }

    public void setWarningText(String warningText) {
        this.warningText = warningText;
    }

    public void clearWarning() {
        warningText = "";
    }
}

