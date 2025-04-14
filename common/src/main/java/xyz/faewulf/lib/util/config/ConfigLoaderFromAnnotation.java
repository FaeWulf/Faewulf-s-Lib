package xyz.faewulf.lib.util.config;

import java.lang.reflect.Field;
import java.util.*;

public class ConfigLoaderFromAnnotation {

    public static Map<String, Map<String, EntryInfo>> loadConfig(String MOD_ID) {

        ConfigRecord configRecord = Config.LIST_OF_CONFIG.get(MOD_ID);

        if (configRecord == null)
            return new HashMap<>();

        Map<String, Map<String, EntryInfo>> configMap = new LinkedHashMap<>();

        Field[] fields = configRecord.getConfigClass().getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        // Iterate over all fields in the config class
        for (Field field : fields) {
            if (field.isAnnotationPresent(Entry.class)) {
                Entry entry = field.getAnnotation(Entry.class);

                String category = entry.category();
                String name = entry.name();
                String info = entry.info();
                boolean hidden = entry.hidden();
                boolean require_restart = entry.require_restart();
                String group = entry.group();

                // Add the field's value to the map
                try {

                    Object value = field.get(null); // Access static field value

                    // Entry result
                    EntryInfo entryInfo = new EntryInfo(field, field.getName(), name, info, value, require_restart, hidden, group);

                    // For slider annotation
                    SliderEntry sliderAnno = field.getAnnotation(SliderEntry.class);
                    if (sliderAnno != null) {
                        if (field.getType() != int.class) {
                            throw new IllegalArgumentException("@SliderEntry can only be applied to int fields: " + field.getName());
                        }

                        int min = sliderAnno.min();
                        int max = sliderAnno.max();
                        int step = sliderAnno.step();

                        entryInfo.setSlider(new SliderInfo(min, max, step));
                    }

                    // If category map doesn't exist, create it
                    configMap.computeIfAbsent(category, k -> new LinkedHashMap<>());
                    configMap.get(category).put(name, entryInfo);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return configMap;
    }

    public static Map<String, EntryInfo> loadConfig_EntryOnly(String MOD_ID) {

        ConfigRecord configRecord = Config.LIST_OF_CONFIG.get(MOD_ID);

        if (configRecord == null)
            return new HashMap<>();

        Map<String, EntryInfo> configMap = new LinkedHashMap<>();

        Field[] fields = configRecord.getConfigClass().getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        // Iterate over all fields in the config class
        for (Field field : fields) {
            if (field.isAnnotationPresent(Entry.class)) {
                Entry entry = field.getAnnotation(Entry.class);

                String name = entry.name();
                String info = entry.info();
                boolean hidden = entry.hidden();
                boolean require_restart = entry.require_restart();
                String group = entry.group();

                // Add the field's value to the map
                try {

                    Object value = field.get(null); // Access static field value
                    EntryInfo entryInfo = new EntryInfo(field, field.getName(), name, info, value, require_restart, hidden, group);

                    // For slider annotation
                    SliderEntry sliderAnno = field.getAnnotation(SliderEntry.class);
                    if (sliderAnno != null) {
                        if (field.getType() != int.class) {
                            throw new IllegalArgumentException("@SliderEntry can only be applied to int fields: " + field.getName());
                        }

                        int min = sliderAnno.min();
                        int max = sliderAnno.max();
                        int step = sliderAnno.step();

                        entryInfo.setSlider(new SliderInfo(min, max, step));
                    }

                    // If category map doesn't exist, create it
                    configMap.put(name, entryInfo);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return configMap;
    }

    // Method to retrieve the default value by config name
    public static Object getDefaultValue(String MOD_ID, String configName) {
        ConfigRecord configRecord = Config.LIST_OF_CONFIG.get(MOD_ID);
        if (configRecord == null)
            return null;

        return configRecord.getDEFAULT_VALUES().get(configName);
    }

    // Method to retrieve all default values
    public static Map<String, Object> getAllDefaultValues(String MOD_ID) {
        ConfigRecord configRecord = Config.LIST_OF_CONFIG.get(MOD_ID);
        if (configRecord == null)
            return new HashMap<>();

        return configRecord.getDEFAULT_VALUES();
    }

    public static List<String> getGroups(String MOD_ID, String category) {

        ConfigRecord configRecord = Config.LIST_OF_CONFIG.get(MOD_ID);

        if (configRecord == null)
            return new ArrayList<>();

        if (!configRecord.getGROUP_OF_CATEGORY().containsKey(category))
            return new ArrayList<>();

        return configRecord.getGROUP_OF_CATEGORY().get(category);
    }

    // Method to initialize and store default values from the config class
    public static void initializeDefaults(ConfigRecord configRecord) {
        try {
            for (Field field : configRecord.getConfigClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Entry.class)) {
                    // Capture the field's current (default) value
                    Object defaultValue = field.get(null);


                    // Store the default value in the map
                    configRecord.getDEFAULT_VALUES().put(field.getName(), defaultValue);

                    // Store group into a category map
                    Entry entry = field.getAnnotation(Entry.class);
                    String group = entry.group();
                    String category = entry.category();

                    // If category map doesn't exist, create it
                    configRecord.getGROUP_OF_CATEGORY().computeIfAbsent(category, k -> new ArrayList<>());

                    if (!configRecord.getGROUP_OF_CATEGORY().get(category).contains(group))
                        configRecord.getGROUP_OF_CATEGORY().get(category).add(group);
                }
            }

            configRecord.getGROUP_OF_CATEGORY().forEach((s, strings) -> {
                Collections.sort(strings);
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class EntryInfo {
        public String info;
        public boolean require_restart;
        public String name;
        public Field targetField;
        public String humanizeName;
        public Object value;
        public String group;
        public boolean hidden;

        public SliderInfo slider = null;

        public boolean pseudoEntry = true;
        public boolean visibleInConfig = true;

        public EntryInfo(Field field, String name, String humanizeName, String info, Object value, boolean require_restart, boolean hidden, String group) {
            this.name = name;
            this.info = info;
            this.require_restart = require_restart;
            this.targetField = field;
            this.humanizeName = humanizeName;
            this.value = value;
            this.group = group;
            this.pseudoEntry = false;
            this.hidden = hidden;
        }

        public void setSlider(SliderInfo slider) {
            this.slider = slider;
        }

        public EntryInfo(String name) {
            this.name = name;
        }
    }

    public static class SliderInfo {
        public int min;
        public int max;
        public int step;

        public SliderInfo(int min, int max, int step) {
            this.max = max;
            this.min = min;
            // Prevent step <= 0
            this.step = Math.max(step, 1);
        }
    }
}
