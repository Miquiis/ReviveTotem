package me.miquiis.revivetotem.managers;

import me.miquiis.revivetotem.ReviveTotem;
import me.miquiis.revivetotem.data.mCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandManager implements CommandExecutor {

    private ReviveTotem plugin;
    private Set<mCommand> commands;

    public CommandManager(ReviveTotem plugin)
    {
        this.plugin = plugin;
        commands = new HashSet<>();
    }

    public void registerCommand(mCommand command)
    {
        commands.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (commands != null && !commands.isEmpty())
        {
            for (mCommand command : commands)
            {
                if (c.getName().equalsIgnoreCase(command.getName()))
                {
                    command.perform(s, new ArrayList<String>(Arrays.asList(args)));
                    return true;
                }
            }
        }

        return false;
    }

}
