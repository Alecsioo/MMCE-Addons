package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LocalizationHelper {
    
    public static String translate(String key) {
        return I18n.format(key);
    }
}
