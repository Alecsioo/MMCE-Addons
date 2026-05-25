package github.alecsio.mmceaddons.common.hatch.iceandfire;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

@MethodsReturnNonnullByDefault
public enum DragonType implements IStringSerializable {
    EMPTY,
    ICE,
    FIRE,
    LIGHTNING,;

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }
}
