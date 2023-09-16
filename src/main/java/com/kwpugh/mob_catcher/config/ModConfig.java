package com.kwpugh.mob_catcher.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.kwpugh.mob_catcher.MobCatcher;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.kwpugh.mob_catcher.MobCatcher.LOGGER;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

    public static String configFile = "/mob_catcher.json";

    @SerializedName("enableDatapackMobTypes")
    public boolean enableDatapackMobTypes = true;

    /**
     * Saves the config to the given file.
     */
    public void saveConfigFile() {
        File file = new File(FabricLoader.getInstance().getConfigDir() + configFile);
        try (var writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            LOGGER.error("Problem occurred when saving config: " + e.getMessage());
        }
    }

    /**
     * Loads config file.
     *
     * @return Config object
     */
    public static ModConfig loadConfigFile() {
        File file = new File(FabricLoader.getInstance().getConfigDir() + configFile);

        ModConfig config = null;
        if (file.exists()) {
            try (var fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                config = GSON.fromJson(fileReader, ModConfig.class);
            } catch (IOException e) {
                throw new RuntimeException(MobCatcher.MOD_ID + " Problem occurred when trying to load config: ", e);
            }
        }
        if(config == null)
            config = new ModConfig();

        config.saveConfigFile();
        return config;
    }
}