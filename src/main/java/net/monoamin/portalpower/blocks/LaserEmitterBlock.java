package net.monoamin.portalpower.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.monoamin.portalpower.blockentities.LaserEmitterBlockEntity;
import net.monoamin.portalpower.blockentities.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class LaserEmitterBlock extends Block implements EntityBlock {

    public static final BooleanProperty ACTIVE = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public LaserEmitterBlock() {
        super(Properties.of()
                .strength(5.0f, 6.0f)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false)
                .setValue(FACING, Direction.NORTH)); // Default facing direction
    }

    // Called when the block is placed
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING);  // Add both ACTIVE and FACING properties to the block's state definition
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Get the direction the player is facing
        Direction direction = context.getNearestLookingDirection();
        // Make sure the block faces the player unless they are placing it up/down
        if (direction.getAxis().isVertical()) {
            direction = context.getHorizontalDirection().getOpposite(); // Face opposite to player for horizontal directions
        }
        return this.defaultBlockState()
                .setValue(FACING, direction)  // Set the block's facing direction
                .setValue(ACTIVE, false); // Default to inactive
    }

    // This method is responsible for updating the block state when needed
    public void setActive(Level level, BlockPos pos, BlockState state, boolean isActive) {
        if (level instanceof ServerLevel) {
            // Update the block state only if the ACTIVE state has changed
            if (state.getValue(ACTIVE) != isActive) {
                level.setBlock(pos, state.setValue(ACTIVE, isActive), 3);  // Flag 3 for update
            }
        }
    }

    @Nullable
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            // Toggle the ACTIVE state on right-click (for testing or demonstration purposes)
            boolean isActive = !state.getValue(ACTIVE);
            this.setActive(level, pos, state, isActive);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Check if we are dealing with the correct block entity type
        if (type == ModBlockEntities.LASER_EMITTER.get()) {
            // Server side ticker
            if (!level.isClientSide) {
                return (lvl, pos, st, be) -> LaserEmitterBlockEntity.serverTick(lvl, pos, st, (LaserEmitterBlockEntity) be);
            }
            // Client side ticker
            else {
                return (lvl, pos, st, be) -> LaserEmitterBlockEntity.clientTick(lvl, pos, st, (LaserEmitterBlockEntity) be);
            }
        }
        return null;  // Return null if it's not the right block entity type
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LaserEmitterBlockEntity(pos, state);
    }
}
