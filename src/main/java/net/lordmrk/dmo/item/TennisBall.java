package net.lordmrk.dmo.item;

import net.lordmrk.dmo.DoggoEntities;
import net.lordmrk.dmo.entity.projectile.thrown.AbstractTennisBallEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class TennisBall extends Item {

    private final DyeColor color;
    private final boolean legacy;

    public TennisBall(ResourceKey<Item> key, DyeColor color, boolean legacy) {
        super(new Item.Properties().stacksTo(16).setId(key));

        this.color = color;
        this.legacy = legacy;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        if(this.legacy) {
            tooltip.accept(Component.literal("This version of tennis ball is deprecated and will be removed in the future.").withStyle(ChatFormatting.RED));
            tooltip.accept(Component.literal("Use a crafting table to convert it into up-to-date tennis ball.").withStyle(ChatFormatting.RED));
        }
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        if(this.legacy) {
            return InteractionResult.FAIL;
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide()) {
            AbstractTennisBallEntity tennisBallEntity = DoggoEntities.newTennisBall(world, user, this.getColor());
            tennisBallEntity.setItem(itemStack);
            tennisBallEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(tennisBallEntity);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, user);

        return InteractionResult.SUCCESS;
    }
}
