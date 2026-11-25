package team.lodestar.wayward_attributes.config;

import net.neoforged.fml.config.ModConfig;
import team.lodestar.lodestone.modules.core.config.ConfigValueHolder;
import team.lodestar.lodestone.modules.core.config.LodestoneConfig;
import team.lodestar.lodestone.modules.core.config.LodestoneConfigHolder;

public class WaywardAttributesCommonConfig extends LodestoneConfig {

    public static final LodestoneConfigHolder<WaywardAttributesCommonConfig> HOLDER = new LodestoneConfigHolder<>(ModConfig.Type.COMMON, WaywardAttributesCommonConfig::new);
    public static final WaywardAttributesCommonConfig CONFIG = HOLDER.getConfig();

    public ConfigValueHolder<Boolean> bakedArrowCritDamage = create("tweaks.ranged", (builder ->
            builder.comment("In Vanilla, critical arrows receive a randomly rolled damage bonus.",
                            "This amounts to anywhere between 0-50% of the arrow's original damage, plus an additional 2.",
                            "Since ranged weapons now show their damage stats, this added bonus becomes unaccounted for in the tooltip.",
                            "If set to true, the crit damage will be baked in onto the ranged weapon's inherent properties.",
                            "This change is meant to make fired arrow' damage properly reflect what is shown in the tooltip.",
                            "Furthermore, this change is meant to remove randomness from the damage output of ranged weapons.")
                    .define("baked_arrow_crit_damage", true)));

    public ConfigValueHolder<Double> baseArrowDamage = create("tweaks.ranged", (builder ->
            builder.comment("Arrow damage is a combination of arrow damage multiplied by velocity.", "This config option defines the base arrow damage value that bows and crossbows will have.")
                    .define("base_arrow_damage", 2.0)));

    public ConfigValueHolder<Double> bowArrowVelocity = create("tweaks.ranged.bow", (builder ->
            builder.comment("The velocity applied to arrows fired using bows.")
                    .define("crossbow_arrow_velocity", 3.0)));
    public ConfigValueHolder<Double> bowDrawSpeed = create("tweaks.ranged.bow", (builder ->
            builder.comment("The draw speed value assigned to bows.")
                    .define("crossbow_draw_speed", 1.0)));

    public ConfigValueHolder<Double> crossbowArrowVelocity = create("tweaks.ranged.crossbow", (builder ->
            builder.comment("The velocity applied to arrows fired using crossbows.")
                    .define("crossbow_arrow_velocity", 4.0)));
    public ConfigValueHolder<Double> crossbowDrawSpeed = create("tweaks.ranged.crossbow", (builder ->
            builder.comment("The draw speed value assigned to crossbows.")
                    .define("crossbow_draw_speed", 0.75)));

    public ConfigValueHolder<Double> swordSweepRadius = create("tweaks.melee.sword", (builder ->
            builder.comment("The sweep attack radius used by swords.")
                    .define("sword_sweep_radius", 0.75)));
    public ConfigValueHolder<Double> swordSweepDamageRatio = create("tweaks.melee.sword", (builder ->
            builder.comment("The sweeping damage ratio used by swords.")
                    .define("sword_sweep_damage_ratio", 0.25)));

}