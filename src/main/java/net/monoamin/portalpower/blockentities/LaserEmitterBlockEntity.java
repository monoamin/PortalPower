package net.monoamin.portalpower.blockentities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.monoamin.portalpower.blocks.ResonatorCoreBlock;
import net.monoamin.portalpower.network.ModMessages;
import net.monoamin.portalpower.network.packet.LaserEmitterSyncPacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class LaserEmitterBlockEntity extends BlockEntity {
    private final EnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyCapability;
    private int currentEnergyLevel;
    private int maxEnergyLevel;
    private int activeEnergyUsePerTick = 10;

    private boolean isActive;
    private static final float[] BASE_TINT = new float[] { 0.5f, 0.5f, 1.0f };
    private static final float BASE_LUMINANCE = luminance(BASE_TINT);
    private List<BeaconBeamSection> beamSections = Lists.newArrayList();


    public int getCurrentEnergyLevel() {
        return currentEnergyLevel;
    }

    public void setCurrentEnergyLevel(int currentEnergyLevel) {
        this.currentEnergyLevel = currentEnergyLevel;
    }

    public int getMaxEnergyLevel() {
        return maxEnergyLevel;
    }

    public void setMaxEnergyLevel(int maxEnergyLevel) {
        this.maxEnergyLevel = maxEnergyLevel;
    }

    public int getActiveEnergyUsePerTick() {
        return activeEnergyUsePerTick;
    }

    public void setActiveEnergyUsePerTick(int activeEnergyUsePerTick) {
        this.activeEnergyUsePerTick = activeEnergyUsePerTick;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LaserEmitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LASER_EMITTER.get(), pos, state);
        this.energyStorage = new EnergyStorage(10000, 100, 0, 0);
        this.energyCapability = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LaserEmitterBlockEntity be) {
        // Server-side logic
        be.currentEnergyLevel = be.energyStorage.getEnergyStored();
        be.maxEnergyLevel = be.energyStorage.getMaxEnergyStored();
        if (be.currentEnergyLevel >= be.activeEnergyUsePerTick && !be.isActive) {
            be.energyStorage.extractEnergy(be.activeEnergyUsePerTick, false);
            be.isActive = true;
            be.setChanged(); // Notify the game that the block state has changed
            level.sendBlockUpdated(pos, state, state, 3); // Synchronize with client
            be.syncWithClient(); // Send synchronization packet to client
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, LaserEmitterBlockEntity blockEntity) {
        // Client-side logic (like beam rendering)
        updateBeamSections((ClientLevel) level, pos, state, blockEntity);
    }

    private static void updateBeamSections(ClientLevel level, BlockPos pos, BlockState state, LaserEmitterBlockEntity laserEmitterBlockEntity) {
        // System.out.println("Updating beam sections at " + pos);
        if (!laserEmitterBlockEntity.isActive()) {
            // System.out.println("Resetting sections");
            laserEmitterBlockEntity.beamSections = Lists.newArrayList();
            return;
        }

        // Get the maximum build height.
        int maxHeight = level.getMaxBuildHeight();

        // Iterate up from the heart, setting the tint as each block is passed.
        int currentHeight = pos.above().getY();
        BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos().set(pos.above());
        float[] previousTint = BASE_TINT;
        float[] currentTint;
        BeaconBeamSection section = new BeaconBeamSection(BASE_TINT);
        // System.out.println("Resetting sections");
        laserEmitterBlockEntity.beamSections = Lists.newArrayList();

        while (currentHeight < maxHeight) {
            BlockState currentState = level.getBlockState(currentPos);
            // Get the current block's color multiplier.
            currentTint = currentState.getBeaconColorMultiplier(level, currentPos, pos);

            if (currentTint == null || compareTints(previousTint, currentTint)) {
                section.increaseHeight();
                currentHeight++;
                currentPos.move(Direction.UP);
                continue;
            }

            // Calculate the new tint.
            float[] newTint = new float[] { previousTint[0] * currentTint[0], previousTint[1] * currentTint[1],
                    previousTint[2] * currentTint[2] };

            // Get the luminance of the new tint
            float newLuminance = luminance(newTint);

            // Scale up the new tint;
            newTint[0] = Math.min(1.0f, newTint[0] * BASE_LUMINANCE / newLuminance);
            newTint[1] = Math.min(1.0f, newTint[1] * BASE_LUMINANCE / newLuminance);
            newTint[2] = Math.min(1.0f, newTint[2] * BASE_LUMINANCE / newLuminance);

            // Add the old section to the list.
            // System.out.println("Adding section: " + section);
            laserEmitterBlockEntity.beamSections.add(section);

            // Make a new section, with the new tint.
            section = new BeaconBeamSection(newTint);

            // Update the previous tint.
            previousTint = newTint;
            // Update the coordinates.
            currentHeight++;
            currentPos.move(Direction.UP);
        }
        // System.out.println("Adding section: " + section);
        laserEmitterBlockEntity.beamSections.add(section);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        // Create a packet to synchronize block entity data with the client
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private void syncWithClient() {
        if (this.level instanceof ServerLevel serverLevel) {
            ModMessages.sendToAllPlayers(new LaserEmitterSyncPacket(this.worldPosition, this.currentEnergyLevel, this.isActive));
        }
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("block.portalpower.laser_emitter.isactive", this.isActive);
        pTag.putInt("block.portalpower.laser_emitter.energystored", energyStorage.getEnergyStored());
        pTag.putInt("block.portalpower.laser_emitter.maxenergystored", energyStorage.getMaxEnergyStored());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.isActive = tag.getBoolean("block.portalpower.laser_emitter.isactive");
        this.currentEnergyLevel = tag.getInt("block.portalpower.laser_emitter.energystored");
        this.maxEnergyLevel = tag.getInt("block.portalpower.laser_emitter.maxenergystored");
    }

    public boolean isActive() {
        return this.isActive;
    }

    @SuppressWarnings("unchecked")
    // Get the current beam sections, if the emitter is inactive, return an empty list
    public List<BeaconBeamSection> getBeamSections() {
        if (!this.isActive) {
            return ImmutableList.of();
        }
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        this.updateBeamSectionsForDirection(facing);
        return this.beamSections;
    }

    // Update beam sections based on the direction the block is facing and stop when hitting a block
    private void updateBeamSectionsForDirection(Direction facing) {
        this.beamSections.clear();
        BlockPos currentPos = this.worldPosition;
        Level level = this.getLevel();
        float[] tint = BASE_TINT;

        // Traverse in the direction of the beam until hitting a block or reaching a max range
        for (int i = 0; i < 256; i++) { // Arbitrary maximum beam length
            currentPos = currentPos.relative(facing);
            BlockState blockCurrentPos = level.getBlockState(currentPos);
            if (level == null) break;
            if (blockCurrentPos != Blocks.AIR.defaultBlockState()) {
                if (blockCurrentPos == ResonatorCoreBlock.stateById(0)){
                    // do something with resonant core
                    break;
                }
                else
                {
                    break; // Stop the beam if it hits a block
                }
            }
            this.beamSections.add(new BeaconBeamSection(tint)); // Add beam section with base tint
        }
    }

    // Compare two color tints for equality
    private static boolean compareTints(float[] previous, float[] current) {
        // If lengths are not equal, arrays cannot be identical
        if (previous.length != current.length) {
            return false;
        }

        // Compare each element of the arrays
        for (int i = 0; i < previous.length; i++) {
            if (previous[i] != current[i]) {
                return false;
            }
        }

        return true;
    }

    // Calculate luminance of a color tint using standard RGB luminance formula
    private static float luminance(float[] tint) {
        return tint[0] * 0.2126f + tint[1] * 0.7152f + tint[2] * 0.0722f;
    }

    // Inner class representing a section of the laser beam
    public static class BeaconBeamSection {
        final float[] color; // Color of this beam section
        private int height; // Height of this beam section

        public BeaconBeamSection(float[] tint) {
            this.color = tint;
            this.height = 1; // Initial height is 1
        }

        // Increase the height of the beam section
        protected void increaseHeight() {
            ++this.height;
        }

        // Get the color of the beam section
        public float[] getColor() {
            return this.color;
        }

        // Get the height of the beam section
        public int getHeight() {
            return this.height;
        }
    }
}
