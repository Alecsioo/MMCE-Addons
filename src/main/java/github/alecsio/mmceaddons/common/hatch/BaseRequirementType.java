package github.alecsio.mmceaddons.common.hatch;

import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;

import javax.annotation.Nullable;

public abstract class BaseRequirementType<T, V extends ComponentRequirement<T, ? extends RequirementType<T, V>>> extends RequirementType<T, V> {

    @Nullable
    @Override
    public String requiresModid() {
        RequiresMod annotation = this.getClass().getAnnotation(RequiresMod.class);
        return (annotation != null) ? annotation.value() : null;
    }
}
