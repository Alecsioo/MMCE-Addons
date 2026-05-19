package github.alecsio.mmceaddons.common.hatch.bloodmagic;

import github.alecsio.mmceaddons.common.hatch.BaseProviderCopy;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;

public class MeteorProviderCopy extends BaseProviderCopy<RequirementMeteor> implements IRequirementHandler<RequirementMeteor> {

    private boolean reserved;

    public MeteorProviderCopy(IRequirementHandler<RequirementMeteor> original) {
        super(original);
        this.reserved = false;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void reserve() {
        reserved = true;
    }

    @Override
    public CraftCheck canHandle(RequirementMeteor requirement) {
        if (isReserved()) {return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.meteor.missing");}

        CraftCheck check = original.canHandle(requirement);

        if (check.isSuccess()) {
            reserve();
        }
        return check;
    }

    @Override
    public void handle(RequirementMeteor requirement) {
        original.handle(requirement);
    }
}
