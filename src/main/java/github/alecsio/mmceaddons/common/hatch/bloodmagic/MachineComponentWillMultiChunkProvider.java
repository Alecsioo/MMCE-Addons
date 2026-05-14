package github.alecsio.mmceaddons.common.hatch.bloodmagic;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentWillMultiChunkProvider extends BaseMachineComponent<RequirementWillMultiChunk> {

    public MachineComponentWillMultiChunkProvider(IOType ioType, IRequirementHandler<RequirementWillMultiChunk> willHandler) {
        super(ioType, willHandler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_WILL);
    }
}
