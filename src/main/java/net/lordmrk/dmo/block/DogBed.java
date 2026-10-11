package net.lordmrk.dmo.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DogBed extends HorizontalDirectionalBlock {

    protected static final VoxelShape BOTTOM = Block.box(-4.0, 0.0, -4.0, 20.0, 8.0, 20.0);
    protected static final VoxelShape EMPTY = Block.box(-3.0, 4.0, -3.0, 19.0, 8.0, 19.0);
    protected static final VoxelShape EMPTY_EAST = Block.box(19.0, 4.0, 0.0, 20.0, 8.0, 16.0);
    protected static final VoxelShape EMPTY_NORTH = Block.box(0.0, 4.0, -4.0, 16.0, 8.0, -3.0);
    protected static final VoxelShape EMPTY_SOUTH = Block.box(0.0, 4.0, 19.0, 16.0, 8.0, 20.0);
    protected static final VoxelShape EMPTY_WEST = Block.box(-4.0, 4.0, 0.0, -3.0, 8.0, 16.0);

    protected static final VoxelShape SHAPE_EAST;
    protected static final VoxelShape SHAPE_NORTH;
    protected static final VoxelShape SHAPE_SOUTH;
    protected static final VoxelShape SHAPE_WEST;

    private final DyeColor color;
    private final WoodType woodType;

    //private final String name;

    public DogBed(ResourceKey<Block> key, DyeColor color, WoodType woodType) {
        super(BlockBehaviour.Properties.of()
                .setId(key)
                .sound(SoundType.WOOD)
                .strength(0.2F)
                .noOcclusion()
                .ignitedByLava()
                .pushReaction(PushReaction.POPPED)
                .bounceRestitution(0.66F)
                .fallDistanceReduction(0.5F));

        this.color = color;
        //String[] woodModColonName = woodType.name().split(":");
        //this.name = StringUtils.capitalizeEveryWord(color.getName().replaceAll("_", " ")) + " " + StringUtils.capitalizeEveryWord(woodModColonName[woodModColonName.length - 1].replaceAll("_", " ")) + " Dog Bed";
        this.woodType = woodType;

        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    public DyeColor getColor() {
        return color;
    }
/*
    @Override
    public MutableText getName() {
        return Text.literal(this.name);
    }*/

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);

        switch(direction) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_EAST;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    static {
        SHAPE_EAST = Shapes.join(BOTTOM, Shapes.or(EMPTY, EMPTY_EAST), BooleanOp.ONLY_FIRST);
        SHAPE_NORTH = Shapes.join(BOTTOM, Shapes.or(EMPTY, EMPTY_NORTH), BooleanOp.ONLY_FIRST);
        SHAPE_SOUTH = Shapes.join(BOTTOM, Shapes.or(EMPTY, EMPTY_SOUTH), BooleanOp.ONLY_FIRST);
        SHAPE_WEST = Shapes.join(BOTTOM, Shapes.or(EMPTY, EMPTY_WEST), BooleanOp.ONLY_FIRST);
    }
}
