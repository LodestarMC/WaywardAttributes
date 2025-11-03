package team.lodestar.wayward_attributes.mixin;

import com.llamalad7.mixinextras.sugar.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import team.lodestar.wayward_attributes.tweaks.*;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyArg(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    private float waywardAttributes$modifyJumpHungerDrain(float exhaustion) {
        Player player = (Player) (Object) this;
        return exhaustion * HungerDrainTweaks.modifyJumpingHungerDrain(player);
    }


    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private AABB waywardAttributes$modifySweepingRadius(AABB aabb) {
        Player player = (Player) ((Object) this);
        return SweepAttackTweaks.modifySweepingArea(player, aabb);
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE"), name = "f7")
    private float waywardAttributes$modifySweepingDamage(float original, @Local(name = "f") float damage) {
        Player player = (Player) ((Object) this);
        return SweepAttackTweaks.modifySweepingDamage(player, damage);
    }
}