package net.monoamin.portalpower.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.monoamin.portalpower.blockentities.LaserEmitterBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ResonatorCoreBlock extends Block implements EntityBlock {
    public ResonatorCoreBlock() {
        super(Properties.of()
                .strength(5.0f, 6.0f)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LaserEmitterBlockEntity(pos,state);
    }
}
