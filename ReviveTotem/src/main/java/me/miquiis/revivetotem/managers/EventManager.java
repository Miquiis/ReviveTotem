package me.miquiis.revivetotem.managers;

import me.miquiis.revivetotem.ReviveTotem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;

public class EventManager {

    private final ReviveTotem plugin;

    private final ArrayList<CustomEventHandler> listeners = new ArrayList<CustomEventHandler>() {
        {
            add(new ReviveTotemEventHandler());
        }
    };

    public EventManager(ReviveTotem plugin)
    {
        this.plugin = plugin;

        for (CustomEventHandler listener : listeners)
        {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            listener.init(plugin);
        }
    }
}

abstract class CustomEventHandler implements Listener {
    public abstract void init(ReviveTotem plugin);
}

class ReviveTotemEventHandler extends CustomEventHandler {

    private ReviveTotem plugin;

    private MessagesManager messagesManager;

    private ReviveManager reviveManager;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (reviveManager.isRitualPart(e.getBlock()))
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage(messagesManager.getMessage("ritual-breaking"));
        }
    }

    @EventHandler
    public void onBlockBreak(EntityExplodeEvent e)
    {
        e.blockList().removeIf(b -> reviveManager.isRitualPart(b));
    }

    @EventHandler
    public void onBlockBreak(BlockPistonExtendEvent e)
    {
        for (Block b : e.getBlocks())
        {
            if (b != null && reviveManager.isRitualPart(b))
            {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onBlockBreak(BlockPistonRetractEvent e)
    {
        for (Block b : e.getBlocks())
        {
            if (b != null && reviveManager.isRitualPart(b))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (e.getBlockPlaced().getType() == Material.DRAGON_HEAD)
            reviveManager.checkRitual(e.getPlayer(), e.getBlockPlaced());
    }

    @Override
    public void init(ReviveTotem plugin) {
        this.plugin = plugin;
        this.messagesManager = plugin.getMessagesManager();
        this.reviveManager = plugin.getReviveManager();
    }
}
