package xyz.faewulf.lib.util.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigRecord {
    private final String name;
    private final String CONFIG_PATH;
    private final String CONFIG_PATH_OLD;
    private final Class<?> CONFIG_CLASS;

    private final Map<String, Object> DEFAULT_VALUES = new LinkedHashMap<>();
    private final Map<String, List<String>> GROUP_OF_CATEGORY = new HashMap<>();

    public ConfigRecord(String MOD_ID, Class<?> ConfigClass) {
        this.name = MOD_ID;
        this.CONFIG_CLASS = ConfigClass;
        this.CONFIG_PATH = "config/" + name + ".toml";
        this.CONFIG_PATH_OLD = "config/" + name + ".json";
    }

    public Map<String, List<String>> getGROUP_OF_CATEGORY() {
        return GROUP_OF_CATEGORY;
    }

    public Map<String, Object> getDEFAULT_VALUES() {
        return DEFAULT_VALUES;
    }

    public Class<?> getConfigClass() {
        return CONFIG_CLASS;
    }

    public String getCONFIG_PATH() {
        return CONFIG_PATH;
    }

    public String getName() {
        return name;
    }

    public String getCONFIG_PATH_OLD() {
        return CONFIG_PATH_OLD;
    }
}
