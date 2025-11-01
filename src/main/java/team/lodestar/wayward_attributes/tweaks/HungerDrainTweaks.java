package team.lodestar.wayward_attributes.tweaks;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import team.lodestar.wayward_attributes.registry.ModAttributeTypes;

public class HungerDrainTweaks {

    public static float modifyJumpingHungerDrain(Player player) {
        return modifyHungerDrain(player, ModAttributeTypes.JUMPING_EXHAUSTION);
    }

    public static float modifySwimmingHungerDrain(Player player) {
        return modifyHungerDrain(player, ModAttributeTypes.SWIMMING_EXHAUSTION);
    }

    public static float modifySprintingHungerDrain(Player player) {
        return modifyHungerDrain(player, ModAttributeTypes.SPRINTING_EXHAUSTION);
    }

    public static float modifyHungerDrain(Player player, Holder<Attribute> relevantAttribute) {
        return (float) player.getAttributeValue(relevantAttribute);
    }
}