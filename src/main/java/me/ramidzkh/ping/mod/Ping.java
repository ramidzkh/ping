package me.ramidzkh.ping.mod;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface Ping {

    long PING_TIME = 10000;

    Identifier PING = new Identifier("ping", "ping");
    SoundEvent PING_SOUND = new SoundEvent(PING);

    static void initialize() {
        Registry.register(Registry.SOUND_EVENT, PING, PING_SOUND);

        ServerSidePacketRegistry.INSTANCE.register(PING, (context, packet) -> {
            PlayerEntity sender = context.getPlayer();
            ((ServerChunkManager) sender.world.getChunkManager()).sendToNearbyPlayers(sender, ServerSidePacketRegistry.INSTANCE.toPacket(PING, new PacketByteBuf(packet.copy())));
        });
    }
}
