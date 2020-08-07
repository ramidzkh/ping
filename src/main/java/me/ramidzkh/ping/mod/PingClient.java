package me.ramidzkh.ping.mod;

import io.netty.buffer.Unpooled;
import me.ramidzkh.ping.extension.ClientWorldExtension;
import me.ramidzkh.ping.extension.EntityExtension;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

import static me.ramidzkh.ping.mod.Ping.PING;

public interface PingClient {

    static void initializeClient() {
        KeyBinding ping = new KeyBinding(
                "ping.ping",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "Utility"
        );

        KeyBindingHelper.registerKeyBinding(ping);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Entity cameraEntity = client.cameraEntity;
            World world = client.world;

            if (cameraEntity != null && world != null && ping.wasPressed()) {
                Optional<Entity> optional = DebugRenderer.getTargetedEntity(cameraEntity, 256);

                if (optional.isPresent()) {
                    PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                    packet.writeBoolean(false);
                    packet.writeVarInt(optional.get().getEntityId());
                    ClientSidePacketRegistry.INSTANCE.sendToServer(PING, packet);
                } else {
                    HitResult blockHit = cameraEntity.rayTrace(256, 0, false);

                    if (blockHit instanceof BlockHitResult && blockHit.getType() == HitResult.Type.BLOCK) {
                        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                        packet.writeBoolean(true);
                        packet.writeBlockPos(((BlockHitResult) blockHit).getBlockPos());
                        ClientSidePacketRegistry.INSTANCE.sendToServer(PING, packet);
                    }
                }
            }
        });

        ClientSidePacketRegistry.INSTANCE.register(PING, (context, packet) -> {
            PlayerEntity player = context.getPlayer();
            World world = player.world;
            double x, y, z;

            if (packet.readBoolean()) {
                BlockPos pos = packet.readBlockPos();
                x = pos.getX();
                y = pos.getY();
                z = pos.getZ();

                ((ClientWorldExtension) player.getEntityWorld()).addPing(pos);
            } else {
                Entity entity = world.getEntityById(packet.readVarInt());

                if (entity != null) {
                    ((EntityExtension) entity).startPingGlowing();
                    Vec3d pos = entity.getPos();

                    x = pos.x;
                    y = pos.y;
                    z = pos.z;
                } else {
                    return;
                }
            }

            world.playSound(player, x, y, z, Ping.PING_SOUND, SoundCategory.PLAYERS, 1F, 1F);
        });
    }
}
