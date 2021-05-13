package com.hamusuke.entityprotection.command;

import com.hamusuke.entityprotection.util.EntityInvoker;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

import java.util.Collection;

public class PEKCCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("pekc").requires((source) -> source.hasPermissionLevel(2));

        literalArgumentBuilder.then(CommandManager.literal("protect").then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> protect(context.getSource(), EntityArgumentType.getEntities(context, "targets")))));

        literalArgumentBuilder.then(CommandManager.literal("unprotect").then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> unprotect(context.getSource(), EntityArgumentType.getEntities(context, "targets")))));

        literalArgumentBuilder.then(CommandManager.literal("isProtected").then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> isProtected(context.getSource(), EntityArgumentType.getEntities(context, "targets")))));

        dispatcher.register(literalArgumentBuilder);
    }

    private static int protect(ServerCommandSource source, Collection<? extends Entity> targets) {
        int i = 0;
        Entity e = null;

        for (Entity entity : targets) {
            EntityInvoker entityInvoker = (EntityInvoker) entity;
            if (entityInvoker.isKillable()) {
                entityInvoker.setKillable(false);
                i++;
                e = entity;
            }
        }

        if (i == 1) {
            source.sendFeedback(new TranslatableText("command.pekc.protect.success.single", e.getDisplayName()), true);
        } else {
            source.sendFeedback(new TranslatableText("command.pekc.protect.success.multiple", i), true);
        }

        return i;
    }

    private static int unprotect(ServerCommandSource source, Collection<? extends Entity> targets) {
        int i = 0;
        Entity e = null;

        for (Entity entity : targets) {
            EntityInvoker entityInvoker = (EntityInvoker) entity;
            if (!entityInvoker.isKillable()) {
                entityInvoker.setKillable(true);
                i++;
                e = entity;
            }
        }

        if (i == 1) {
            source.sendFeedback(new TranslatableText("command.pekc.unprotect.success.single", e.getDisplayName()), true);
        } else {
            source.sendFeedback(new TranslatableText("command.pekc.unprotect.success.multiple", i), true);
        }

        return i;
    }

    private static int isProtected(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            EntityInvoker entityInvoker = (EntityInvoker) entity;
            source.sendFeedback(new TranslatableText("command.pekc.isprotected." + !entityInvoker.isKillable() + ".success", entity.getDisplayName(), new LiteralText(entity.getUuidAsString()).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click"))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, entity.getUuidAsString())))), false);
        }

        return targets.size();
    }
}
