package team.lodestar.wayward_attributes.events;

import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.network.SyncReloadListenerDataPayload;
import team.lodestar.wayward_attributes.tweaks.*;

@EventBusSubscriber(modid = WaywardAttributes.MODID)
public class GameEvents {

    @SubscribeEvent
    public static void syncListeners(OnDatapackSyncEvent event) {
        event.getRelevantPlayers().forEach(p -> {
            p.connection.send(new SyncReloadListenerDataPayload());
        });
    }

    @SubscribeEvent
    public static void registerListeners(AddReloadListenerEvent event) {
        AttributeDisplayDataReloadListener.register(event);
    }

    @SubscribeEvent
    public static void modifyEating(LivingEntityUseItemEvent.Start event) {
        HungerTweaks.modifyEatingSpeed(event);
    }

    @SubscribeEvent
    public static void modifyVisibility(LivingEvent.LivingVisibilityEvent event) {
        DetectionTweaks.modifyVisibility(event);
    }
}
