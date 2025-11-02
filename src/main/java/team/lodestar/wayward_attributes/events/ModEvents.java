package team.lodestar.wayward_attributes.events;

import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.event.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.tweaks.*;

@EventBusSubscriber()
public class ModEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        RangedAttributeTweaks.modifyComponents(event);
        SweepAttackTweaks.modifyComponents(event);
    }
}
