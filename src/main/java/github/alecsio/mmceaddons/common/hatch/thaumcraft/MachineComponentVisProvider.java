package github.alecsio.mmceaddons.common.hatch.thaumcraft;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentVisProvider extends BaseMachineComponent<RequirementVis> {

    public MachineComponentVisProvider(IOType ioType, IRequirementHandler<RequirementVis> visHandler) {
        super(ioType, visHandler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_VIS);
    }
}
