package eu.kennytv.forcecloseloadingscreen;

import eu.kennytv.forcecloseloadingscreen.config.Config;
import eu.kennytv.forcecloseloadingscreen.config.ConfigHolder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class ForceCloseLoadingScreenMod implements ModInitializer {

    private static boolean hasClothConfig; // (▀̿Ĺ̯▀̿ ̿)

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer("cloth-config").ifPresent(mod -> {
            AutoConfig.register(Config.class, GsonConfigSerializer::new);
            ConfigHolder.config = AutoConfig.getConfigHolder(Config.class).getConfig();
            hasClothConfig = true;
        });
    }

    public static boolean isInstantClose() {
        return hasClothConfig && ConfigHolder.config.isInstantlyCloseUnsafe();
    }
}
