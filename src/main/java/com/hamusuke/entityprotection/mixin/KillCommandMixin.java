package com.hamusuke.entityprotection.mixin;

import com.hamusuke.entityprotection.event.EntityKillCommandEvent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.KillCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(KillCommand.class)
public class KillCommandMixin {
    @Inject(at = @At(value = "HEAD"), method = "execute", cancellable = true)
    private static void execute(ServerCommandSource source, Collection<? extends Entity> targets, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        int i = 0;
        Entity e = null;

        for (Entity entity : targets) {
            if (EntityKillCommandEvent.EVENT.invoker().onKillCommand(entity)) {
                entity.kill();
                i++;
                e = entity;
            }
        }

        if (i == 1) {
            source.sendFeedback(new TranslatableText("commands.kill.success.single", e.getDisplayName()), true);
        } else if (i > 1) {
            source.sendFeedback(new TranslatableText("commands.kill.success.multiple", i), true);
        } else {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create();
        }

        cir.setReturnValue(i);
    }
}
