package github.alecsio.mmceaddons.common.hatch.vanilla;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentDimensionProvider extends BaseMachineComponent<RequirementDimension> {

    public MachineComponentDimensionProvider(IOType ioType, IRequirementHandler<RequirementDimension> dimensionHandler) {
        super(ioType, dimensionHandler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_DIMENSION);
    }
}
