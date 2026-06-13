package com.jacobs.mae;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ArrowCyclePayload() implements CustomPayload {
    public static final ArrowCyclePayload INSTANCE = new ArrowCyclePayload();
    public static final Id<ArrowCyclePayload> ID = new Id<>(
            Identifier.of(ArrowsExpandedMod.MOD_ID, "cycle_arrow"));
    public static final PacketCodec<RegistryByteBuf, ArrowCyclePayload> CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
