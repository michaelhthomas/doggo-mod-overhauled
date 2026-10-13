package net.lordmrk.dmo.client.render.entity.model;

import net.lordmrk.dmo.DoggoAction;
import net.lordmrk.dmo.client.render.entity.DoggoRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.Set;

public class DoggoEntityModel extends EntityModel<DoggoRenderState> {

	/**
	 * The key of the real head model part, whose value is {@value}.
	 */
	private static final String REAL_HEAD = "real_head";
	/**
	 * The key of the upper body model part, whose value is {@value}.
	 */
	private static final String UPPER_BODY = "upper_body";
	/**
	 * The key of the real tail model part, whose value is {@value}.
	 */
	private static final String REAL_TAIL = "real_tail";
	/**
	 * The main bone used to animate the head. Contains {@link #realHead} as one of
	 * its children.
	 */
	public final ModelPart head;
	protected final ModelPart realHead;
	protected final ModelPart torso;
	protected final ModelPart rightHindLeg;
	protected final ModelPart leftHindLeg;
	protected final ModelPart rightFrontLeg;
	protected final ModelPart leftFrontLeg;
	/**
	 * The main bone used to animate the tail. Contains {@link #realTail} as one of
	 * its children.
	 */
	protected final ModelPart tail;
	protected final ModelPart realTail;
	protected final ModelPart neck;
	protected final ModelPart mouth;

	protected static final float PI = Mth.PI;
	protected static final float PI_2 = PI * 2;
	protected static final float PI_3 = PI * 3;
	protected static final float PI_8 = PI * 8;
	protected static final float PI_10 = PI * 10;
	protected static final float PI_11 = PI * 11;
	protected static final float PI_13 = PI * 13;
	protected static final float PI_43 = PI * 43;
	protected static final float PI_49 = PI * 49;

	protected static final float DEGREES_1   = PI / 180;
	protected static final float DEGREES_4   = DEGREES_1 * 4;
	protected static final float DEGREES_5   = DEGREES_1 * 5;
	protected static final float DEGREES_6   = DEGREES_1 * 6;
	protected static final float DEGREES_10  = DEGREES_1 * 10;
	protected static final float DEGREES_15  = DEGREES_1 * 15;
	protected static final float DEGREES_20  = DEGREES_1 * 20;
	protected static final float DEGREES_25  = DEGREES_1 * 25;
	protected static final float DEGREES_30  = DEGREES_1 * 30;
	protected static final float DEGREES_35  = DEGREES_1 * 35;
	protected static final float DEGREES_40  = DEGREES_1 * 40;
	protected static final float DEGREES_45  = DEGREES_1 * 45;
	protected static final float DEGREES_50  = DEGREES_1 * 50;
	protected static final float DEGREES_55  = DEGREES_1 * 55;
	protected static final float DEGREES_60  = DEGREES_1 * 60;
	protected static final float DEGREES_65  = DEGREES_1 * 65;
	protected static final float DEGREES_70  = DEGREES_1 * 70;
	protected static final float DEGREES_72  = DEGREES_1 * 72;
	protected static final float DEGREES_75  = DEGREES_1 * 75;
	protected static final float DEGREES_80  = DEGREES_1 * 80;
	protected static final float DEGREES_85  = DEGREES_1 * 85;
	protected static final float DEGREES_90  = DEGREES_1 * 90;
	protected static final float DEGREES_95  = DEGREES_1 * 95;
	protected static final float DEGREES_100 = DEGREES_1 * 100;
	protected static final float DEGREES_105 = DEGREES_1 * 105;
	protected static final float DEGREES_110 = DEGREES_1 * 110;
	protected static final float DEGREES_115 = DEGREES_1 * 115;
	protected static final float DEGREES_120 = DEGREES_1 * 120;
	protected static final float DEGREES_125 = DEGREES_1 * 125;
	protected static final float DEGREES_130 = DEGREES_1 * 130;
	protected static final float DEGREES_180 = Mth.PI;
	protected static final float DEGREES_270 = DEGREES_1 * 270;
	protected static final float DEGREES_333 = DEGREES_1 * 333;

