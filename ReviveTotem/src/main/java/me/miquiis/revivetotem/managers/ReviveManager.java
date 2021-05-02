package me.miquiis.revivetotem.managers;

import me.miquiis.revivetotem.ReviveTotem;
import me.miquiis.revivetotem.data.TotemData;
import me.miquiis.revivetotem.data.TotemDirection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReviveManager {

    private ReviveTotem plugin;

    private ConfigManager configManager;
    private MessagesManager messagesManager;

    private Set<TotemData> currentRituals;

    public ReviveManager(ReviveTotem plugin)
    {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.messagesManager = plugin.getMessagesManager();

        this.currentRituals = new HashSet<>();
    }

    public void checkRitual(Player ritualCaller, Block dragonHead)
    {
        /**
         *
         * Legend: DH = Dragon Head, DB = Diamond Block, Db = Diamond Block with Sign
         *
         * STRUCTURE:
         *
         *    DH
         *  DBDbDB
         *    DB
         *
         */

        Block trunk = dragonHead.getRelative(BlockFace.DOWN);
        if (!isDiamond(trunk)) return;
        Block legs = trunk.getRelative(BlockFace.DOWN);
        if (!isDiamond(legs)) return;

        TotemDirection direction = findDirection(trunk);

        Block leftArm = findLArm(trunk, direction);
        if (!isDiamond(leftArm)) return;
        Block rightArm = findRArm(trunk, direction);
        if (!isDiamond(rightArm)) return;

        Block sign = findSign(trunk, direction);
        if (!isSign(sign)) return;

        startRitual(new TotemData(dragonHead, trunk, leftArm, rightArm, legs, sign, ritualCaller));
    }

    private void startRitual(TotemData totemData)
    {
        if (totemData.getRevived() == null)
            return;

        if (totemData.getRevived().getGameMode() != GameMode.SPECTATOR)
            return;

        this.currentRituals.add(totemData);

        totemData.getWorld().strikeLightning(totemData.getTotemHead().getLocation());

        totemData.getReviver().sendTitle(messagesManager.getMessage("ritual-start-title").replace("%PLAYER%", totemData.getRevived().getName()), messagesManager.getMessage("ritual-start-subtitle").replace("%PLAYER%", totemData.getRevived().getName()), 10, 70, 20);

        new BukkitRunnable()
        {
            final World world = totemData.getWorld();
            final List<Block> totem = totemData.getTotemParts();
            int index = 0;
            @Override
            public void run() {
                if (totem.size() > index)
                {
                    Block block = totem.get(index);
                    world.playSound(block.getLocation().add(.5,.5,.5), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
                    world.spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(.5,.5,.5), 100, block.getBlockData());
                    block.setType(Material.AIR);
                    index++;
                }
                else
                {
                    world.playSound(totemData.getTotemLeg().getLocation().add(.5,.5,.5), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1f);
                    totemData.getRevived().teleport(totemData.getTotemLeg().getLocation().add(.5,.5,.5));
                    totemData.getRevived().setGameMode(GameMode.SURVIVAL);
                    currentRituals.remove(totemData);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);

    }

    public boolean isRitualPart(Block b)
    {
        return this.currentRituals.stream().anyMatch(p ->
        {
            return p.getTotemParts().stream().anyMatch(bb ->
            {
                return bb.getLocation().toVector().equals(b.getLocation().toVector());
            });
        });
    }

    private TotemDirection findDirection(Block trunk)
    {
        return trunk.getRelative(BlockFace.NORTH).getType() == Material.DIAMOND_BLOCK ? TotemDirection.SIDEWAYS : TotemDirection.FRONT;
    }

    private Block findLArm(Block trunk, TotemDirection direction)
    {
        switch (direction)
        {
            case SIDEWAYS:
                return trunk.getRelative(BlockFace.NORTH);
            case FRONT:
                return trunk.getRelative(BlockFace.WEST);
            default:
                return null;
        }
    }

    private Block findRArm(Block trunk, TotemDirection direction)
    {
        switch (direction)
        {
            case SIDEWAYS:
                return trunk.getRelative(BlockFace.SOUTH);
            case FRONT:
                return trunk.getRelative(BlockFace.EAST);
            default:
                return null;
        }
    }

    private Block findSign(Block trunk, TotemDirection direction)
    {
        switch (direction)
        {
            case FRONT:
                Block sideTry = trunk.getRelative(BlockFace.NORTH);
                return isSign(sideTry) ? sideTry : trunk.getRelative(BlockFace.SOUTH);
            case SIDEWAYS:
                Block frontTry = trunk.getRelative(BlockFace.EAST);
                return isSign(frontTry) ? frontTry : trunk.getRelative(BlockFace.WEST);
            default:
                return null;
        }
    }

    private boolean isSign(Block sign)
    {
        return sign != null && sign.getType().toString().contains("_SIGN");
    }

    private boolean isDiamond(Block block)
    {
        return block != null && block.getType() == Material.DIAMOND_BLOCK;
    }

}
