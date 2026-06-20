package team.lodestar.wayward_attributes.events;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.registry.WaywardAttributeTypes;
import team.lodestar.wayward_attributes.registry.WaywardPayloadTypes;
import team.lodestar.wayward_attributes.tweaks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("wayward_dump_attributes")
                        .requires(source -> source.hasPermission(0)) //is this necessary for client commands? idk
                        .executes(context -> {
                            context.getSource().sendSystemMessage(Component.literal("The following attributes do not have an icon: "));
                            context.getSource().sendSystemMessage(Component.literal("-----"));
                            for(Map.Entry<ResourceKey<Attribute>, Attribute> entry : BuiltInRegistries.ATTRIBUTE.entrySet()){
                                if(AttributeDisplayDataReloadListener.DISPLAY_DATA.stream().noneMatch(attributeDisplay -> attributeDisplay.attribute().is(entry.getKey()))){
                                    context.getSource().sendSystemMessage(Component.literal(entry.getKey().location().toString()));
                                }
                            }
                            context.getSource().sendSuccess(() -> Component.literal("-----"), true);
                            return 1;
                        })
        );
    }
}