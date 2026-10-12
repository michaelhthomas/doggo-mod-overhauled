package net.lordmrk.dmo.client.render.block.entity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class DogBowlRenderState extends BlockEntityRenderState {

    public boolean hasCustomName;
    public String customName = "";
    public int amount;
    public final ItemStackRenderState food = new ItemStackRenderState();
}
