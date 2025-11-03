package team.lodestar.wayward_attributes.tweaks;

import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.wayward_attributes.registry.*;

public class DetectionTweaks {

    public static void modifyVisibility(LivingEvent.LivingVisibilityEvent event) {
        var entity = event.getEntity();
        double detectionRadius = Math.max(entity.getAttributeValue(WaywardAttributeTypes.DETECTION_RADIUS), 0.1f);
        event.modifyVisibility(event.getVisibilityModifier() * detectionRadius);
    }
}