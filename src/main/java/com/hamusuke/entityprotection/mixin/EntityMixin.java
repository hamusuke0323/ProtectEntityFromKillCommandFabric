package com.hamusuke.entityprotection.mixin;

import com.hamusuke.entityprotection.util.EntityInvoker;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements EntityInvoker {
    private boolean killable = true;

    @Inject(method = "toTag", at = @At(value = "HEAD"))
    private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (!this.killable) {
            tag.putBoolean("Killable", false);
        }
    }

    @Inject(method = "fromTag", at = @At(value = "HEAD"))
    private void fromTag(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("Killable") && !tag.getBoolean("Killable")) {
            this.setKillable(false);
        }
    }

    public boolean isKillable() {
        return this.killable;
    }

    public void setKillable(boolean killable) {
        this.killable = killable;
    }
}
