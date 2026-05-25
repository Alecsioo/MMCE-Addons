package github.alecsio.mmceaddons.common.integration.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BaseInfoProvider<T extends TileEntity> implements IProbeInfoProvider {

    private final Class<T> tileClass;

    public BaseInfoProvider(Class<T> tileClass) {
        this.tileClass = tileClass;
    }

    protected abstract String getName();
    protected abstract void addProbeInfo(IProbeInfo iProbeInfo, T hatch);

    @Override
    public String getID() {
        return String.format("modularmachineryaddons.%s_info_provider", getName());
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData) {
        if (!iBlockState.getBlock().hasTileEntity(iBlockState)) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(iProbeHitData.getPos());
        if (!tileClass.isInstance(tileEntity)) {
            return;
        }

        T typedTile = tileClass.cast(tileEntity);

        addProbeInfo(iProbeInfo, typedTile);
    }

    protected String wrapInLoc(String string) {
        return IProbeInfo.STARTLOC + string + IProbeInfo.ENDLOC;
    }
}
