package team.lodestar.wayward_attributes.mixin.egshels_stench;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import team.lodestar.wayward_attributes.EgshelsStenchConfig;

import static net.minecraft.world.item.Item.getPlayerPOVHitResult;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.BLOCK && level.getBlockState(blockhitresult.getBlockPos()).is(Blocks.END_PORTAL_FRAME)) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            player.startUsingItem(hand);
            if (level instanceof ServerLevel) {
                BlockPos targetPos = null;
                double distance = Double.MAX_VALUE;
                for (BlockPos pos : EgshelsStenchConfig.parseBlockPosList()) {
                    double dist = pos.distSqr(player.blockPosition());
                    if (dist < distance) {
                        distance = dist;
                        targetPos = pos;
                    }
                }
                if (targetPos != null) {
                    EyeOfEnder eyeofender = new EyeOfEnder(level, player.getX(), player.getY(0.5), player.getZ());
                    eyeofender.setItem(itemstack);
                    eyeofender.signalTo(targetPos);
                    level.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeofender.position(), GameEvent.Context.of(player));
                    level.addFreshEntity(eyeofender);
                    if (player instanceof ServerPlayer serverplayer) {
                        CriteriaTriggers.USED_ENDER_EYE.trigger(serverplayer, targetPos);
                    }

                    float f = Mth.lerp(level.random.nextFloat(), 0.33F, 0.5F);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, f);
                    itemstack.consume(1, player);
                    player.awardStat(Stats.ITEM_USED.get(Items.ENDER_EYE));
                    player.swing(hand, true);
                    return InteractionResultHolder.success(itemstack);
                }
            }

            return InteractionResultHolder.consume(itemstack);
        }
    }
}
