package team.lodestar.wayward_attributes.data;

import net.minecraft.core.HolderLookup.*;
import net.minecraft.data.*;
import net.minecraft.data.tags.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.data.*;
import org.jetbrains.annotations.*;
import team.lodestar.lodestone.registry.common.tag.*;
import team.lodestar.wayward_attributes.*;

import java.util.concurrent.*;

public class WaywardAttributesItemTagDatagen extends ItemTagsProvider {

    public WaywardAttributesItemTagDatagen(PackOutput pOutput, CompletableFuture<Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, WaywardAttributes.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(Provider pProvider) {
        tag(LodestoneItemTags.RANGED_ENCHANTABLE);
        tag(ModTags.ItemTags.AFFECTED_BY_DRAW_SPEED).addTags(LodestoneItemTags.RANGED_ENCHANTABLE);
    }
}