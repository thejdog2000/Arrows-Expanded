package com.jacobs.mae.mixin;

import com.jacobs.mae.ArrowEffectTracker;
import com.jacobs.mae.ArrowEffects;
import com.jacobs.mae.ArrowMode;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class AbstractArrowMixin {
    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void minecraftArrowsExpanded$applyEntityImpact(EntityHitResult hitResult, CallbackInfo ci) {
        PersistentProjectileEntity arrow = (PersistentProjectileEntity) (Object) this;
        ArrowMode mode = ArrowEffectTracker.consume(arrow);
        ArrowEffects.apply(arrow, mode, hitResult.getPos(), hitResult.getEntity());
    }

    @Inject(method = "onBlockHit", at = @At("TAIL"))
    private void minecraftArrowsExpanded$applyBlockImpact(BlockHitResult hitResult, CallbackInfo ci) {
        PersistentProjectileEntity arrow = (PersistentProjectileEntity) (Object) this;
        ArrowMode mode = ArrowEffectTracker.consume(arrow);
        ArrowEffects.apply(arrow, mode, hitResult.getPos(), null);
    }
}
