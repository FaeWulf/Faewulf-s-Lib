package xyz.faewulf.lib.util.config.ConfigScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.config.ConfigLoaderFromAnnotation;
import xyz.faewulf.lib.util.config.ConfigScreen.Components.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static xyz.faewulf.lib.util.config.ConfigScreen.ConfigScreen.CONFIG_ENTRIES;
import static xyz.faewulf.lib.util.config.ConfigScreen.ConfigScreen.CONFIG_VALUES;

public class ConfigTab implements Tab {

    public Map<ConfigLoaderFromAnnotation.EntryInfo, List<AbstractWidget>> tabEntries = new LinkedHashMap<>();
    private String MOD_ID;

    Component Title;

    public ConfigTab(String MOD_ID, String title, Map<String, ConfigLoaderFromAnnotation.EntryInfo> entry) {

        this.Title = Component.translatable(MOD_ID + ".config.category." + title);

        //creating options buttons
        ConfigLoaderFromAnnotation.getGroups(MOD_ID, title).forEach(group -> {

            // Add GroupButton to the tab's Widget List
            GroupButton groupButton = new GroupButton(Component.literal(group));

            tabEntries.put(new ConfigLoaderFromAnnotation.EntryInfo(group), new ArrayList<>() {{
                add(groupButton);
            }});

            entry.forEach((s1, entryInfo) -> {

                // If hidden from config screen
                if (entryInfo.hidden)
                    return;

                // If not the same group then return
                if (!Objects.equals(entryInfo.group, group))
                    return;

                List<AbstractWidget> buttonList = new ArrayList<>();

                CONFIG_ENTRIES.add(entryInfo);
                CONFIG_VALUES.put(entryInfo.name, entryInfo.value);

                try {
                    Object ref = entryInfo.targetField.get(null);
                    // Handle for: Number
                    if (ref instanceof Number) {
                        buttonList.add(
                                new NumberButtonInfo(MOD_ID, 0, 20, Component.literal(s1), Minecraft.getInstance().font, entryInfo).alignLeft()
                        );
                        if (entryInfo.slider == null) {
                            buttonList.add(
                                    new NumberButton(MOD_ID, Minecraft.getInstance().font, 0, 16, Component.literal(s1), entryInfo)
                            );
                        } else {
                            buttonList.add(
                                    new IntSliderButton(MOD_ID, 0, 20, Component.literal(s1), entryInfo)
                            );
                        }
                    } else
                        // Handle for: boolean and enum
                        buttonList.add(
                                new OptionButton(
                                        MOD_ID,
                                        20, 20, 20, 20,
                                        Component.literal(s1),
                                        button -> {
                                            //System.out.println("Button " + s1 + ": " + entryInfo.info + ", " + entryInfo.value + ", " + entryInfo.require_restart);

                                            // modconfig field
                                            Field field = entryInfo.targetField;
                                            Object value;
                                            try {
                                                value = field.get(null);

                                                if (value instanceof Boolean b) {
                                                    field.set(null, !b);
                                                }

                                                if (value instanceof Enum<?> enumValue) {
                                                    field.set(null, getNextEnumValue(enumValue));
                                                }

                                            } catch (IllegalAccessException e) {
                                                Constants.LOG.error("[backpack] Something went wrong with the config system...");
                                                e.printStackTrace();
                                            }

                                        },
                                        entryInfo
                                ));

                } catch (IllegalAccessException e) {
                    Constants.LOG.error("[backpack] Something went wrong with the config system...");
                    e.printStackTrace();
                }
                //CreateButton(Component.literal(s1), ));
                tabEntries.put(entryInfo, buttonList);
                groupButton.addToControlList(entryInfo);
            });
        });
    }

    // If no entries in the tab then hide the tab
    public boolean isShouldHideFromConfigScreen() {
        AtomicInteger realEntry = new AtomicInteger();
        tabEntries.forEach((entryInfo, abstractWidgets) -> {
            if (!entryInfo.pseudoEntry)
                realEntry.getAndIncrement();
        });

        return tabEntries.isEmpty() || realEntry.get() == 0;
    }

    public static <E extends Enum<E>> E getNextEnumValue(Enum<?> currentValue) {
        E[] enumValues = (E[]) currentValue.getDeclaringClass().getEnumConstants();  // Get all enum values of the type
        int currentIndex = currentValue.ordinal();  // Get current index
        int nextIndex = (currentIndex + 1) % enumValues.length;  // Calculate next index, wrap around if needed
        return enumValues[nextIndex];  // Return the next value
    }

    @Override
    public @NotNull Component getTabTitle() {
        return Title;
    }

    @Override
    public @NotNull Component getTabExtraNarration() {
        return Component.empty();
    }

    @Override
    public void visitChildren(@NotNull Consumer<AbstractWidget> consumer) {

    }

    @Override
    public void doLayout(@NotNull ScreenRectangle screenRectangle) {

    }
}

