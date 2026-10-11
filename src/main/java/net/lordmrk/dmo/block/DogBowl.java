package net.lordmrk.dmo.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.lordmrk.dmo.block.entity.DogBowlEntity;
import net.lordmrk.dmo.entity.DoggoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DogBowl extends BaseEntityBlock implements EntityBlock {
	
	protected static final VoxelShape SHAPE = Block.box(2.5, 0.0, 2.5, 13.5, 4.0, 13.5);

	private final DyeColor color;

	public DogBowl(DyeColor color) {
		super(FabricBlockSettings.of().sound(SoundType.STONE).strength(2.0f).requiresCorrectToolForDrops());

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
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof DogBowlEntity) {
                Containers.dropContents(world, pos, (DogBowlEntity)blockEntity);
                // update comparators
                world.updateNeighbourForOutputSignal(pos,this);
            }

            super.onRemove(state, world, pos, newState, moved);
        }
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof DogBowlEntity)) {
			return;
		}

		DogBowlEntity dogBowlEntity = (DogBowlEntity) blockEntity;

		if(itemStack.hasCustomHoverName()) {
			dogBowlEntity.setCustomName(itemStack.getHoverName());
		}
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if(!world.isClientSide) {
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