	public DoggoEntityModel(ModelPart root) {
		super(root);
		this.head = root.getChild(PartNames.HEAD);
	    this.realHead = this.head.getChild(REAL_HEAD);
		this.mouth = this.realHead.getChild("doggo_mouth");
	    this.torso = root.getChild(PartNames.BODY);
	    this.neck = root.getChild(UPPER_BODY);
	    this.rightHindLeg = root.getChild(PartNames.RIGHT_HIND_LEG);
	    this.leftHindLeg = root.getChild(PartNames.LEFT_HIND_LEG);
	    this.rightFrontLeg = root.getChild(PartNames.RIGHT_FRONT_LEG);
	    this.leftFrontLeg = root.getChild(PartNames.LEFT_FRONT_LEG);
	    this.tail = root.getChild(PartNames.TAIL);
	    this.realTail = this.tail.getChild(REAL_TAIL);
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		// Head
		PartDefinition modelPartData2 = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.offset(-1.0F, 13.5F, -7.0F));
		modelPartData2.addOrReplaceChild(REAL_HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F).texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F).texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F).texOffs(0, 10).addBox(-0.5F, -0.001F, -5.0F, 3.0F, 2.0F, 4.0F), PartPose.ZERO)
		// The lower jaw. Box UV unwrapping sends a 1-tall box's underside and its side faces to
		// different texture rows, so it takes two slabs to reproduce the vanilla wolf muzzle's bottom
		// unit exactly from the wolf's own texture: the UP face samples the muzzle underside
		// (texOffs 0,10 -> the wolf's u7..10 v10..14) and the side faces sample the lower lip row
		// (texOffs 0,12 -> the wolf's v16). A third slab carries the mouth-interior overlay on the top
		// face, which the upper muzzle hides while the mouth is closed. The offset parks the underside
		// at the wolf muzzle's own y=2.999 so it does not z-fight the head's underside.
			.addOrReplaceChild("doggo_mouth", CubeListBuilder.create()
				.texOffs(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3.0F, 1.0F, 4.0F, Set.of(Direction.UP))
				.texOffs(0, 12).addBox(-0.5F, 0.0F, -5.0F, 3.0F, 1.0F, 4.0F, Set.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST))
				.texOffs(43, 14).addBox(-0.5F, 0.0F, -5.0F, 3.0F, 1.0F, 4.0F, Set.of(Direction.DOWN)),
				PartPose.offset(0.0F, 1.999F, 0.0F));

		// Body
		modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
		modelPartData.addOrReplaceChild(UPPER_BODY, CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5707964F, 0.0F, 0.0F));

		// Legs
		CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F);
		CubeListBuilder mirroredLeg = CubeListBuilder.create().mirror().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F);
		modelPartData.addOrReplaceChild(PartNames.RIGHT_HIND_LEG, mirroredLeg, PartPose.offset(-2.5F, 16.0F, 7.0F));
		modelPartData.addOrReplaceChild(PartNames.LEFT_HIND_LEG, leg, PartPose.offset(0.5F, 16.0F, 7.0F));
		modelPartData.addOrReplaceChild(PartNames.RIGHT_FRONT_LEG, mirroredLeg, PartPose.offset(-2.5F, 16.0F, -4.0F));
		modelPartData.addOrReplaceChild(PartNames.LEFT_FRONT_LEG, leg, PartPose.offset(0.5F, 16.0F, -4.0F));

		// Tail
		PartDefinition modelPartData3 = modelPartData.addOrReplaceChild(PartNames.TAIL, CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, 0.62831855F, 0.0F, 0.0F));
		modelPartData3.addOrReplaceChild(REAL_TAIL, CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.ZERO);

		return LayerDefinition.create(modelData, 64, 32);
	}

	@Override
	public void setupAnim(DoggoRenderState state) {
		switch(state.action) {
		case NAPPING:
			return;
		case EATING_FROM_BOWL:
			this.head.xRot = 0.69813170f;
			this.head.yRot = state.yRot * 0.017453292F;
			break;
		case EATING:
		case NEUTRAL:
		case PLAY_TIME:
			this.head.xRot = state.xRot * 0.017453292F;
			this.head.yRot = state.yRot * 0.017453292F;
		default:
			break;
		}

		tail(state);
		setMouth(state);
	}

	private void setMouth(DoggoRenderState state) {
		if(state.hasMouthOpened || state.hasStackInMouth) {
			this.mouth.xRot = 0.17453292f;
		} else {
			this.mouth.xRot = 0f;
		}
	}
	
	private void tail(DoggoRenderState state) {
		float h = state.tailAngle;

		if(!state.inSittingPose && state.action != DoggoAction.LISTENING && state.action != DoggoAction.SCRATCHING) {
			h += (this.torso.xRot - 1.57079632f);
		}

		switch(state.feeling) {
		case NEUTRAL:
			this.tail.yRot = 0f;
			this.tail.xRot = h;
			return;
		case HAPPY:
			this.tail.yRot = Mth.cos(state.animationTick) * 0.4f;
			this.tail.xRot = h;
			return;
		case SAD:
			this.tail.yRot = 0f;
			this.tail.xRot = 40f;
			return;
		}
	}
}
