package net.lordmrk.dmo.modules.jade;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum DebugDoggoComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {

    INSTANCE;

    private static Identifier identifier = Identifier.withDefaultNamespace("debug");
    private static String KEY_ACTION = "Action";
    private static String KEY_GOAL_DELAY = "GoalDelay";

    @Override
    public void appendServerData(CompoundTag nbt, EntityAccessor entityAccessor) {
        DoggoEntity doggoEntity = (DoggoEntity) entityAccessor.getEntity();
        nbt.putString(KEY_ACTION, doggoEntity.getAction().name());
        nbt.putInt(KEY_GOAL_DELAY, doggoEntity.getGoalData(DoggoAction.PLAY_IN_SNOW).getDelayTicks());
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig iPluginConfig) {
        CompoundTag nbt = accessor.getServerData();

        if (nbt.contains(KEY_ACTION)) {
            tooltip.add(Component.literal("DoggoAction: " + nbt.getString(KEY_ACTION)));
        }

        if (nbt.contains(KEY_GOAL_DELAY)) {
            tooltip.add(Component.literal("Doggo Goal Delay: " + nbt.getInt(KEY_GOAL_DELAY)));
        }
    }

    @Override
    public Identifier getUid() {
        return identifier;
    }
}
