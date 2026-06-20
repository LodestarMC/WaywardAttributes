package team.lodestar.wayward_attributes.core.registry;

import net.minecraft.core.registries.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.neoforged.neoforge.event.entity.*;
import net.neoforged.neoforge.registries.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.tweaks.RangedAttributeTweaks;
import team.lodestar.wayward_attributes.tweaks.SweepAttackTweaks;
import team.lodestar.lodestone.systems.attribute.*;

import static team.lodestar.lodestone.registry.common.LodestoneAttributes.*;
import static team.lodestar.lodestone.systems.attribute.LodestoneRangedAttribute.create;
import static team.lodestar.wayward_attributes.WaywardAttributes.path;

public class WaywardAttributeTypes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, WaywardAttributes.MODID);

    public static final DeferredHolder<Attribute, Attribute> MAGIC_RESISTANCE = registerAttribute(
            create(path("magic_resistance"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_PROFICIENCY = registerAttribute(
            create(path("magic_proficiency"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = registerAttribute(
            create(path("magic_damage"), 0.0D, 0.0D, 2048.0D).setAsBaseAttribute(BASE_MAGIC_DAMAGE).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> JUMPING_EXHAUSTION = registerAttribute(
            create(path("jumping_exhaustion"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSentiment(Attribute.Sentiment.NEGATIVE).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> SWIMMING_EXHAUSTION = registerAttribute(
            create(path("swimming_exhaustion"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSentiment(Attribute.Sentiment.NEGATIVE).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> SPRINTING_EXHAUSTION = registerAttribute(
            create(path("sprinting_exhaustion"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSentiment(Attribute.Sentiment.NEGATIVE).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> EATING_SPEED = registerAttribute(
            create(path("eating_speed"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> DETECTION_RADIUS = registerAttribute(
            create(path("detection_radius"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSentiment(Attribute.Sentiment.NEGATIVE).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> ARROW_DAMAGE = registerAttribute(
            create(path("arrow_damage"), 0.0D, 0.0D, 2048.0D).setAsBaseAttribute(RangedAttributeTweaks.BASE_ARROW_DAMAGE).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ARROW_VELOCITY = registerAttribute(
            create(path("arrow_velocity"), 0.0D, 0.0D, 2048.0D).setAsBaseAttribute(RangedAttributeTweaks.BASE_ARROW_VELOCITY).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> DRAW_SPEED = registerAttribute(
            create(path("draw_speed"), 0.0D, 0.0D, 2048.0D).setAsBaseAttribute(RangedAttributeTweaks.BASE_DRAW_SPEED).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> SWEEPING_DAMAGE_RADIUS = registerAttribute(
            create(path("sweeping_damage_radius"), 0.0D, 0.0D, 2048.0D).setAsBaseAttribute(SweepAttackTweaks.BASE_SWEEP_RADIUS).setSyncable(true));

    public static DeferredHolder<Attribute, Attribute> registerAttribute(LodestoneAttributeBuilder builder) {
        return ATTRIBUTES.register(builder.id.getPath(), builder::build);
    }

    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(e -> {
            for (DeferredHolder<Attribute, ? extends Attribute> entry : ATTRIBUTES.getEntries()) {
                event.add(e, entry);
            }
        });
    }
}