package github.alecsio.mmceaddons.common.hatch.mekanism.laser;

import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentLaserProvider extends BaseMachineComponent<RequirementLaser> {

    public MachineComponentLaserProvider(IOType ioType, IRequirementHandler<RequirementLaser> handler) {
        super(ioType, handler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_LASER);
    }
}
