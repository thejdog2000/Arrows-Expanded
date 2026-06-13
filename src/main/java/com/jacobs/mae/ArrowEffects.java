package com.jacobs.mae;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ArrowEffects {
    private ArrowEffects() {
    }

    public static void apply(PersistentProjectileEntity arrow, ArrowMode mode, Vec3d hitLocation, Entity hitEntity) {
        if (!(arrow.getEntityWorld() instanceof ServerWorld level) || mode == null || mode == ArrowMode.REGULAR) {
            return;
        }

        switch (mode) {
            case WEB_3X1 -> placeWebColumn(level, BlockPos.ofFloored(hitLocation));
            case LIGHTNING -> strikeLightning(level, hitLocation);
            case EXPLOSIVE -> level.createExplosion(arrow, hitLocation.x, hitLocation.y, hitLocation.z, 3.0F, World.ExplosionSourceType.BLOCK);
            case NAPALM_EXPLOSIVE -> napalm(level, arrow, hitLocation, hitEntity);
            case KNOCKBACK -> knockBack(arrow, hitEntity);
            case TELEPORT -> teleportOwner(arrow, level, hitLocation);
            case REGULAR -> {
            }
        }

        arrow.discard();
    }

    private static void placeWebColumn(ServerWorld level, BlockPos center) {
        for (int y = 0; y < 3; y++) {
            BlockPos pos = center.up(y);
            if (level.getBlockState(pos).isAir()) {
                level.setBlockState(pos, Blocks.COBWEB.getDefaultState());
            }
        }
    }

    private static void strikeLightning(ServerWorld level, Vec3d hitLocation) {
        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(level, SpawnReason.TRIGGERED);
        if (lightning != null) {
            lightning.refreshPositionAfterTeleport(hitLocation);
            level.spawnEntity(lightning);
        }
    }

    private static void napalm(ServerWorld level, PersistentProjectileEntity arrow, Vec3d hitLocation, Entity hitEntity) {
        level.createExplosion(arrow, hitLocation.x, hitLocation.y, hitLocation.z, 2.75F, World.ExplosionSourceType.NONE);

        BlockPos center = BlockPos.ofFloored(hitLocation);
        for (BlockPos pos : BlockPos.iterate(center.add(-1, 0, -1), center.add(1, 1, 1))) {
            if (level.getBlockState(pos).isAir()) {
                level.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }

        if (hitEntity != null) {
            hitEntity.setOnFireFor(6.0F);
        }
    }

    private static void knockBack(PersistentProjectileEntity arrow, Entity hitEntity) {
        if (hitEntity == null) {
            return;
        }

        Vec3d targetPos = new Vec3d(hitEntity.getX(), hitEntity.getY(), hitEntity.getZ());
        Vec3d arrowPos = new Vec3d(arrow.getX(), arrow.getY(), arrow.getZ());
        Vec3d push = targetPos.subtract(arrowPos).normalize().multiply(2.0).add(0.0, 0.6, 0.0);
        hitEntity.addVelocity(push);
        hitEntity.velocityDirty = true;
    }

    private static void teleportOwner(PersistentProjectileEntity arrow, ServerWorld level, Vec3d hitLocation) {
        Entity owner = arrow.getOwner();
        if (owner instanceof ServerPlayerEntity player) {
            player.teleport(level, hitLocation.x, hitLocation.y + 0.2, hitLocation.z, java.util.Set.<PositionFlag>of(), player.getYaw(), player.getPitch(), true);
        } else if (owner instanceof LivingEntity livingEntity) {
            livingEntity.teleport(level, hitLocation.x, hitLocation.y + 0.2, hitLocation.z, java.util.Set.<PositionFlag>of(), livingEntity.getYaw(), livingEntity.getPitch(), true);
        }
    }
}
