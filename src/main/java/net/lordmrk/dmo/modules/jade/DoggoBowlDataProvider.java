package net.lordmrk.dmo.modules.jade;

import net.lordmrk.dmo.DoggoModOverhauled;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum DoggoBowlDataProvider implements IServerDataProvider<BlockAccessor> {

    INSTANCE;

    private static final String KEY_CUSTOM_NAME = "CustomName";

    @Override
    public void appendServerData(CompoundTag nbt, BlockAccessor accessor) {
        DogBowlEntity dogBowl = (DogBowlEntity) accessor.getBlockEntity();

        if (dogBowl.getCustomName() != null) {
            nbt.putString(KEY_CUSTOM_NAME, dogBowl.getCustomName().getString());
        }
    }

    @Override
    public Identifier getUid() {
        return DoggoModOverhauled.JADE_DOG_BOWL;
    }
}
