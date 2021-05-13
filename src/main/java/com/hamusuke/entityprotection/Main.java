package com.hamusuke.entityprotection;

import com.hamusuke.entityprotection.command.PEKCCommand;
import com.hamusuke.entityprotection.event.EntityKillCommandEvent;
import com.hamusuke.entityprotection.util.EntityInvoker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {
    public static final String MOD_ID = "entityprotection";

    public void onInitialize() {
        EntityKillCommandEvent.EVENT.register((target) -> ((EntityInvoker) target).isKillable());

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> PEKCCommand.register(dispatcher));
    }
}
