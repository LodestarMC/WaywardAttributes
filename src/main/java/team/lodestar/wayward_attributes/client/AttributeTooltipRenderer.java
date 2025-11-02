package team.lodestar.wayward_attributes.client;

import com.mojang.datafixers.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.*;
import org.jetbrains.annotations.*;
import team.lodestar.wayward_attributes.*;

import java.util.*;

public class AttributeTooltipRenderer {

    @ApiStatus.Internal
    public static List<ClientTooltipComponent> addToTooltip(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, List<ClientTooltipComponent> components) {
        var mutable = new ArrayList<>(components);
        for (Either<FormattedText, TooltipComponent> element : elements) {
            for (int j = 0; j < components.size(); j++) {
                var component = components.get(j);
                if (!(component instanceof ClientTextTooltip textTooltip)) {
                    continue;
                }

                var left = element.left();
                if (left.isPresent() && left.get() instanceof Component text) {
                    if (!textTooltip.text.equals(text.getVisualOrderText())) {
                        continue;
                    }

                    var display = findAttributeDisplay(stack, text);
                    if (display == null) {
                        continue;
                    }
                    var textColor = text.getStyle().getColor();
                    int color = textColor != null ? textColor.getValue() : -1;
                    if (color == 5592405) { // default gray color
                        continue;
                    }
                    int offset = 0;
                    var string = text.getString();
                    if (string.startsWith("+") || string.startsWith("-")) {
                        offset = 2;
                    }
                    component = new AttributeTooltipComponent(display, textTooltip.text, offset, color);
                }
                mutable.set(j, component);
            }
        }
        return Collections.unmodifiableList(mutable);
    }

    public static AttributeDisplay findAttributeDisplay(ItemStack stack, Component text) {
        var display = findAttributeDisplay(stack, text.getContents());
        if (display.isPresent()) {
            return display.get();
        }
        var siblings = text.getSiblings();
        if (!siblings.isEmpty()) {
            display = findAttributeDisplay(stack, siblings.getFirst().getContents());
            if (display.isPresent()) {
                return display.get();
            }
        }
        return null;
    }

    public static Optional<AttributeDisplay> findAttributeDisplay(ItemStack stack, ComponentContents contents) {
        if (!(contents instanceof TranslatableContents translatableContents)) {
            return Optional.empty();
        }
        var key = translatableContents.getKey();
        if (!key.contains("modifier")) {
            return Optional.empty();
        }
        for (Object arg : translatableContents.getArgs()) {
            if (!(arg instanceof MutableComponent component)) {
                continue;
            }
            if (!(component.getContents() instanceof TranslatableContents argContents)) {
                continue;
            }
            var attributeName = argContents.getKey();
            if (!attributeName.contains("attribute.name.")) {
                continue;
            }
            var attributeKey = attributeName.replace("attribute.name.", "");
            var attribute = getAttribute(ResourceLocation.tryParse(attributeKey));
            if (attribute == null) { //
                attributeKey = attributeKey.replaceFirst("\\.", ":");
                attribute = getAttribute(ResourceLocation.tryParse(attributeKey));
            }
            return Optional.ofNullable(AttributeDisplay.findMatching(stack, attribute));
        }
        return Optional.empty();
    }

    public static Holder<Attribute> getAttribute(@Nullable ResourceLocation id) {
        if (id == null) {
            return null;
        }
        var registryAccess = Minecraft.getInstance().level.registryAccess();
        var registry = registryAccess.registryOrThrow(Registries.ATTRIBUTE);
        var holder = registry.getHolder(ResourceKey.create(Registries.ATTRIBUTE, id));
        return holder.orElse(null);
    }
}
