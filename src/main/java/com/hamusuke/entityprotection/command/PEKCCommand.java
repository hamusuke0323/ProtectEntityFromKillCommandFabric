package com.hamusuke.entityprotection.command;

import com.hamusuke.entityprotection.util.EntityInvoker;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

import java.util.Collection;
import java.util.stream.Collectors;

public class PEKCCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("pekc").requires((source) -> source.hasPermissionLevel(2));

        literalArgumentBuilder.then(CommandManager.literal("protect").then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> protect(context.getSource(), EntityArgumentType.getEntities(context, "targets").stream().filter(entity -> ((EntityInvoker) entity).isKillable()).collect(Collectors.toList())))));

        literalArgumentBuilder.then(CommandManager.literal("unprotect").then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> unprotect(context.getSource(), EntityArgumentType.getEntities(context, "targets").stream().filter(entity -> !((EntityInvoker) entity).isKillable()).collect(Collectors.toList())))));

        literalArgumentBuilder.then(CommandManager.literal("isProtected").then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> isProtected(context.getSource(), EntityArgumentType.getEntities(context, "targets")))));

        dispatcher.register(literalArgumentBuilder);
    }

    private static int protect(ServerCommandSource source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        targets.forEach(entity -> ((EntityInvoker) entity).setKillable(false));

        if (targets.size() == 1) {
            source.sendFeedback(new TranslatableText("command.pekc.protect.success.single", targets.iterator().next().getDisplayName()), true);
        } else if (targets.size() > 1) {
            source.sendFeedback(new TranslatableText("command.pekc.protect.success.multiple", targets.size()), true);
        } else {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create();
        }

        return targets.size();
    }

    private static int unprotect(ServerCommandSource source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        targets.forEach(entity -> ((EntityInvoker) entity).setKillable(true));

        if (targets.size() == 1) {
            source.sendFeedback(new TranslatableText("command.pekc.unprotect.success.single", targets.iterator().next().getDisplayName()), true);
        } else if (targets.size() > 1) {
            source.sendFeedback(new TranslatableText("command.pekc.unprotect.success.multiple", targets.size()), true);
        } else {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create();
        }

        return targets.size();
    }

    private static int isProtected(ServerCommandSource source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        targets.forEach(entity -> source.sendFeedback(new TranslatableText("command.pekc.isprotected." + !((EntityInvoker) entity).isKillable() + ".success", entity.getDisplayName(), new LiteralText(entity.getUuidAsString()).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click"))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, entity.getUuidAsString())))), false));

        if (targets.size() == 0) {
            throw EntityArgumentType.ENTITY_NOT_FOUND_EXCEPTION.create();
        }

        return targets.size();
    }
}
