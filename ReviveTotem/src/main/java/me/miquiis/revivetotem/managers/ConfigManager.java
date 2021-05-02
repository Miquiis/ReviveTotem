package me.miquiis.revivetotem.managers;

import me.miquiis.revivetotem.ReviveTotem;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final ReviveTotem plugin;
    private final File configFile;
    private final FileConfiguration config;

    public ConfigManager(ReviveTotem plugin)
    {
        this.plugin = plugin;
        this.config = new YamlConfiguration();
        this.configFile = load();
    }

    public String getString(String path)
    {
        return config.getString(path);
    }

    public Integer getInt(String path)
    {
        return config.getInt(path);
    }

    public Long getLong(String path)
    {
        return config.getLong(path);
    }

    public Boolean getBoolean(String path) { return config .getBoolean(path); }

    private File load()
    {
        try
        {
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            if (!configFile.exists())
            {
                plugin.saveResource("config.yml", false);
            }
            config.load(configFile);
            return configFile;
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
