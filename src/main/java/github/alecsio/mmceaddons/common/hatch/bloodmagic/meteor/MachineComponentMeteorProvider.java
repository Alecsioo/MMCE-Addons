package github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor;

import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentMeteorProvider extends BaseMachineComponent<RequirementMeteor> {

    public MachineComponentMeteorProvider(IOType ioType, IRequirementHandler<RequirementMeteor> meteorHandler) {
        super(ioType, meteorHandler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_METEOR);
    }
}
