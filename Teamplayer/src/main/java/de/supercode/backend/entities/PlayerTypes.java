package de.supercode.backend.entities;

public enum PlayerTypes {
    ROOKIE(1, 10),
    NORMAL(11, 30),
    VETERAN(31, 50),
    LEGENDARY(51, 80);

    private final int minRange;
    private final int maxRange;

    PlayerTypes(int minRange, int maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
    }


    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }
}