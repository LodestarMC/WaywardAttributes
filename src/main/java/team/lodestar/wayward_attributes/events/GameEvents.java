package team.lodestar.wayward_attributes.events;

import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.event.*;
import team.lodestar.wayward_attributes.*;

@EventBusSubscriber()
public class GameEvents {

    @SubscribeEvent
    public static void registerListeners(AddReloadListenerEvent event) {
        AttributeDisplayDataReloadListener.register(event);
    }
}
