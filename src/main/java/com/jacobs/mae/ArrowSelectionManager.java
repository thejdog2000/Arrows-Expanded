package com.jacobs.mae;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class ArrowSelectionManager {
    private static final Map<UUID, PlayerArrowState> PLAYER_STATES = new ConcurrentHashMap<>();

    private ArrowSelectionManager() {
    }

    public static ArrowMode getSelected(ServerPlayerEntity player) {
        return stateFor(player).selectedMode;
    }

    public static ArrowMode inspectOrCycle(ServerPlayerEntity player) {
        PlayerArrowState state = stateFor(player);

        if (state.hasInspected) {
            state.selectedMode = state.selectedMode.next();
        } else {
            state.hasInspected = true;
        }

        announce(player, state.selectedMode);
        return state.selectedMode;
    }

    public static void announce(ServerPlayerEntity player, ArrowMode mode) {
        Text message = Text.literal("Arrow selected: " + mode.displayName());
        player.sendMessage(message, true);
        ArrowsExpandedMod.LOGGER.info("{} selected arrow mode {}", player.getName().getString(), mode.displayName());
    }

    private static PlayerArrowState stateFor(ServerPlayerEntity player) {
        return PLAYER_STATES.computeIfAbsent(player.getUuid(), id -> new PlayerArrowState());
    }

    private static final class PlayerArrowState {
        private ArrowMode selectedMode = ArrowMode.REGULAR;
        private boolean hasInspected;
    }
}
