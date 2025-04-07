package github.alecsio.mmceaddons.common.tile.machinecomponent.base;

import github.alecsio.mmceaddons.common.crafting.helper.BaseProviderCopy;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

public abstract class BaseMachineComponentCopy<T> extends MachineComponent<BaseProviderCopy<T>> {

    private final BaseProviderCopy<T> handlerCopy;

    public BaseMachineComponentCopy(IOType ioType, BaseProviderCopy<T> handlerCopy) {
        super(ioType);
        this.handlerCopy = handlerCopy;
    }

    @Override
    public BaseProviderCopy<T> getContainerProvider() {
        return handlerCopy;
    }
}
