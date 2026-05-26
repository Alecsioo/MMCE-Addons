package github.alecsio.mmceaddons.common.hatch.abyssalcraft;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentPotentialEnergyProvider extends BaseMachineComponent<RequirementPotentialEnergy> {

    public MachineComponentPotentialEnergyProvider(IOType ioType, IRequirementHandler<RequirementPotentialEnergy> handler) {
        super(ioType, handler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_POTENTIAL_ENERGY);
    }
}
