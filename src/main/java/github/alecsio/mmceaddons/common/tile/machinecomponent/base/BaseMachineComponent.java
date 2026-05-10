package github.alecsio.mmceaddons.common.tile.machinecomponent.base;

import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

public abstract class BaseMachineComponent<T> extends MachineComponent<IRequirementHandler<T>> {

    private final IRequirementHandler<T> handler;

    public BaseMachineComponent(IOType ioType, IRequirementHandler<T> handler) {
        super(ioType);
        this.handler = handler;
    }

    @Override
    public IRequirementHandler<T> getContainerProvider() {
        return handler;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }
}
