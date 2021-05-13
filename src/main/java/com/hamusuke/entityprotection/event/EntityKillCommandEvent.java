package com.hamusuke.entityprotection.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;

public interface EntityKillCommandEvent {
    Event<EntityKillCommandEvent> EVENT = EventFactory.createArrayBacked(EntityKillCommandEvent.class, (listeners) -> (target) -> {
        for (EntityKillCommandEvent event : listeners) {
            if (!event.onKillCommand(target)) {
                return false;
            }
        }

        return true;
    });

    boolean onKillCommand(Entity target);
}
