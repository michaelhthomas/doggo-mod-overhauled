package net.lordmrk.dmo.block;

import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DogBowl extends BaseEntityBlock {

	protected static final VoxelShape SHAPE = Block.box(2.5, 0.0, 2.5, 13.5, 4.0, 13.5);

	private final DyeColor color;

	public DogBowl(ResourceKey<Block> key, DyeColor color) {
		super(BlockBehaviour.Properties.of().setId(key).sound(SoundType.STONE).strength(2.0f).requiresCorrectToolForDrops());

		this.color = color;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DogBowlEntity(pos, state);
	}

	public DyeColor getColor() {
		return color;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean movedByPiston) {
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity instanceof DogBowlEntity) {
			Containers.dropContents(world, pos, (DogBowlEntity)blockEntity);
			// update comparators
			world.updateNeighbourForOutputSignal(pos, this);
		}

		super.affectNeighborsAfterRemoval(state, world, pos, movedByPiston);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof DogBowlEntity)) {
			return;
		}

		DogBowlEntity dogBowlEntity = (DogBowlEntity) blockEntity;

		if(itemStack.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME)) {
			dogBowlEntity.setCustomName(itemStack.getHoverName());
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if(!world.isClientSide()) {
			MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);

			if(screenHandlerFactory != null) {
				player.openMenu(screenHandlerFactory);
			}
		}

		return InteractionResult.SUCCESS;
	}

	public static boolean isEqual(Block block) {
		return block instanceof DogBowl;
	}

	public static boolean canEatFromBowl(BlockPos pos, DoggoEntity doggoEntity) {
		Level world = doggoEntity.level();

		if(isEqual(world.getBlockState(pos).getBlock())) {
			DogBowlEntity dogBowlEntity = (DogBowlEntity) world.getBlockEntity(pos);

			if(dogBowlEntity.hasCustomName()) {
				if(doggoEntity.hasCustomName() && doggoEntity.getDisplayName().getString().equals(dogBowlEntity.getCustomName().getString())) {
					return true;
				}

				return false;
			}

			return true;
		}

		return false;
	}
}
