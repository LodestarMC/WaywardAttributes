package team.lodestar.wayward_attributes.core.datagen;

import net.minecraft.core.*;
import net.minecraft.data.*;
import net.minecraft.data.tags.*;
import net.minecraft.world.damagesource.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.WaywardTags.*;

import java.util.concurrent.*;

public class WaywardAttributesDamageTypeDatagen extends DamageTypeTagsProvider {

    public WaywardAttributesDamageTypeDatagen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, WaywardAttributes.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(DamageTypeTags.IS_MAGIC).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC);
        tag(DamageTypeTags.AFFECTED_BY_MAGIC_RESISTANCE).addTag(DamageTypeTags.IS_MAGIC);
        tag(DamageTypeTags.AFFECTED_BY_MAGIC_PROFICIENCY).addTag(DamageTypeTags.IS_MAGIC);
        tag(DamageTypeTags.CAN_TRIGGER_MAGIC_DAMAGE).add(DamageTypes.PLAYER_ATTACK);
        tag(DamageTypeTags.IGNORES_MAGIC_ATTACK_COOLDOWN_SCALAR);
    }
}
