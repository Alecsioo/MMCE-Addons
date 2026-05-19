package github.alecsio.mmceaddons.common.hatch.bloodmagic;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponentCopy;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentMeteorProvider extends BaseMachineComponentCopy<RequirementMeteor> {

    public MachineComponentMeteorProvider(IOType ioType, IRequirementHandler<RequirementMeteor> meteorHandler) {
        super(ioType, new MeteorProviderCopy(meteorHandler));
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_METEOR);
    }
}
