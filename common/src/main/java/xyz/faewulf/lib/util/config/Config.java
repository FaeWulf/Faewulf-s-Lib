package xyz.faewulf.lib.util.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.platform.Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private static boolean alreadyInit = false;

    public static final Map<String, ConfigRecord> LIST_OF_CONFIG = new HashMap<>();

    private static final Map<String, Boolean> LIST_OF_LOADED_CONFIGS = new HashMap<>();

    public static void init() {

        //prevent calling twice and so on
        if (alreadyInit)
            return;

        if (Services.PLATFORM.isDevelopmentEnvironment())
            registerConfig(Constants.MOD_ID, ModConfigs.class);

        alreadyInit = true;
    }

    private static void init(String MOD_ID) {

        // If not exist
        if (!LIST_OF_LOADED_CONFIGS.containsKey(MOD_ID))
            return;


        // If already loaded
        if (LIST_OF_LOADED_CONFIGS.get(MOD_ID))
            return;

        // Init load
        ConfigRecord configRecord = LIST_OF_CONFIG.get(MOD_ID);

        if (configRecord == null)
            return;

        ConfigLoaderFromAnnotation.initializeDefaults(configRecord);
        loadFromFile(configRecord);
        saveConfig(configRecord);
        LIST_OF_LOADED_CONFIGS.put(MOD_ID, true);
    }

    // For loading before mixin
    public static void preLoad(String MOD_ID, Class<?> modClass) {
        registerConfig(MOD_ID, modClass);
    }

    public static void registerConfig(String MOD_ID, Class<?> ModClass) {

        if (LIST_OF_CONFIG.containsKey(MOD_ID))
            return;

        LIST_OF_CONFIG.put(MOD_ID, new ConfigRecord(MOD_ID, ModClass));
        LIST_OF_LOADED_CONFIGS.put(MOD_ID, false);

        init(MOD_ID);
    }

    public static void save(String MOD_ID) {
        ConfigRecord configRecord = LIST_OF_CONFIG.get(MOD_ID);
        if (configRecord == null) {
            Constants.LOG.warn("Config of " + MOD_ID + " could not be found.");
            return;
        }
        saveConfig(configRecord);
    }

    public static void reloadConfig(String modId) {
        if (!LIST_OF_CONFIG.containsKey(modId)) {
            Constants.LOG.error("{} not valid/not found.", modId);
            return;
        }

        loadFromFile(LIST_OF_CONFIG.get(modId));
        Constants.LOG.info("Reloaded {} config file.", modId);
    }

    public static void reloadAllConfig() {
        LIST_OF_CONFIG.forEach((s, configRecord) -> {
            loadFromFile(configRecord);
            Constants.LOG.info("Reloaded {} config file.", configRecord.getName());
        });

        Constants.LOG.info("Total config reloaded: {}", LIST_OF_CONFIG.size());
    }

    private static void saveConfig(ConfigRecord configRecord) {
        try (FileWriter writer = new FileWriter(configRecord.getCONFIG_PATH())) {

            //category <configName, value>
            Map<String, Map<String, ConfigLoaderFromAnnotation.EntryInfo>> configMap = ConfigLoaderFromAnnotation.loadConfig(configRecord.getName());


            for (Map.Entry<String, Map<String, ConfigLoaderFromAnnotation.EntryInfo>> entry : configMap.entrySet()) {
                //group
                writer.write("[" + entry.getKey() + "]\n");

                List<String> groupAppeared = new ArrayList<>();

                for (Map.Entry<String, ConfigLoaderFromAnnotation.EntryInfo> subEntry : entry.getValue().entrySet()) {

                    if (!groupAppeared.contains(subEntry.getValue().group)) {
                        groupAppeared.add(subEntry.getValue().group);
                        writer.write("\n# Group: " + subEntry.getValue().group + "\n");
                    }

                    //put value
                    writer.write('"' + subEntry.getKey() + '"' + " = " + toTomlValue(subEntry.getValue().value));

                    //put info if it has info
                    if (!subEntry.getValue().info.isEmpty())
                        writer.write("\t\t\t# " + subEntry.getValue().info);

                    //breakline
                    writer.write("\n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toTomlValue(Object value) {
        //string
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        //int and bool
        else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        }
        //enum
        else if (value.getClass().isEnum()) {
            return "\"" + value + "\"";
        }
        return "";
    }

    // This will load config value from file and update to the ModConfig class
    private static void loadFromFile(ConfigRecord configRecord) {

        if (Files.exists(Path.of(configRecord.getCONFIG_PATH_OLD()))) {
            Constants.LOG.warn("Detected the old configuration file '{}'. The mod {} has transitioned to using '{}' for configuration. Sorry for the inconvenient!", configRecord.getCONFIG_PATH_OLD(), configRecord.getName(), configRecord.getCONFIG_PATH());
        }

        try (FileConfig reader = FileConfig.of(configRecord.getCONFIG_PATH())) {

            //create directory if doesn't exist
            File dir = new File("config");
            if (!dir.exists())
                dir.mkdir();

            // Parse the TOML file
            reader.load();

            // Iterate through the fields in the class
            for (Field field : configRecord.getConfigClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Entry.class)) {
                    Entry entry = field.getAnnotation(Entry.class);
                    String category = entry.category();
                    String name = entry.name();

                    // Retrieve the value from the TOML file
                    Object value = reader.get(category + "." + name);

                    // Validate and assign the value
                    if (value != null && isValidType(field, value)) {
                        field.set(null, convertValue(field.getType(), value));
                    } else {
                        Constants.LOG.warn("Invalid type for config '{}' in category '{}'. Override with default value...", name, category);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if the value type matches the field's type
    private static boolean isValidType(Field field, Object value) {
        Class<?> fieldType = field.getType();
        if (fieldType.isAssignableFrom(value.getClass())) {
            return true;
        }
        if (fieldType == int.class && value instanceof Number) {
            return true;
        } else if (fieldType == boolean.class && value instanceof Boolean) {
            return true;
        } else if (fieldType.isEnum() && value instanceof String) {
            try {
                Enum.valueOf((Class<Enum>) fieldType, (String) value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    // Helper method to convert values to their appropriate types
    private static Object convertValue(Class<?> fieldType, Object value) {
        if (fieldType == int.class && value instanceof Number) {
            return ((Number) value).intValue();
        } else if (fieldType == boolean.class && value instanceof Boolean) {
            return value;
        } else if (fieldType.isEnum() && value instanceof String) {
            return Enum.valueOf((Class<Enum>) fieldType, (String) value);
        }
        return value;
    }
}
