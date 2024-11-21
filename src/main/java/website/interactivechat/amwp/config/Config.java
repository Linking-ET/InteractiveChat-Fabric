package website.interactivechat.amwp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import website.interactivechat.amwp.InteractiveChat;

public class Config {
    private boolean enableTitle = true;
    private boolean enableSubtitle = true;
    private boolean enableActionBar = true;
    private List<String> itemKeywords = Arrays.asList("[item]");
    private List<String> inventoryKeywords = Arrays.asList("[inv]");
    private List<String> enderChestKeywords = Arrays.asList("[ender]");
    private String chatFormat = "{name} >> {message}";
    private String nameFormat = "{prefix}{name}{suffix}";
    private String nameColor = "yellow";
    private String separatorColor = "gray";
    private boolean enableLuckPerms = true;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    private static final File CONFIG_FILE = CONFIG_DIR.resolve("interactivechat.json").toFile();
    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = loadConfig();
        }
        return instance;
    }

    public static void reloadConfig() {
        InteractiveChat.LOGGER.info("Reloading InteractiveChat configuration...");
        instance = loadConfig();
    }

    private static Config loadConfig() {
        InteractiveChat.LOGGER.info("Loading InteractiveChat configuration...");
        
        try {
            if (!CONFIG_FILE.exists()) {
                InteractiveChat.LOGGER.info("Configuration file not found, creating default configuration...");
                Config config = new Config();
                config.saveConfig();
                return config;
            }

            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                Config config = GSON.fromJson(reader, Config.class);
                if (config == null) {
                    InteractiveChat.LOGGER.error("Failed to parse configuration file, creating new one with default values");
                    config = new Config();
                }
                config.saveConfig(); // Save to ensure any new fields are written
                InteractiveChat.LOGGER.info("Configuration loaded successfully");
                return config;
            }
        } catch (Exception e) {
            InteractiveChat.LOGGER.error("Failed to load configuration: {}", e.getMessage());
            InteractiveChat.LOGGER.error("Stack trace:", e);
            InteractiveChat.LOGGER.info("Creating new configuration file with default values");
            Config config = new Config();
            config.saveConfig();
            return config;
        }
    }

    public void saveConfig() {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) {
                InteractiveChat.LOGGER.info("Creating configuration directory...");
                if (!CONFIG_FILE.getParentFile().mkdirs()) {
                    InteractiveChat.LOGGER.error("Failed to create configuration directory!");
                    return;
                }
            }

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
                InteractiveChat.LOGGER.info("Configuration saved successfully to {}", CONFIG_FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            InteractiveChat.LOGGER.error("Failed to save configuration: {}", e.getMessage());
            InteractiveChat.LOGGER.error("Stack trace:", e);
        }
    }

    public boolean isEnableTitle() {
        return enableTitle;
    }

    public boolean isEnableSubtitle() {
        return enableSubtitle;
    }

    public boolean isEnableActionBar() {
        return enableActionBar;
    }

    public List<String> getItemKeywords() {
        return itemKeywords;
    }

    public List<String> getInventoryKeywords() {
        return inventoryKeywords;
    }

    public List<String> getEnderChestKeywords() {
        return enderChestKeywords;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public String getNameFormat() {
        return nameFormat;
    }

    public String getNameColor() {
        return nameColor;
    }

    public String getSeparatorColor() {
        return separatorColor;
    }

    public void setChatFormat(String format) {
        this.chatFormat = format;
        saveConfig();
    }

    public void setNameFormat(String format) {
        this.nameFormat = format;
        saveConfig();
    }

    public void setNameColor(String color) {
        this.nameColor = color;
        saveConfig();
    }

    public void setSeparatorColor(String color) {
        this.separatorColor = color;
        saveConfig();
    }

    public boolean isEnableLuckPerms() {
        return enableLuckPerms;
    }

    public void setEnableLuckPerms(boolean enable) {
        this.enableLuckPerms = enable;
        saveConfig();
    }
} 