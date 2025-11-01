package team.lodestar.wayward_attributes.mixin.client;

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
import org.spongepowered.asm.mixin.injection.callback.*;
import team.lodestar.wayward_attributes.client.*;

import java.util.*;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {

    @Inject(method = "gatherTooltipComponentsFromElements", at = @At(value = "RETURN"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/event/RenderTooltipEvent$GatherComponents;getMaxWidth()I")), cancellable = true)
    private static void waywardAttributes$addEnchantmentTooltip(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir, @Local(name = "font") Font font, @Local RenderTooltipEvent.GatherComponents event) {
        cir.setReturnValue(AttributeTooltipRenderer.addToTooltip(stack, elements, cir.getReturnValue(), mouseX, screenWidth, font, event));
    }
}