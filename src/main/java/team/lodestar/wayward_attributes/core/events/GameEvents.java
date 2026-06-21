package team.lodestar.wayward_attributes.core.events;

import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.core.data.listener.*;
import team.lodestar.wayward_attributes.core.data.listener.SyncReloadListenerDataPayload;
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
    public static void onDamage(LivingDamageEvent.Pre event) {
        MagicDamageSystem.processAttributes(event);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        MagicDamageSystem.triggerMagicDamage(event);
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