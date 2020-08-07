package me.ramidzkh.ping.mixin;

import me.ramidzkh.ping.extension.EntityExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "method_27022", at = @At("HEAD"), cancellable = true)
    private void shouldGlow(Entity entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (entity != null && ((EntityExtension) entity).isPingGlowing()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}
