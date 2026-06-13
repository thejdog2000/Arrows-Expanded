package com.jacobs.mae;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

public final class ArrowEffectTracker {
    private static final Map<UUID, ArrowMode> ARROW_MODES = new ConcurrentHashMap<>();

    private ArrowEffectTracker() {
    }

    public static void tag(AbstractArrow arrow, ArrowMode mode) {
        if (mode != ArrowMode.REGULAR) {
            ARROW_MODES.put(arrow.getUUID(), mode);
        }
    }

    public static ArrowMode consume(AbstractArrow arrow) {
        return ARROW_MODES.remove(arrow.getUUID());
    }
}
