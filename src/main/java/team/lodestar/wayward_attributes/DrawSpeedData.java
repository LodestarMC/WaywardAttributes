package team.lodestar.wayward_attributes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class DrawSpeedData {

    public static final Codec<DrawSpeedData> CODEC = RecordCodecBuilder.create(obj -> obj.group(
            Codec.FLOAT.fieldOf("charges").forGetter(c -> c.partialUseTicks)
    ).apply(obj, DrawSpeedData::new));

    public static final StreamCodec<ByteBuf, DrawSpeedData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    private float partialUseTicks;

    public DrawSpeedData() {
    }

    public DrawSpeedData(float partialUseTicks) {
        this.partialUseTicks = partialUseTicks;
    }

    public int increment(float drawSpeed) {
        partialUseTicks += drawSpeed;
        int progress = 0;
        while (partialUseTicks >= 1) {
            partialUseTicks--;
            progress++;
        }
        return progress;
    }
}
