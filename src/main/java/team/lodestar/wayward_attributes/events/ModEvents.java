package team.lodestar.wayward_attributes.events;

import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.registry.WaywardAttributeTypes;
import team.lodestar.wayward_attributes.registry.WaywardPayloadTypes;
import team.lodestar.wayward_attributes.tweaks.*;

@EventBusSubscriber(modid = WaywardAttributes.MODID)
public class ModEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        RangedAttributeTweaks.modifyComponents(event);
        SweepAttackTweaks.modifyComponents(event);
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        WaywardAttributeTypes.modifyEntityAttributes(event);
    }

    @SubscribeEvent
    public static void registerNetworkStuff(RegisterPayloadHandlersEvent event) {
        WaywardPayloadTypes.registerNetworkStuff(event);
    }
}