package github.alecsio.mmceaddons.common.hatch.handler;

import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;

public interface IRequirementHandler<T> {
    CraftCheck canHandle(T requirement);
    void handle(T requirement);
}
