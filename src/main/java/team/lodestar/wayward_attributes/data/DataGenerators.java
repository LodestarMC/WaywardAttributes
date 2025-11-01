package team.lodestar.wayward_attributes.data;

import net.minecraft.core.*;
import net.minecraft.data.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.common.data.*;
import net.neoforged.neoforge.data.event.*;
import team.lodestar.wayward_attributes.*;

import java.util.concurrent.*;

@EventBusSubscriber(modid = WaywardAttributes.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();

        var langDatagen = new WaywardAttributesLangDatagen(output);

        var blockTagDatagen = new WaywardAttributesBlockTagDatagen(output, provider, helper);
        var itemTagDatagen = new WaywardAttributesItemTagDatagen(output, provider, blockTagDatagen.contentsGetter(), helper);

        generator.addProvider(includeClient, langDatagen);

        generator.addProvider(includeServer, blockTagDatagen);
        generator.addProvider(includeServer, itemTagDatagen);
    }
}
