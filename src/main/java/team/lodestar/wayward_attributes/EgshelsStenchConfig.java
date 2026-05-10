package team.lodestar.wayward_attributes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import team.lodestar.lodestone.modules.core.config.LodestoneConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EgshelsStenchConfig extends LodestoneConfig {

    public static ConfigValueHolder<String> POSITIONS = new ConfigValueHolder<>(WaywardAttributes.MODID, "egshels/stench/helcraft/whatever", (builder ->
            builder.comment("The positions.")
                    .define("ender_eye_positions", "[0,0,0], [100, 0, 0]")));


    public EgshelsStenchConfig(ModConfigSpec.Builder builder) {
        super(WaywardAttributes.MODID, "egshels", builder);
    }

    public static final EgshelsStenchConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        final Pair<EgshelsStenchConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(EgshelsStenchConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    public static List<BlockPos> parseBlockPosList() {
        List<BlockPos> positions = new ArrayList<>();

        // Matches: [x,y,z]
        Pattern pattern = Pattern.compile("\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*]");
        Matcher matcher = pattern.matcher(POSITIONS.getConfigValue());

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int z = Integer.parseInt(matcher.group(3));

            positions.add(new BlockPos(x, y, z));
        }

        return positions;
    }
}
