package net.lordmrk.dmo.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lordmrk.dmo.DoggoEntities;
import net.lordmrk.dmo.entity.projectile.thrown.AbstractTennisBallEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TennisBall extends Item {

    private final DyeColor color;
    private final boolean legacy;

    public TennisBall(DyeColor color, boolean legacy) {
        super(new FabricItemSettings().stacksTo(16));

        this.color = color;
        this.legacy = legacy;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if(this.legacy) {
            tooltip.add(Component.literal("This version of tennis ball is deprecated and will be removed in the future.").withStyle(ChatFormatting.RED));
            tooltip.add(Component.literal("Use a crafting table to convert it into up-to-date tennis ball.").withStyle(ChatFormatting.RED));
        }
    }

    public DyeColor getColor() {
        return color;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        if(this.legacy) {
            return InteractionResultHolder.fail(itemStack);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            AbstractTennisBallEntity tennisBallEntity = DoggoEntities.newTennisBall(world, user, this.getColor());
            tennisBallEntity.setItem(itemStack);
            tennisBallEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(tennisBallEntity);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}
