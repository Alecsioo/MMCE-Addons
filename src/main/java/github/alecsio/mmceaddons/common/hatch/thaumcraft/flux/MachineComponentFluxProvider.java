package github.alecsio.mmceaddons.common.hatch.thaumcraft.flux;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentFluxProvider extends BaseMachineComponent<RequirementFlux> {

    public MachineComponentFluxProvider(IRequirementHandler<RequirementFlux> fluxProvider, IOType ioType) {
        super(ioType, fluxProvider);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_FLUX);
    }
}
