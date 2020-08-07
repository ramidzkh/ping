package me.ramidzkh.ping.mixin;

import me.ramidzkh.ping.extension.EntityExtension;
import me.ramidzkh.ping.mod.Ping;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin implements EntityExtension {

    private long glowingTime;

    @Override
    public void startPingGlowing() {
        glowingTime = System.currentTimeMillis() + Ping.PING_TIME;
    }

    @Override
    public boolean isPingGlowing() {
        return glowingTime > System.currentTimeMillis();
    }
}
