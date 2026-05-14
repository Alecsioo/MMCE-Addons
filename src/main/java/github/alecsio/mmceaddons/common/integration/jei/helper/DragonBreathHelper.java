package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.DragonBreath;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DragonBreathHelper extends BaseIngredientHelper<DragonBreath> {

    @Override
    public String getUniqueId(DragonBreath dragonBreath) {
        return String.valueOf(dragonBreath.getAmount()) + '-' + dragonBreath.getAmount();
    }

    @Override
    public String getResourceId(DragonBreath dragonBreath) {
        return getUniqueId(dragonBreath);
    }

    @Override
    public DragonBreath copyIngredient(DragonBreath dragonBreath) {
        return new DragonBreath(dragonBreath.getDragonType(), dragonBreath.getAmount());
    }
}
