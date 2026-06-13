package com.jacobs.mae;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class ArrowSelectionManager {
    private static final Map<UUID, PlayerArrowState> PLAYER_STATES = new ConcurrentHashMap<>();

    private ArrowSelectionManager() {
    }

    public static ArrowMode getSelected(ServerPlayer player) {
        return stateFor(player).selectedMode;
    }

    public static ArrowMode inspectOrCycle(ServerPlayer player) {
        PlayerArrowState state = stateFor(player);

        if (state.hasInspected) {
            state.selectedMode = state.selectedMode.next();
        } else {
            state.hasInspected = true;
        }

        announce(player, state.selectedMode);
        return state.selectedMode;
    }

    public static void announce(ServerPlayer player, ArrowMode mode) {
        Component message = Component.literal("Arrow selected: " + mode.displayName());
        player.sendSystemMessage(message, true);
        ArrowsExpandedMod.LOGGER.info("{} selected arrow mode {}", player.getGameProfile().name(), mode.displayName());
    }

    private static PlayerArrowState stateFor(ServerPlayer player) {
        return PLAYER_STATES.computeIfAbsent(player.getUUID(), id -> new PlayerArrowState());
    }

    private static final class PlayerArrowState {
        private ArrowMode selectedMode = ArrowMode.REGULAR;
        private boolean hasInspected;
    }
}
