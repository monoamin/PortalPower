package net.monoamin.portalpower.blocks;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.monoamin.portalpower.blockentities.ResonatorCoreBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public class ResonatorCoreBlock extends Block implements EntityBlock {

    public ResonatorCoreBlock() {
        super(Properties.of()
                .strength(5.0f, 6.0f)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResonatorCoreBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (level0, pos0, state0, blockEntity) -> ((ResonatorCoreBlockEntity)blockEntity).tick(level,pos0,state0,(ResonatorCoreBlockEntity)blockEntity);
        //return type == ModBlockEntities.PORTAL_CONTROLLER.get() ? ResonatorCoreBlockEntity::tick : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ResonatorCoreBlockEntity) {
                // Open the container on the server side
                NetworkHooks.openScreen((ServerPlayer) player, (ResonatorCoreBlockEntity) blockEntity, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }
}