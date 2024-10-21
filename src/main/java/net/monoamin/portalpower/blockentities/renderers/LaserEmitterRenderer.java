package net.monoamin.portalpower.blockentities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.monoamin.portalpower.blockentities.LaserEmitterBlockEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class LaserEmitterRenderer implements BlockEntityRenderer<LaserEmitterBlockEntity> {

    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("portalpower", "textures/entity/laser_beam.png");

    public LaserEmitterRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(LaserEmitterBlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        long time = blockEntity.getLevel().getGameTime();
        List<LaserEmitterBlockEntity.BeaconBeamSection> list = blockEntity.getBeamSections();
        int totalHeight = 0;

        // Get the block's facing direction to rotate the beam
        Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
        float rotationY = getRotationYFromFacing(facing);
        float rotationX = getRotationXFromFacing(facing);

        for (int sectionNumber = 0; sectionNumber < list.size(); ++sectionNumber) {
            LaserEmitterBlockEntity.BeaconBeamSection beamSection = list.get(sectionNumber);
            renderBeaconBeam(matrixStack, buffer, partialTicks, time, totalHeight,
                    sectionNumber == list.size() - 1 ? 1024 : beamSection.getHeight(),
                    beamSection.getColor(), rotationY, rotationX);
            totalHeight += beamSection.getHeight();
        }
    }

    private static float getRotationYFromFacing(Direction facing) {
        return switch (facing) {
            case NORTH -> 0.0F;
            case EAST -> 270.0F;
            case SOUTH -> 180.0F;
            case WEST -> 90.0F;
            default -> 0.0F;
        };
    }

    private static float getRotationXFromFacing(Direction facing) {
        return switch (facing) {
            case UP -> 0.0F;
            case DOWN -> 180.0F;
            default -> 90.0F;
        };
    }

    private static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource bufferSource, float maybePartialTick,
                                         long time, int totalHeight, int height, float[] tint, float rotationY, float rotationX) {
        renderBeaconBeam(poseStack, bufferSource, BEAM_LOCATION, maybePartialTick, 1.0F, time, totalHeight, height,
                tint, 0.1F, 0.15F, rotationY, rotationX);
    }

    public static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation beamImageLocation,
                                        float maybePartialTick, float unknownFloat1, long time, int totalHeight, int height, float[] tint,
                                        float unknownFloat2, float unknownFloat3, float rotationY, float rotationX) {
        int i = totalHeight + height;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY)); // Rotate the beam according to the facing direction on Y-axis
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX)); // Rotate the beam according to the facing direction on X-axis
        float rotationAmount = (float) Math.floorMod(time, 40) + maybePartialTick;
        float f1 = height < 0 ? rotationAmount : -rotationAmount;
        float f2 = Mth.frac(f1 * 0.2F - (float) Mth.floor(f1 * 0.1F));
        float f3 = tint[0];
        float f4 = tint[1];
        float f5 = tint[2];
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationAmount * 2.25F - 45.0F));
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = -unknownFloat2;
        float f12 = -unknownFloat2;
        float f15 = -1.0F + f2;
        float f16 = (float) height * unknownFloat1 * (0.5F / unknownFloat2) + f15;
        renderPart(poseStack, bufferSource.getBuffer(RenderType.beaconBeam(beamImageLocation, false)), f3, f4, f5, 1.0F, totalHeight,
                i, 0.0F, unknownFloat2, unknownFloat2, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
        poseStack.popPose();
        f6 = -unknownFloat3;
        float f7 = -unknownFloat3;
        f8 = -unknownFloat3;
        f9 = -unknownFloat3;
        f15 = -1.0F + f2;
        f16 = (float) height * unknownFloat1 + f15;
        renderPart(poseStack, bufferSource.getBuffer(RenderType.beaconBeam(beamImageLocation, true)), f3, f4, f5, 0.125F,
                totalHeight, i, f6, f7, unknownFloat3, f8, f9, unknownFloat3, unknownFloat3, unknownFloat3, 0.0F, 1.0F, f16, f15);
        poseStack.popPose();
    }

    private static void renderPart(PoseStack p_112156_, VertexConsumer p_112157_, float p_112158_, float p_112159_,
                                   float p_112160_, float p_112161_, int p_112162_, int p_112163_, float p_112164_, float p_112165_,
                                   float p_112166_, float p_112167_, float p_112168_, float p_112169_, float p_112170_, float p_112171_,
                                   float p_112172_, float p_112173_, float p_112174_, float p_112175_) {
        PoseStack.Pose posestack$pose = p_112156_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_,
                p_112164_, p_112165_, p_112166_, p_112167_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_,
                p_112170_, p_112171_, p_112168_, p_112169_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_,
                p_112166_, p_112167_, p_112170_, p_112171_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_,
                p_112168_, p_112169_, p_112164_, p_112165_, p_112172_, p_112173_, p_112174_, p_112175_);
    }

    private static void renderQuad(Matrix4f p_253960_, Matrix3f p_254005_, VertexConsumer p_112122_, float p_112123_,
                                   float p_112124_, float p_112125_, float p_112126_, int p_112127_, int p_112128_, float p_112129_,
                                   float p_112130_, float p_112131_, float p_112132_, float p_112133_, float p_112134_, float p_112135_,
                                   float p_112136_) {
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112128_, p_112129_,
                p_112130_, p_112134_, p_112135_);
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112127_, p_112129_,
                p_112130_, p_112134_, p_112136_);
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112127_, p_112131_,
                p_112132_, p_112133_, p_112136_);
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112128_, p_112131_,
                p_112132_, p_112133_, p_112135_);
    }

    private static void addVertex(Matrix4f p_253955_, Matrix3f p_253713_, VertexConsumer p_253894_, float p_253871_,
                                  float p_253841_, float p_254568_, float p_254361_, int p_254357_, float p_254451_, float p_254240_,
                                  float p_254117_, float p_253698_) {
        p_253894_.vertex(p_253955_, p_254451_, (float) p_254357_, p_254240_)
                .color(p_253871_, p_253841_, p_254568_, p_254361_).uv(p_254117_, p_253698_)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_253713_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public boolean shouldRenderOffScreen(LaserEmitterBlockEntity blockEntity) {
        return true; // To allow the beam to be visible even from a distance
    }

    @Override
    public boolean shouldRender(LaserEmitterBlockEntity blockEntity, Vec3 cameraPos) {
        return true; // Adjust if you need to check when it should render
    }
}
