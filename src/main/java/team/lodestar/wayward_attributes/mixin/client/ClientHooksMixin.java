package team.lodestar.wayward_attributes.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.mojang.datafixers.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.*;
import net.neoforged.neoforge.client.event.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import team.lodestar.wayward_attributes.client.*;

import java.util.*;
import java.util.stream.*;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {

    @WrapOperation(method = "gatherTooltipComponentsFromElements", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;"))
    private static List<ClientTooltipComponent> waywardAttributes$addEnchantmentTooltip(Stream<?> instance, Operation<List<ClientTooltipComponent>> original,
                                                                                        @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) List<Either<FormattedText, TooltipComponent>> elements,
                                                                                        @Local(name = "mouseX") int mouseX, @Local(name = "screenWidth") int screenWidth,
                                                                                        @Local(name = "font") Font font, @Local RenderTooltipEvent.GatherComponents event) {
        final List<ClientTooltipComponent> result = original.call(instance);

        return AttributeTooltipRenderer.addToTooltip(stack, elements, result, mouseX, screenWidth, font, event);
    }
}