package me.ramidzkh.ping.mixin;

import me.ramidzkh.ping.extension.ClientWorldExtension;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    private final List<Entity> entities = new ArrayList<>();

    @Shadow
    private ClientWorld world;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo callbackInfo) {
        ((ClientWorldExtension) world).getPings().forEach((pos, time) -> {
            if (System.currentTimeMillis() < time) {
                ShulkerEntity entity = new ShulkerEntity(EntityType.SHULKER, world);
                entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                entity.setInvisible(true);
                entity.setGlowing(true);

                world.addEntity(entity.getEntityId(), entity);
                entities.add(entity);
            }
        });
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void postRender(CallbackInfo callbackInfo) {
        entities.removeIf(entity -> {
            world.removeEntity(entity.getEntityId());
            return true;
        });
    }
}
