package team.lodestar.wayward_attributes.tweaks;

import net.minecraft.core.Holder;
import net.minecraft.core.component.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.wayward_attributes.registry.WaywardAttributeTypes;

public class HungerTweaks {

    public static float modifyJumpingHungerDrain(Player player) {
        return modifyHungerDrain(player, WaywardAttributeTypes.JUMPING_EXHAUSTION);
    }

    public static float modifySwimmingHungerDrain(Player player) {
        return modifyHungerDrain(player, WaywardAttributeTypes.SWIMMING_EXHAUSTION);
    }

    public static float modifySprintingHungerDrain(Player player) {
        return modifyHungerDrain(player, WaywardAttributeTypes.SPRINTING_EXHAUSTION);
    }

    public static float modifyHungerDrain(Player player, Holder<Attribute> relevantAttribute) {
        return (float) player.getAttributeValue(relevantAttribute);
    }

    public static void modifyEatingSpeed(LivingEntityUseItemEvent.Start event) {
        if (event.getItem().getComponents().has(DataComponents.FOOD)) {
            var entity = event.getEntity();
            double eatingSpeedMultiplier = Math.max(entity.getAttributeValue(WaywardAttributeTypes.EATING_SPEED), 0.1f);
            event.setDuration((int) (event.getDuration() / eatingSpeedMultiplier));
        }
    }
}