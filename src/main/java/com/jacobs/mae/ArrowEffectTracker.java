package com.jacobs.mae;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.projectile.PersistentProjectileEntity;

public final class ArrowEffectTracker {
    private static final Map<UUID, ArrowMode> ARROW_MODES = new ConcurrentHashMap<>();

    private ArrowEffectTracker() {
    }

    public static void tag(PersistentProjectileEntity arrow, ArrowMode mode) {
        if (mode != ArrowMode.REGULAR) {
            ARROW_MODES.put(arrow.getUuid(), mode);
        }
    }

    public static ArrowMode consume(PersistentProjectileEntity arrow) {
        return ARROW_MODES.remove(arrow.getUuid());
    }
}
