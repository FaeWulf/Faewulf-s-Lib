package xyz.faewulf.lib.api.v1.dev;

import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.gameTests.registerGameTests;
import xyz.faewulf.lib.util.gameTests.entry.general.initTest;

public class GameTest {

    public static final String DEFAULT = Constants.MOD_ID + ":default";
    public static final String UNDERWATER = Constants.MOD_ID + ":underwater";

    /**
     * Registers a package path to game test.
     *
     * @param path The path of the game test to register.
     * @throws NullPointerException if {@code path} is null.
     * @see initTest
     */
    public static void register(String path) {
        registerGameTests.paths.add(path);
    }
}