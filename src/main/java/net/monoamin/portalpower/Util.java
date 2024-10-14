package net.monoamin.portalpower;

import net.minecraft.core.BlockPos;
import net.monoamin.portalpower.BoundingBox;
import java.util.List;

public class Util {
    public enum Plane {
        XY, YZ, XZ
    }

    public static List<BlockPos> getNeighbors(BlockPos pos) {
        return List.of(
                pos.above(),
                pos.below(),
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west()
        );
    }

    public static Plane determinePlane(BlockPos controllerPos, BlockPos adjacentPos) {
        int dx = adjacentPos.getX() - controllerPos.getX();
        int dy = adjacentPos.getY() - controllerPos.getY();
        int dz = adjacentPos.getZ() - controllerPos.getZ();

        if (dx != 0) {
            return Plane.YZ;
        } else if (dy != 0) {
            return Plane.XZ;
        } else if (dz != 0) {
            return Plane.XY;
        }
        return null; // Undefined plane
    }

    public static boolean isInPlane(BlockPos pos, Plane plane, BlockPos controllerPos) {
        if (pos == controllerPos) return true;
        switch (plane) {
            case XY:
                return pos.getZ() == controllerPos.getZ();
            case YZ:
                return pos.getX() == controllerPos.getX();
            case XZ:
                return pos.getY() == controllerPos.getY();
            default:
                return false;
        }
    }

    public static BoundingBox getBounds(List<BlockPos> blocks) {
        int minX = blocks.stream().mapToInt(BlockPos::getX).min().orElse(0);
        int minY = blocks.stream().mapToInt(BlockPos::getY).min().orElse(0);
        int minZ = blocks.stream().mapToInt(BlockPos::getZ).min().orElse(0);
        int maxX = blocks.stream().mapToInt(BlockPos::getX).max().orElse(0);
        int maxY = blocks.stream().mapToInt(BlockPos::getY).max().orElse(0);
        int maxZ = blocks.stream().mapToInt(BlockPos::getZ).max().orElse(0);
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

}
