package xyz.faewulf.lib.util.config.ConfigScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.faewulf.lib.util.config.Config;
import xyz.faewulf.lib.util.config.ConfigLoaderFromAnnotation;

import java.util.*;

public class ConfigScreen extends Screen {

    private final String MOD_ID;

    //Textures
    private final ResourceLocation ATLAS_TEXTURE;

    //background
    private final int ATLAS_SIZE = 32 * 3; // number of atlas tile
    private final int TILE_SIZE = 32;
    private int[][] tileMap;  // Store the (x, y) positions of the random tiles in the atlas
    private int tilesX;
    private int tilesY;

    //client
    private final Screen parent;
    private final Minecraft client;

    //layout vars
    private static final int RIGHT_TAB_PADDING = 2;
    private Boolean isChanged = false;

    //config variables, for checking save
    //CONFIG_VALUES is for current values of all options
    public static Map<String, Object> CONFIG_VALUES = new LinkedHashMap<>();
    public static List<ConfigLoaderFromAnnotation.EntryInfo> CONFIG_ENTRIES = new ArrayList<>();

    //main components
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);

    @Nullable
    private TabNavigationBar tabNavigationBar;

    @Nullable
    private ScrollableListWidget slw;

    @Nullable
    private Tab selectedTab;

    @Nullable
    private GridLayout rightTab;

    public static GridLayout infoTab;
    public static String currentInfo;
    public static boolean updateCall = false;

    //part components
    @Nullable
    private EditBox searchBar;

    @Nullable
    private Button ButtonReset_Cancel;
    @Nullable
    private Button ButtonUndo;
    @Nullable
    private Button ButtonDone_Save;

    public static MultiLineTextWidget infoTab_Title;
    public static MultiLineTextWidget infoTab_Info;

    protected ConfigScreen(Screen parent, String MOD_ID) {
        super(Component.translatable(MOD_ID + ".config." + "title"));
        this.parent = parent;
        client = Minecraft.getInstance();

        this.tilesX = (int) Math.ceil(this.width * 1.0f / TILE_SIZE);
        this.tilesY = (int) Math.ceil(this.height * 1.0f / TILE_SIZE);
        this.tileMap = new int[tilesX][tilesY];

        this.MOD_ID = MOD_ID;
        ATLAS_TEXTURE = ResourceLocation.tryBuild(MOD_ID, "textures/gui/atlas_background.png");
    }

    public static Screen getScreen(Screen parent, String MOD_ID) {
        return new ConfigScreen(parent, MOD_ID);
    }

    @Override
    protected void init() {

        CONFIG_ENTRIES.clear();
        CONFIG_VALUES.clear();

        //background
        generateRandomTileMap();

        //data
        Map<String, Map<String, ConfigLoaderFromAnnotation.EntryInfo>> configMap = ConfigLoaderFromAnnotation.loadConfig(MOD_ID);

        //top tab bar
        TabNavigationBar.Builder tabBuilder = TabNavigationBar.builder(this.tabManager, this.width);

        configMap.forEach((s, stringEntryTypeMap) -> { //create tab for each category
            ConfigTab configTab = new ConfigTab(MOD_ID, s, stringEntryTypeMap);

            if (!configTab.isShouldHideFromConfigScreen())
                tabBuilder.addTabs(configTab);
        });

        this.tabNavigationBar = tabBuilder.build();

        //right tab
        this.rightTab = new GridLayout();

        GridLayout.RowHelper rightTabRowHelper = this.rightTab.createRowHelper(2);
        rightTabRowHelper.defaultCellSetting().alignHorizontallyCenter().alignVerticallyBottom().padding(1);

        //search bar
        this.searchBar = rightTabRowHelper.addChild(new EditBox(this.font, 0, 0, 150, 20, Component.translatable(MOD_ID + ".config." + "searchbar.hint")), 2);
        searchBar.setHint(Component.translatable(MOD_ID + ".config." + "searchbar.hint"));
        searchBar.setResponder(this::pushList);

        //3 buttons in right tab
        ButtonReset_Cancel = rightTabRowHelper.addChild(
                Button.builder(
                                Component.translatable(MOD_ID + ".config." + "reset"),
                                button -> {
                                    //This is cancel mode
                                    if (isChanged) {
                                        this.undoConfig();
                                        this.onClose();
                                        return;
                                    }

                                    //reset mode
                                    Map<String, Object> defaultValue = ConfigLoaderFromAnnotation.getAllDefaultValues(MOD_ID);
                                    //by using CONFIG_ENTRIES set value
                                    //System.out.println(defaultValue);

                                    CONFIG_ENTRIES.forEach(entryInfo -> {
                                        try {
                                            entryInfo.targetField.set(null, defaultValue.get(entryInfo.name));
                                        } catch (IllegalAccessException e) {

                                            throw new RuntimeException(e);
                                        }
                                    });
                                })
                        .width(75)
                        .tooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "reset.tooltip")))
                        .build()
        );

        ButtonUndo = rightTabRowHelper.addChild(
                Button.builder(
                                Component.translatable(MOD_ID + ".config." + "undo"),
                                button -> this.undoConfig())
                        .width(75)
                        .tooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "undo.tooltip")))
                        .build()
        );

        //disable by default
        ButtonUndo.active = false;

        ButtonDone_Save = rightTabRowHelper.addChild(
                Button.builder(
                                Component.translatable(MOD_ID + ".config." + "exit"),
                                button -> {
                                    if (isChanged) {
                                        Config.save(MOD_ID);
                                        this.updateConfig();
                                    } else {
                                        undoConfig();
                                        this.onClose();
                                    }
                                })
                        .width(154)
                        .tooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "exit.tooltip")))
                        .build(),
                2
        );

        //Info tab (right tab)
        infoTab = new GridLayout();
        GridLayout.RowHelper infoTabRowHelper = infoTab.createRowHelper(1);
        infoTabRowHelper.defaultCellSetting().padding(4);

        infoTab_Title = infoTabRowHelper.addChild(new MultiLineTextWidget(Component.literal(""), this.font));
        infoTab_Info = infoTabRowHelper.addChild(new MultiLineTextWidget(Component.literal(""), this.font));
        infoTab_Title.setMaxWidth(154);
        infoTab_Info.setMaxWidth(154);

        //register rendering to main Screen

        //for scroll list
        ScreenRectangle screenRectangle = new ScreenRectangle(0, this.tabNavigationBar.getRectangle().bottom(), this.width - this.rightTab.getWidth(), this.height - this.tabNavigationBar.getRectangle().bottom());
        this.slw = new ScrollableListWidget(MOD_ID, this.client, screenRectangle.width(), screenRectangle.height(), 24, screenRectangle.height(), 20);

        //navigation
        this.tabNavigationBar.selectTab(0, false);
        this.selectedTab = null;

        this.addRenderableWidget(slw);

        infoTab.visitWidgets(this::addRenderableWidget); //info register

        //register each widget in right tab (buttons)
        rightTab.visitWidgets(abstractWidget -> {
            abstractWidget.setTabOrderGroup(1);
            this.addRenderableWidget(abstractWidget);
        });

        this.addRenderableWidget(this.tabNavigationBar);

        //init the reposition
        this.repositionElements();
    }

    @Override
    protected void repositionElements() { //on resize windows event

        //background
        this.tilesX = (int) Math.ceil(this.width * 1.0f / TILE_SIZE);
        this.tilesY = (int) Math.ceil(this.height * 1.0f / TILE_SIZE);
        this.tileMap = new int[tilesX][tilesY];
        generateRandomTileMap();

        //other comp
        if (this.tabNavigationBar != null && this.rightTab != null && this.slw != null) {
            this.tabNavigationBar.setWidth(this.width);
            //arrange each main comp
            this.tabNavigationBar.arrangeElements();
            this.rightTab.arrangeElements();
            infoTab.arrangeElements();

            //the Y position of the bottom of navbar
            int i = this.tabNavigationBar.getRectangle().bottom();

            //reposition for right tab
            FrameLayout.centerInRectangle(this.rightTab, this.width - this.rightTab.getWidth() - RIGHT_TAB_PADDING, this.height - this.rightTab.getHeight() - RIGHT_TAB_PADDING, this.rightTab.getWidth() + RIGHT_TAB_PADDING, this.rightTab.getHeight() + RIGHT_TAB_PADDING);

            //reposition for info tab
            FrameLayout.alignInRectangle(infoTab, this.width - this.rightTab.getWidth() - RIGHT_TAB_PADDING, i, this.rightTab.getWidth() + RIGHT_TAB_PADDING, this.height - i - this.rightTab.getHeight(), 0f, 0f);

            //for list, slw
            ScreenRectangle screenRectangle = new ScreenRectangle(0, i, this.width - this.rightTab.getWidth() - RIGHT_TAB_PADDING, this.height);

            //resize and reposition

            HeaderAndFooterLayout headerAndFooterLayout = new HeaderAndFooterLayout(this, i, 0);
            this.slw.updateSize(screenRectangle.width(), headerAndFooterLayout);

            //this.slw.setSize(screenRectangle.width(), screenRectangle.height(), screenRectangle.top(), screenRectangle.height());
            this.tabManager.setTabArea(screenRectangle);
        }

    }

    @Override
    public void tick() {
        super.tick();

        //refresh scrollbar (change entry list for scrollbar
        //only change when change tab
        if (this.slw != null && this.searchBar != null && this.selectedTab != this.tabManager.getCurrentTab()) {
            pushList(this.searchBar.getValue());
        }

        // To handle re render when using group buttons
        if (updateCall) {
            updateCall = false;
            this.pushListWithoutSetScroll(this.searchBar == null ? null : this.searchBar.getValue());
        }

        //checking for save
        //checking between CONFIG_VALUES and CONFIG_ENTRIES, if different then trigger save button and cancel button
        //CONFIG_VALUES is for reference only, don't change it here

        Map<String, ConfigLoaderFromAnnotation.EntryInfo> current_config_data = ConfigLoaderFromAnnotation.loadConfig_EntryOnly(MOD_ID);

        this.isChanged = false;
        current_config_data.forEach((s, entryInfo) -> {
            try {

                //value from CONFIG_VALUES
                Object pastValue = CONFIG_VALUES.get(entryInfo.name);
                Object currentValue = entryInfo.targetField.get(null);

                if (pastValue != null && !pastValue.equals(currentValue)) {
                    isChanged = true;
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        });

        //switch mode each buttons based on change mode
        if (ButtonUndo != null && ButtonDone_Save != null && ButtonReset_Cancel != null && isChanged) {
            this.ButtonDone_Save.setMessage(Component.translatable(MOD_ID + ".config." + "save").withStyle(ChatFormatting.GREEN));
            this.ButtonDone_Save.setTooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "save.tooltip")));

            this.ButtonReset_Cancel.setMessage(Component.translatable(MOD_ID + ".config." + "cancel").withStyle(ChatFormatting.RED));
            this.ButtonReset_Cancel.setTooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "cancel.tooltip")));

            this.ButtonUndo.active = true;
        } else {
            this.ButtonReset_Cancel.setMessage(Component.translatable(MOD_ID + ".config." + "reset"));
            this.ButtonReset_Cancel.setTooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "reset.tooltip")));

            this.ButtonDone_Save.setMessage(Component.translatable(MOD_ID + ".config." + "exit"));
            this.ButtonDone_Save.setTooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "exit.tooltip")));

            this.ButtonUndo.active = false;
        }
    }

    //update config: CONFIG_ENTRIES -> CONFIG_VALUES (CONFIG_ENTRIES holds variable's pointer)
    private void updateConfig() {
        for (ConfigLoaderFromAnnotation.EntryInfo configEntry : CONFIG_ENTRIES) {
            try {
                CONFIG_VALUES.put(configEntry.name, configEntry.targetField.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //undo config: CONFIG_VALUES -> variables (by using CONFIG_ENTRIES which holding variable's pointer)
    private void undoConfig() {
        for (ConfigLoaderFromAnnotation.EntryInfo configEntry : CONFIG_ENTRIES) {
            try {
                Object value = CONFIG_VALUES.get(configEntry.name);
                configEntry.targetField.set(null, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //method for reset scroll list content
    private void pushList(String filter) {
        if (this.slw == null)
            return;
        pushListWithoutSetScroll(filter);
        this.slw.setScrollAmount(0);
    }

    private void pushListWithoutSetScroll(@Nullable String filter) {
        if (this.slw == null || this.searchBar == null)
            return;

        this.selectedTab = this.tabManager.getCurrentTab();
        this.slw.clear();

        if (this.selectedTab instanceof ConfigTab configTab) {
            configTab.tabEntries.forEach((entryType, buttons) -> {

                if (!entryType.visibleInConfig) {
                    return;
                }

                if (entryType.pseudoEntry) {
                    this.slw.addRow(entryType, buttons.toArray(new AbstractWidget[]{}));
                } else if (filter == null || filter.isEmpty()) {
                    this.slw.addRow(entryType, buttons.toArray(new AbstractWidget[]{}));
                } else if (entryType.name.contains(filter.toLowerCase())) {
                    this.slw.addRow(entryType, buttons.toArray(new AbstractWidget[]{}));
                }
            });
        }
    }

    @Override
    public void onClose() {
        if (this.client != null)
            this.client.setScreen(this.parent);
        else
            super.onClose();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int $$1, int $$2, float $$3) {
        //this.renderBackground(guiGraphics, $$1, $$2, $$3);

        //drawRandomTiledBackground(guiGraphics);
        //extra layer for right tab
        guiGraphics.fillGradient(
                this.rightTab.getX() - RIGHT_TAB_PADDING,
                this.tabNavigationBar.getRectangle().bottom(),
                this.width,
                this.height,
                0xbb000000, 0xbb000000
        );

        super.render(guiGraphics, $$1, $$2, $$3);
    }

    private void drawRandomTiledBackground(GuiGraphics guiGraphics) {
        int tilesPerRow = ATLAS_SIZE / TILE_SIZE;  // Number of tiles per row in the atlas

        for (int y = 0; y < tilesY; y++) {
            for (int x = 0; x < tilesX; x++) {
                // Calculate the tile's atlas coordinates based on the stored index
                int tileIndex = tileMap[x][y];
                int tileX = (tileIndex % tilesPerRow) * TILE_SIZE;  // X offset in the atlas
                int tileY = (tileIndex / tilesPerRow) * TILE_SIZE;  // Y offset in the atlas

                //Draw the tile from the atlas
                guiGraphics.blit(
                        RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND,
                        ATLAS_TEXTURE,
                        x * TILE_SIZE,  // X position on the screen
                        y * TILE_SIZE,  // Y position on the screen
                        tileX, tileY,   // X, Y offset in the atlas
                        TILE_SIZE, TILE_SIZE,  // Tile size (width, height)
                        ATLAS_SIZE, ATLAS_SIZE  // Atlas size (width, height)
                );
            }
        }
    }

    private void generateRandomTileMap() {
        Random random = new Random();
        int tilesPerRow = ATLAS_SIZE / TILE_SIZE;  // Number of tiles per row in the atlas

        for (int y = 0; y < tilesY; y++) {
            for (int x = 0; x < tilesX; x++) {
                // Randomly select a tile from the atlas by generating random coordinates (tileX, tileY)
                int tileX = random.nextInt(tilesPerRow);  // Random column
                int tileY = random.nextInt(tilesPerRow);  // Random row

                // Store the (tileX, tileY) for later use when rendering
                tileMap[x][y] = tileY * tilesPerRow + tileX;  // Linear index for simplicity
            }
        }
    }
}
