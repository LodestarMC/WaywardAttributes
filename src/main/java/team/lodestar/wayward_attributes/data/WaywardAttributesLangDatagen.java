package team.lodestar.wayward_attributes.data;

import net.minecraft.core.registries.*;
import net.minecraft.data.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.registry.*;

import java.util.*;

public class WaywardAttributesLangDatagen extends LanguageProvider {

    public WaywardAttributesLangDatagen(PackOutput gen) {
        super(gen, WaywardAttributes.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        var attributes = new HashSet<>(WaywardAttributeTypes.ATTRIBUTES.getEntries());

        attributes.remove(WaywardAttributeTypes.SWEEPING_DAMAGE_RADIUS);
        attributes.forEach(a -> {
            String name = DataHelper.toTitleCase(a.getId().getPath(), "_");
            add("attribute.name." + WaywardAttributes.MODID + "." + BuiltInRegistries.ATTRIBUTE.getKey(a.get()).getPath(), name);
        });


        add("attribute.name.player.sweeping_damage_ratio", "Sweep Damage");
        add("attribute.name.wayward_attributes.sweeping_damage_radius", "Sweep Radius");

    }
}