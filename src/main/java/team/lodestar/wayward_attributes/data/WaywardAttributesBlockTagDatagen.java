package team.lodestar.wayward_attributes.data;

import net.minecraft.core.*;
import net.minecraft.data.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.wayward_attributes.*;

import javax.annotation.*;
import java.util.concurrent.*;

public class WaywardAttributesBlockTagDatagen extends BlockTagsProvider {

    public WaywardAttributesBlockTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, WaywardAttributes.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Wayward Attributes Block Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
    }
}