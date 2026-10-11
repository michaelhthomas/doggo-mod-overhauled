package net.lordmrk.dmo.screen;

import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DogBowlScreenHandler extends AbstractContainerMenu {

    private final Container inv;

    public DogBowlScreenHandler(int syncId, Inventory playerInv) {
        this(syncId, playerInv, new SimpleContainer(1));
    }

    public DogBowlScreenHandler(int syncId, Inventory playerInv, Container inv) {
        super(DoggoModOverhauled.DOG_BOWL_SCREEN_HANDLER, syncId);

        checkContainerSize(inv, 1);
        this.inv = inv;

        inv.startOpen(playerInv.player);

        //Our inventory
        this.addSlot(new DogFoodSlot(inv, 0, 80, 17));

        //The player inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 48 + y * 18));
            }
        }

        //The player Hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInv, x, 8 + x * 18, 106));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return inv.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        Slot slot = this.slots.get(invSlot);

        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            ItemStack newStack = originalStack.copy();

            if (invSlot < this.inv.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inv.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inv.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            return newStack;
        }

        return ItemStack.EMPTY;
    }

    static class DogFoodSlot extends Slot {

        public DogFoodSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.get(DataComponents.FOOD) != null && stack.is(ItemTags.MEAT);
        }

        public int getMaxStackSize() {
            return 64;
        }
    }
}
