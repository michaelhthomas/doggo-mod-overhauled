package net.lordmrk.dmo.modules.jade;

import net.lordmrk.dmo.DoggoModOverhauled;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum DoggoBowlComponentProvider implements IBlockComponentProvider {

    INSTANCE;

    private static final String KEY_CUSTOM_NAME = "CustomName";

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        CompoundTag nbt = accessor.getServerData();

        tooltip.clear();
        if (nbt.contains(KEY_CUSTOM_NAME)) {
            tooltip.append(Component.translatable("jade.doggomodoverhauled.dog_bowl.custom_name", nbt.getString(KEY_CUSTOM_NAME)).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        } else {
            tooltip.append(Component.translatable(accessor.getBlockState().getBlock().getDescriptionId()).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        }
    }

    @Override
    public Identifier getUid() {
        return DoggoModOverhauled.JADE_DOG_BOWL;
    }
}
