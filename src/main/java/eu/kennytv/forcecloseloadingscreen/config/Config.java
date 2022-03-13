package eu.kennytv.forcecloseloadingscreen.config;

import me.shedaniel.autoconfig.ConfigData;

@me.shedaniel.autoconfig.annotation.Config(name = "forcecloseloadingscreen")
public class Config implements ConfigData {

    boolean instantlyCloseUnsafe;

    public boolean isInstantlyCloseUnsafe() {
        return instantlyCloseUnsafe;
    }
}