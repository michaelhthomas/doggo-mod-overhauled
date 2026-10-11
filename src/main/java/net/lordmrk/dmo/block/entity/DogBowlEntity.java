package net.lordmrk.dmo.block.entity;

import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.inventory.ImplementedInventory;
import net.lordmrk.dmo.screen.DogBowlScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DogBowlEntity extends BlockEntity implements MenuProvider, ImplementedInventory,  ContainerListener, Nameable {

	private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
	private Component customName;

	public DogBowlEntity(BlockPos pos, BlockState state) {
		super(DoggoModOverhauled.DOG_BOWL_ENTITY, pos, state);
	}

	@Override
	public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
		DogBowlScreenHandler handler = new DogBowlScreenHandler(syncId, inv, this);
		handler.addSlotListener(this);
		return handler;
	}

	@Override
	public Component getCustomName() {
		return this.customName;
	}

	@Override
	public Component getDisplayName() {
		return this.getName();
	}

	public int getFoodHunger() {
		if (this.items.isEmpty()) {
			return 0;
		}

		ItemStack itemStack = this.items.get(0);

		if (itemStack.isEmpty()) {
			return 0;
		}

		Item item = itemStack.getItem();

		if(!item.isEdible()) {
			return 0;
		}

		return item.getFoodProperties().getNutrition() * 4;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return items;
	}

	public Component getName() {
		if (this.customName == null) {
			return Component.translatable("block.doggomodoverhauled.dog_bowl_entity");
		}

		return Component.translatable("block.doggomodoverhauled.named_dog_bowl_entity", this.customName.getString());
	}

	public void foodEaten() {
		if (this.items.isEmpty()) {
			return;
		}

		ItemStack itemStack = this.items.get(0);

		if (itemStack.isEmpty()) {
			return;
		}

		itemStack.shrink(1);
		this.setChanged();
	}

	@Override
	public void load(CompoundTag nbt) {
		this.items.clear();

		ContainerHelper.loadAllItems(nbt, this.items);

		if(nbt.contains("CustomName", 8)) {
			this.customName = Component.Serializer.fromJson(nbt.getString("CustomName"));
		}

		this.setChanged();
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		nbt = ContainerHelper.saveAllItems(nbt, this.items);

		if(this.customName != null) {
			nbt.putString("CustomName", Component.Serializer.toJson(this.customName));
		}
	}

	public boolean hasCustomName() {
		return this.customName != null;
	}

	public boolean hasFood() {
		if (this.items.isEmpty()) {
			return false;
		}

		return !items.get(0).isEmpty();
	}

	public void setCustomName(Component customName) {
		this.customName = customName;
	}

//	public void fromClientTag(NbtCompound nbt) {
//		DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
//
//		Inventories.readNbt(nbt, items);
//
//		for(int i = 0; i < this.items.size(); i++) {
//			this.items.set(i, items.get(i));
//		}
//
//		if(nbt.contains("CustomName", 8)) {
//			this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
//		}
//	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}

	@Override
	public void setChanged() {
		if (this.level == null) {
			return;
		}

		this.level.blockEntityChanged(this.worldPosition);

		if (this.level.isClientSide()) {
			return;
		}

		((ServerLevel) this.level).getChunkSource().blockChanged(worldPosition); // Mark changes to be synced to the client.
	}


	@Override
	public void slotChanged(AbstractContainerMenu handler, int slotId, ItemStack stack) {

	}

	@Override
	public void dataChanged(AbstractContainerMenu handler, int property, int value) {

	}
}
