package me.ramidzkh.ping.extension;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.util.math.BlockPos;

public interface ClientWorldExtension {

    void addPing(BlockPos pos);

    Object2LongMap<BlockPos> getPings();
}
