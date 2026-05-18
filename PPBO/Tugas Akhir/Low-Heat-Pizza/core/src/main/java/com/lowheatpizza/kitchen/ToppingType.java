package com.lowheatpizza.kitchen;

public enum ToppingType {
    DOUGH("dough"),
    SAUCE("sauce"),
    CHEESE("cheese"),
    PEPPERONI("pepperoni"),
    OTHER("other");

    private final String id;

    ToppingType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static ToppingType fromId(String id) {
        for (ToppingType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return OTHER;
    }
}

