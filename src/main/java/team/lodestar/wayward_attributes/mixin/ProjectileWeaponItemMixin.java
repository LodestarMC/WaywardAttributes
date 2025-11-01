package team.lodestar.wayward_attributes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import team.lodestar.wayward_attributes.tweaks.RangedAttributeTweaks;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {

    @ModifyArg(method = "shoot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V"), index = 3)
    private float enchmod$modifyVelocity(float velocity, @Local(name = "shooter") LivingEntity shooter, @Local(name = "weapon") ItemStack weapon, @Local(name = "projectileItems") List<ItemStack> projectileItems) {
        return RangedAttributeTweaks.modifyVelocity(velocity, shooter, weapon, projectileItems);
    }

    @ModifyArg(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V"), index = 1)
    private Projectile enchmod$modifyDamage(Projectile projectile, @Local(name = "shooter") LivingEntity shooter) {
        RangedAttributeTweaks.modifyDamage(shooter, projectile);
        return projectile;
    }
}
