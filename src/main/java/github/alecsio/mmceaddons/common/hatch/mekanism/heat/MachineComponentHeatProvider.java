package github.alecsio.mmceaddons.common.hatch.mekanism.heat;

import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentHeatProvider extends BaseMachineComponent<RequirementHeat> {

    public MachineComponentHeatProvider(IOType ioType, IRequirementHandler<RequirementHeat> handler) {
        super(ioType, handler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_HEAT);
    }
}
