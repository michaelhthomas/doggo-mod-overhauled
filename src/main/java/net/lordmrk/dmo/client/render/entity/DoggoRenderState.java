package net.lordmrk.dmo.client.render.entity;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.DoggoFeeling;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

/**
 * Render state for {@link net.lordmrk.dmo.entity.DoggoEntity}. Snapshots the entity each frame so the renderer,
 * models and layers never touch the live entity during rendering.
 */
public class DoggoRenderState extends HoldingEntityRenderState {

    public DoggoAction action = DoggoAction.NEUTRAL;
    public DoggoFeeling feeling = DoggoFeeling.NEUTRAL;
    public boolean hasMouthOpened;
    public boolean hasStackInMouth;
    public boolean inSittingPose;
    public boolean inLieDownPose;
    public boolean furWet;
    public boolean angry;
    public boolean tamed;
    public float animationTick;
    public int animation;
    public int scratchingSide;
    public float tailAngle;
    public DyeColor collarColor;
    public Identifier texture;

    /** Lerped animation values, resolved with the frame's partial tick. */
    public float begAnimationProgress;
    public float shakeProgress;

    public float getBegAnimationProgress() {
        return this.begAnimationProgress;
    }

    public float getShakeAnimationProgress(float offset) {
        float g = (this.shakeProgress + offset) / 1.8F;
        g = Mth.clamp(g, 0.0F, 1.0F);

        return Mth.sin(g * Mth.PI) * Mth.sin(g * Mth.PI * 11.0F) * 0.15F * Mth.PI;
    }
}
