package com.example.memory.shop;

public enum Rarity {
    COMMON(0),
    UNCOMMON(1),
    RARE(2),
    EPIC(3),
    LEGENDARY(4),
    UNIQUE(5);

    private final int order;

    /**
     * Constructor for the Rarity enum
     * @param order the order of the rarity
     */
    Rarity(int order) {
        this.order = order;
    }

    /**
     * Gets the order of the rarity
     * @return the order of the rarity
     */
    public int getOrder() {
        return order;
    }

    /**
     * Converts a string to a Rarity object
     * @param rarity the string to convert
     * @return the Rarity object
     */
    public static Rarity fromString(String rarity) {
        switch (rarity) {
            case "Common":
                return COMMON;
            case "Uncommon":
                return UNCOMMON;
            case "Rare":
                return RARE;
            case "Epic":
                return EPIC;
            case "Legendary":
                return LEGENDARY;
            case "Unique":
                return UNIQUE;
            default:
                return null;
        }
    }
}