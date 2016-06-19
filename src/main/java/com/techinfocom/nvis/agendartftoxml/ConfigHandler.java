package com.techinfocom.nvis.agendartftoxml;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by volkov_kv on 19.06.2016.
 */
public class ConfigHandler {
    private Config conf;
    private static ConfigHandler ourInstance = new ConfigHandler();

    public static ConfigHandler getInstance() {
        return ourInstance;
    }

    private ConfigHandler() {
        conf = ConfigFactory.load();
    }

    public Config getValidationRules() {
        return conf.getConfig("validationRules");
    }
}
