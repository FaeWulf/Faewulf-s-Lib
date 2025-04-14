package xyz.faewulf.lib.util.config;


import xyz.faewulf.lib.Constants;

@ModConfig(mod_id = Constants.MOD_ID)
public class ModConfigs {

    @Entry(category = "Client", name = "Boolean", require_restart = false)
    public static boolean boolean_test = true;

    @Entry(category = "Client", name = "Enum type", require_restart = false)
    public static boolean enum_test = true;

    @Entry(category = "Client", name = "Int value", require_restart = false, group = "Number option")
    public static int int_test = 10;

    @Entry(category = "Client", name = "Int Slider", require_restart = false, group = "Number option")
    @SliderEntry(min = 0, max = 100, step = 2)
    public static int int_slider = 50;

    @Entry(category = "Server", name = "Boolean with caution", require_restart = true)
    public static boolean boolean_with_caution = false;

    @Entry(category = "style", name = "Hidden value 1", require_restart = false, hidden = true)
    public static String hidden_value = "default";

    @Entry(category = "style", name = "Hidden value 2", require_restart = false, hidden = true)
    public static String hidden_value_2 = "default";

}