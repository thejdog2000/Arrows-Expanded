package com.jacobs.mae;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ArrowCyclePayload() implements CustomPacketPayload {
    public static final ArrowCyclePayload INSTANCE = new ArrowCyclePayload();
    public static final Type<ArrowCyclePayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(ArrowsExpandedMod.MOD_ID, "cycle_arrow"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ArrowCyclePayload> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
