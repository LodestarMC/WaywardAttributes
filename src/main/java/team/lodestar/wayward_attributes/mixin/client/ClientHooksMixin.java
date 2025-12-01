package team.lodestar.wayward_attributes.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.wayward_attributes.client.*;

import java.util.List;
import java.util.stream.Stream;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {

    @Unique
    private static ItemStack waywardAttributes$attributeStack;

    @Inject(method = "gatherTooltipComponentsFromElements", at = @At(value = "HEAD"))
    private static void waywardAttributes$cacheItemStack(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        waywardAttributes$attributeStack = stack;
    }

    @WrapOperation(method = "lambda$gatherTooltipComponentsFromElements$10", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;create(Lnet/minecraft/util/FormattedCharSequence;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;"))
    private static ClientTooltipComponent waywardAttributes$addEnchantmentTooltip(FormattedCharSequence text, Operation<ClientTooltipComponent> original, @Local(argsOnly = true) FormattedText component) {
        var optional = AttributeTooltipRenderer.modifyComponent(waywardAttributes$attributeStack, component);
        return optional.orElseGet(() -> original.call(text));
    }

    @WrapOperation(method = "lambda$gatherTooltipComponentsFromElements$7", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;splitLine(Lnet/minecraft/network/chat/FormattedText;Lnet/minecraft/client/gui/Font;I)Ljava/util/stream/Stream;"))
    private static Stream<ClientTooltipComponent> waywardAttributes$addEnchantmentTooltipToSplitTooltip(FormattedText component, Font text, int font, Operation<Stream<ClientTooltipComponent>> original) {
        var optional = AttributeTooltipRenderer.modifyComponent(waywardAttributes$attributeStack, component);
        return optional.map(Stream::of).orElseGet(() -> original.call(component, text, font));
    }
}