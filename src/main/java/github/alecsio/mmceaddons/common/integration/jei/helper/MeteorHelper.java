package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Meteor;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MeteorHelper extends BaseIngredientHelper<Meteor> {

    @Override
    public String getUniqueId(Meteor meteor) {
        return "Meteor";
    }

    @Override
    public String getResourceId(Meteor meteor) {
        return getUniqueId(meteor);
    }

    @Override
    public Meteor copyIngredient(Meteor meteor) {
        // Yes, I know, the components and so on...
        return new Meteor(meteor.getCatalystStack(), meteor.getComponents(), meteor.getExplosionStrength(), meteor.getRadius());
    }
}
