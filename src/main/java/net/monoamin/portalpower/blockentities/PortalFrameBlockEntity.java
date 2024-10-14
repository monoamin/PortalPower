package net.monoamin.portalpower.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PortalFrameBlockEntity extends BlockEntity {
    public PortalFrameBlockEntity(BlockPos pos, BlockState state) {
            super(ModBlockEntities.PORTAL_FRAME.get(), pos, state);
    }

    public  void setPortalActive(boolean active){

    }
}
