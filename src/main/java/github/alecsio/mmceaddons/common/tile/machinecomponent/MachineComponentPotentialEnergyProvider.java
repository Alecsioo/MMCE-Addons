package github.alecsio.mmceaddons.common.tile.machinecomponent;

import github.alecsio.mmceaddons.common.crafting.component.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.crafting.requirement.abyssalcraft.RequirementPotentialEnergy;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.base.BaseMachineComponent;
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
