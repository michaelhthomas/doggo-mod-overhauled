package net.lordmrk.dmo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerUtils {

    public static void decrementItemStackIfNotCreative(Player player, ItemStack itemStack) {
        if (player.getAbilities().instabuild) {
            return;
        }

        itemStack.shrink(1);
    }
}
