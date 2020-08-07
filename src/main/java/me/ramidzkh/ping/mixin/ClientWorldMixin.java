package me.ramidzkh.ping.mixin;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import me.ramidzkh.ping.extension.ClientWorldExtension;
import me.ramidzkh.ping.mod.Ping;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements ClientWorldExtension {

    private final Object2LongMap<BlockPos> pings = new Object2LongOpenHashMap<>();

    @Override
    public void addPing(BlockPos pos) {
        pings.put(pos, System.currentTimeMillis() + Ping.PING_TIME);
    }

    @Override
    public Object2LongMap<BlockPos> getPings() {
        return pings;
    }
}
