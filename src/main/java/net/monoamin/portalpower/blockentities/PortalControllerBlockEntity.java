package net.monoamin.portalpower.blockentities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.monoamin.portalpower.BoundingBox;
import net.monoamin.portalpower.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.common.util.LazyOptional;
import net.monoamin.portalpower.blocks.PortalControllerBlock;
import net.monoamin.portalpower.blocks.PortalFrameBlock;
import net.monoamin.portalpower.menus.PortalControllerMenu;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PortalControllerBlockEntity extends BlockEntity implements MenuProvider {
    private final EnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyCapability;
    private boolean isOn = false;
    private static int ticks = 0;
    private static final int INPUT_SLOT = 0;
    private static final int PORTAL_STATE = 1;
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    public int displayEnergyLevel = 0;
    public int displayMaxEnergyLevel = 100;

    public PortalControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTAL_CONTROLLER.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int p_39284_) {
                return switch (p_39284_) {
                    case 0 -> PortalControllerBlockEntity.this.displayEnergyLevel;
                    case 1 -> PortalControllerBlockEntity.this.displayMaxEnergyLevel;
                    default -> 0;
                };
            }

            @Override
            public void set(int p_39285_, int p_39286_) {
                switch (p_39285_) {
                    case 0 -> PortalControllerBlockEntity.this.displayEnergyLevel = p_39286_;
                    case 1 -> PortalControllerBlockEntity.this.displayMaxEnergyLevel = p_39286_;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };


        this.energyStorage = new EnergyStorage(100000, 1000, 0, 0);
        this.energyCapability = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyCapability.cast();
        }
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCapability.invalidate();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public boolean isOn() {
        return isOn;
    }

    public void toggle() {
        isOn = !isOn;
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("block.portalpower.resonator_core_block.ison", isOn);
        pTag.put("block.portalpower.resonator_core_block.inventory", itemHandler.serializeNBT());
        pTag.putInt("block.portalpower.resonator_core_block.energystored", energyStorage.getEnergyStored());
        pTag.putInt("block.portalpower.resonator_core_block.maxenergystored", energyStorage.getMaxEnergyStored());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.isOn = tag.getBoolean("block.portalpower.resonator_core_block.ison");
        // Deserialize the inventory
        if (tag.contains("block.portalpower.resonator_core_block.inventory", Tag.TAG_COMPOUND)) {
            itemHandler.deserializeNBT(tag.getCompound("block.portalpower.resonator_core_block.inventory"));
        }
        this.displayEnergyLevel = tag.getInt("block.portalpower.resonator_core_block.energystored");
        this.displayMaxEnergyLevel = tag.getInt("block.portalpower.resonator_core_block.maxenergystored");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Portal Controller");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new PortalControllerMenu(id, playerInventory, this, this.data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PortalControllerBlockEntity blockEntity) {
        if (level == null || level.isClientSide()) {
            return;
        }
        blockEntity.displayEnergyLevel = blockEntity.energyStorage.getEnergyStored();
        blockEntity.displayMaxEnergyLevel = blockEntity.energyStorage.getMaxEnergyStored();
        blockEntity.energyStorage.extractEnergy(10,false);

        // Every 1 seconds
        if (ticks == 20) {

            // Calculate required energy based on portal frame
            int requiredEnergy = blockEntity.calculateRequiredEnergy();

            if (blockEntity.energyStorage.getEnergyStored() >= requiredEnergy && !blockEntity.isOn) {
                blockEntity.activatePortal();
                blockEntity.energyStorage.extractEnergy(requiredEnergy, false);
                blockEntity.isOn = true;
                blockEntity.setChanged(); // Notify the game that the block state has changed
            } else if (blockEntity.isOn) {
                // Re-validate the portal frame in case it was altered
                PortalFrameInfo frameInfo = blockEntity.scanPortalFrame();
                if (frameInfo == null || blockEntity.energyStorage.getEnergyStored() < requiredEnergy) {
                    blockEntity.deactivatePortal();
                    blockEntity.isOn = false;
                    blockEntity.setChanged(); // Notify the game that the block state has changed
                }
            }
            ticks = 0;
        }
        else {
            ticks++;
        }
    }

    private void activatePortal() {
        // Implement portal activation logic
        // For example, changing portal blocks to active state or spawning particles
        System.out.println("Portal Activated at " + this.getBlockPos());
        // Example: level.setBlock(this.getBlockPos(), ModBlocks.ACTIVE_PORTAL.get().defaultBlockState(), 3);
    }

    private void deactivatePortal() {
        // Implement portal deactivation logic
        // For example, reverting portal blocks to inactive state or stopping particles
        System.out.println("Portal Deactivated at " + this.getBlockPos());
        // Example: level.setBlock(this.getBlockPos(), ModBlocks.INACTIVE_PORTAL.get().defaultBlockState(), 3);
    }

    private PortalFrameInfo scanPortalFrame() {
        // Step 1: Determine the portal plane
        Util.Plane portalPlane = null;
        BlockPos controllerPos = this.getBlockPos();

        for (BlockPos neighbor : Util.getNeighbors(controllerPos)) {
            if (level.getBlockState(neighbor) == PortalFrameBlock.stateById(0)) {
                portalPlane = Util.determinePlane(controllerPos, neighbor);
                break;
            }
        }

        if (portalPlane == null) {
            return null; // No adjacent frame blocks found
        }

        // Step 2: Collect all frame blocks in the portal plane using BFS
        List<BlockPos> frameBlocks = collectFrameBlocks(controllerPos, portalPlane);

        if (frameBlocks.isEmpty()) {
            return null; // No frame blocks found in the determined plane
        }

        // Step 3: Validate frame blocks
        if (!hasFullCorners(frameBlocks, portalPlane, controllerPos)) {
            return null;
        }
        if (!isSinglePlane(frameBlocks, portalPlane, controllerPos)) {
            return null;
        }
        if (!hasNoGaps(frameBlocks, portalPlane, controllerPos)) {
            return null;
        }

        // Calculate width and height based on bounding box
        BoundingBox bounds = Util.getBounds(frameBlocks);
        int width, height;
        switch (portalPlane) {
            case XY:
                width = bounds.maxY - bounds.minY + 1;
                height = bounds.maxZ - bounds.minZ + 1;
                break;
            case YZ:
                width = bounds.maxZ - bounds.minZ + 1;
                height = bounds.maxX - bounds.minX + 1;
                break;
            case XZ:
                width = bounds.maxX - bounds.minX + 1;
                height = bounds.maxZ - bounds.minZ + 1;
                break;
            default:
                width = 0;
                height = 0;
        }

        return new PortalFrameInfo(width, height, frameBlocks);
    }

    private List<BlockPos> collectFrameBlocks(BlockPos startPos, Util.Plane plane) {
        List<BlockPos> frameBlocks = new ArrayList<>();
        List<BlockPos> queue = new ArrayList<>();
        List<BlockPos> visited = new ArrayList<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.remove(0);
            BlockState state = level.getBlockState(current);
            boolean a = state == PortalControllerBlock.stateById(0);
            boolean b = state == PortalFrameBlock.stateById(0);
            boolean c = Util.isInPlane(current, plane, startPos);

            if ((a||b)&&c) {
                frameBlocks.add(current);

                for (BlockPos neighbor : Util.getNeighbors(current)) {
                    if (!visited.contains(neighbor) && level.getBlockState(neighbor) == PortalFrameBlock.stateById(0) && Util.isInPlane(neighbor, plane, startPos)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }

        return frameBlocks;
    }

    private boolean hasFullCorners(List<BlockPos> frameBlocks, Util.Plane plane, BlockPos controllerPos) {
        BoundingBox bounds = Util.getBounds(frameBlocks);
        List<BlockPos> corners = new ArrayList<>();

        switch (plane) {
            case XY:
                corners.add(new BlockPos(bounds.minX, bounds.minY, bounds.minZ));
                corners.add(new BlockPos(bounds.minX, bounds.maxY, bounds.minZ));
                corners.add(new BlockPos(bounds.maxX, bounds.minY, bounds.minZ));
                corners.add(new BlockPos(bounds.maxX, bounds.maxY, bounds.minZ));
                break;
            case YZ:
                corners.add(new BlockPos(bounds.minX, bounds.minY, bounds.minZ));
                corners.add(new BlockPos(bounds.minX, bounds.maxY, bounds.minZ));
                corners.add(new BlockPos(bounds.minX, bounds.minY, bounds.maxZ));
                corners.add(new BlockPos(bounds.minX, bounds.maxY, bounds.maxZ));
                break;
            case XZ:
                corners.add(new BlockPos(bounds.minX, bounds.minY, bounds.minZ));
                corners.add(new BlockPos(bounds.maxX, bounds.minY, bounds.minZ));
                corners.add(new BlockPos(bounds.minX, bounds.minY, bounds.maxZ));
                corners.add(new BlockPos(bounds.maxX, bounds.minY, bounds.maxZ));
                break;
        }

        for (BlockPos corner : corners) {
            if (!frameBlocks.contains(corner)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSinglePlane(List<BlockPos> frameBlocks, Util.Plane plane, BlockPos controllerPos) {
        for (BlockPos pos : frameBlocks) {
            if (!Util.isInPlane(pos, plane, controllerPos)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNoGaps(List<BlockPos> frameBlocks, Util.Plane plane, BlockPos controllerPos) {
        BoundingBox bounds = Util.getBounds(frameBlocks);

        for (int x = bounds.minX; x <= bounds.maxX; x++) {
            for (int y = bounds.minY; y <= bounds.maxY; y++) {
                for (int z = bounds.minZ; z <= bounds.maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (isBoundary(x, y, z, bounds, plane, controllerPos)) {
                        if (!frameBlocks.contains(pos)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isBoundary(int x, int y, int z, BoundingBox bounds, Util.Plane plane, BlockPos controllerPos) {
        switch (plane) {
            case XY:
                return x == bounds.minX || x == bounds.maxX || y == bounds.minY || y == bounds.maxY;
            case YZ:
                return y == bounds.minY || y == bounds.maxY || z == bounds.minZ || z == bounds.maxZ;
            case XZ:
                return x == bounds.minX || x == bounds.maxX || z == bounds.minZ || z == bounds.maxZ;
            default:
                return false;
        }
    }

    private int calculateRequiredEnergy() {
        // Scan the portal frame
        PortalFrameInfo frameInfo = scanPortalFrame();

        if (frameInfo == null) {
            return Integer.MAX_VALUE; // No valid portal found
        }

        int width = frameInfo.width;
        int height = frameInfo.height;

        // Define energy requirement, e.g., 1000 FE per block of frame
        int frameSize = 2 * (width + height); // Perimeter
        return frameSize * 1000;
    }

    public int getEnergyStored(){
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored(){
        return energyStorage.getMaxEnergyStored();
    }

    public void setEnergyStored(int value){

    }

    // Helper class to store portal frame information
    private static class PortalFrameInfo {
        int width;
        int height;
        List<BlockPos> framePositions;

        PortalFrameInfo(int width, int height, List<BlockPos> framePositions) {
            this.width = width;
            this.height = height;
            this.framePositions = framePositions;
        }
    }
}