package team.lodestar.wayward_attributes.mixin;

import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import team.lodestar.wayward_attributes.tweaks.*;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @ModifyArg(method = "checkMovementStatistics",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSwimming()Z"), to = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;onClimbable()Z")))
    private float enchmod$modifySwimmingHungerDrain(float exhaustion) {
        Player player = (Player) (Object) this;
        return exhaustion * HungerDrainTweaks.modifySwimmingHungerDrain(player);
    }

    @ModifyArg(method = "checkMovementStatistics",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSprinting()Z")))
    private float enchmod$modifySprintingHungerDrain(float exhaustion) {
        Player player = (Player) (Object) this;
        return exhaustion * HungerDrainTweaks.modifySprintingHungerDrain(player);
    }
}
