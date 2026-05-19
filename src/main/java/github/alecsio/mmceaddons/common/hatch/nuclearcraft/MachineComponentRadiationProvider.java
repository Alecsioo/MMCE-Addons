package github.alecsio.mmceaddons.common.hatch.nuclearcraft;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentRadiationProvider extends BaseMachineComponent<RequirementRadiation> {

    public MachineComponentRadiationProvider(IOType ioType, IRequirementHandler<RequirementRadiation> radiationHandler) {
        super(ioType, radiationHandler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_RADIATION);
    }
}
