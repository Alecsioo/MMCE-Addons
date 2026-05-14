package github.alecsio.mmceaddons.common.hatch.iceandfire;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentDragonBreathProvider extends BaseMachineComponent<RequirementDragonBreath> {

    public MachineComponentDragonBreathProvider(IOType ioType, IRequirementHandler<RequirementDragonBreath> handler) {
        super(ioType, handler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_DRAGON_BREATH);
    }
}
