package github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.BaseMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;

public class MachineComponentEssentiaProvider extends BaseMachineComponent<RequirementEssentia> {

    public MachineComponentEssentiaProvider(IOType ioType, IRequirementHandler<RequirementEssentia> essentiaHandler) {
        super(ioType, essentiaHandler);
    }

    @Override
    public ComponentType getComponentType() {
        return ModularMachineryAddonsComponents.COMPONENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsComponents.KEY_COMPONENT_ESSENTIA);
    }
}
