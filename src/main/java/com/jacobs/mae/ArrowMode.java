package com.jacobs.mae;

public enum ArrowMode {
    REGULAR("Regular"),
    WEB_3X1("Web 3x1"),
    LIGHTNING("Lightning"),
    EXPLOSIVE("Explosive"),
    NAPALM_EXPLOSIVE("Napalm Explosive"),
    KNOCKBACK("Knock Back"),
    TELEPORT("Teleport");

    private static final ArrowMode[] VALUES = values();

    private final String displayName;

    ArrowMode(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public ArrowMode next() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }
}
