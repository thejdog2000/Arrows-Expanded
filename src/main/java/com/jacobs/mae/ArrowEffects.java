package com.jacobs.mae;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public final class ArrowEffects {
    private static final float EXPLOSIVE_POWER = 1.75F;
    private static final float NAPALM_EXPLOSIVE_POWER = 1.0F;
    private static final float NAPALM_BURN_SECONDS = 2.0F;

    private ArrowEffects() {
    }

    public static void apply(AbstractArrow arrow, ArrowMode mode, Vec3 hitLocation, Entity hitEntity) {
        if (!(arrow.level() instanceof ServerLevel level) || mode == null || mode == ArrowMode.REGULAR) {
            return;
        }

        switch (mode) {
            case WEB_3X1 -> placeWebSheet(level, BlockPos.containing(hitLocation), arrow.getDeltaMovement());
            case LIGHTNING -> strikeLightning(level, hitLocation);
            case EXPLOSIVE -> level.explode(arrow, hitLocation.x, hitLocation.y, hitLocation.z, EXPLOSIVE_POWER, Level.ExplosionInteraction.BLOCK);
            case NAPALM_EXPLOSIVE -> napalm(level, arrow, hitLocation, hitEntity);
            case KNOCKBACK -> knockBack(arrow, hitEntity);
            case TELEPORT -> teleportOwner(arrow, level, hitLocation);
            case REGULAR -> {
            }
        }

        arrow.discard();
    }

    private static void placeWebSheet(ServerLevel level, BlockPos center, Vec3 arrowVelocity) {
        boolean arrowMostlyEastWest = Math.abs(arrowVelocity.x) >= Math.abs(arrowVelocity.z);

        for (int width = -1; width <= 1; width++) {
            for (int height = 0; height < 2; height++) {
                BlockPos pos = arrowMostlyEastWest
                        ? center.offset(0, height, width)
                        : center.offset(width, height, 0);

                if (level.getBlockState(pos).isAir()) {
                    level.setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
                }
            }
        }
    }

    private static void strikeLightning(ServerLevel level, Vec3 hitLocation) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
        if (lightning != null) {
            lightning.setPos(hitLocation);
            level.addFreshEntity(lightning);
        }
    }

    private static void napalm(ServerLevel level, AbstractArrow arrow, Vec3 hitLocation, Entity hitEntity) {
        level.explode(arrow, hitLocation.x, hitLocation.y, hitLocation.z, NAPALM_EXPLOSIVE_POWER, Level.ExplosionInteraction.NONE);

        BlockPos center = BlockPos.containing(hitLocation);
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, 0, -1), center.offset(1, 1, 1))) {
            if (level.getBlockState(pos).isAir()) {
                level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            }
        }

        if (hitEntity != null) {
            hitEntity.igniteForSeconds(NAPALM_BURN_SECONDS);
        }
    }

    private static void knockBack(AbstractArrow arrow, Entity hitEntity) {
        if (hitEntity == null) {
            return;
        }

        Vec3 push = hitEntity.position().subtract(arrow.position()).normalize().scale(2.0).add(0.0, 0.6, 0.0);
        hitEntity.push(push);
        hitEntity.hurtMarked = true;
    }

    private static void teleportOwner(AbstractArrow arrow, ServerLevel level, Vec3 hitLocation) {
        Entity owner = arrow.getOwner();
        if (owner instanceof ServerPlayer player) {
            player.teleportTo(level, hitLocation.x, hitLocation.y + 0.2, hitLocation.z, java.util.Set.<Relative>of(), player.getYRot(), player.getXRot(), true);
        } else if (owner instanceof LivingEntity livingEntity) {
            livingEntity.teleportTo(level, hitLocation.x, hitLocation.y + 0.2, hitLocation.z, java.util.Set.<Relative>of(), livingEntity.getYRot(), livingEntity.getXRot(), true);
        }
    }
}
