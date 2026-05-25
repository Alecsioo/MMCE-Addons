package github.alecsio.mmceaddons.common.hatch;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;

import javax.annotation.Nullable;

public abstract class BaseComponent extends ComponentType {

    @Nullable
    @Override
    public String requiresModid() {
        RequiresMod annotation = this.getClass().getAnnotation(RequiresMod.class);
        return (annotation != null) ? annotation.value() : null;
    }
}
