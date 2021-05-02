package me.miquiis.revivetotem;

import me.miquiis.revivetotem.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class ReviveTotem extends JavaPlugin {

    private static ReviveTotem instance;

    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private ReviveManager reviveManager;

    private CommandManager commandManager;
    private EventManager eventManager;

    @Override
    public void onEnable()
    {
        instance = this;

        configManager = new ConfigManager(this);
        messagesManager = new MessagesManager(this);
        reviveManager = new ReviveManager(this);
        commandManager = new CommandManager(this);
        eventManager = new EventManager(this);

    }

    @Override
    public void onDisable()
    {

    }

    public static ReviveTotem getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public ReviveManager getReviveManager() {
        return reviveManager;
    }
}
