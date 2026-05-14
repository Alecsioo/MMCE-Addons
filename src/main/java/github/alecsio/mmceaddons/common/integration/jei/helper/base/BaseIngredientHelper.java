package github.alecsio.mmceaddons.common.integration.jei.helper.base;

import com.google.common.collect.Iterables;
import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BaseIngredientHelper<T extends IRequiresEquals<T>> implements IIngredientHelper<T> {

    @Nullable
    @Override
    public T getMatch(Iterable<T> ingredients, T toMatch) {
        if(Iterables.isEmpty(ingredients)) {return null;}

        for (T ingredient : ingredients) {
            if (ingredient.equalsTo(toMatch)) {return ingredient;}
        }
        return null;
    }

    @Override
    public String getDisplayName(T ingredient) {
        return ingredient.getClass().getSimpleName();
    }

    @Override
    public String getWildcardId(T t) {
        return getUniqueId(t);
    }

    @Override
    public String getModId(T t) {
        return ModularMachineryAddons.MODID;
    }

    @Override
    public String getErrorInfo(@Nullable T t) {
        return "Encountered an error with ingredient";
    }
}
