package me.miquiis.revivetotem.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.*;

public class TotemData {

    private List<Block> totemParts;

    private Block totemHead, totemTrunk, totemLArm, totemRArm, totemLeg, totemSign;

    private Sign sign;

    private Player reviver;

    private Player revived;

    public TotemData(Block totemHead, Block totemTrunk, Block totemLArm, Block totemRArm, Block totemLeg, Block totemSign, Player reviver)
    {
        this.totemHead = totemHead;
        this.totemTrunk = totemTrunk;
        this.totemLArm = totemLArm;
        this.totemRArm = totemRArm;
        this.totemLeg = totemLeg;
        this.totemSign = totemSign;

        this.reviver = reviver;

        this.totemParts = Arrays.asList(totemHead, totemSign, totemTrunk, totemLArm, totemRArm, totemLeg);

        this.sign = (Sign) totemSign.getState();

        findRevived().ifPresent(s -> this.revived = s);
    }

    public List<Block> getTotemParts() {
        return totemParts;
    }

    private Optional<Player> findRevived()
    {
        return Arrays.stream(sign.getLines()).filter(s -> !s.isEmpty()).map(String::trim).map(Bukkit::getPlayer).filter(Objects::nonNull).filter(p -> !p.equals(reviver)).findFirst();
    }

    public World getWorld()
    {
        return totemHead.getWorld();
    }

    public Block getTotemHead() {
        return totemHead;
    }

    public Player getReviver() {
        return reviver;
    }

    public Player getRevived() {
        return revived;
    }

    public Block getTotemLArm() {
        return totemLArm;
    }

    public Block getTotemLeg() {
        return totemLeg;
    }

    public Block getTotemRArm() {
        return totemRArm;
    }

    public Block getTotemSign() {
        return totemSign;
    }

    public Block getTotemTrunk() {
        return totemTrunk;
    }

    public boolean isTotem(Block b)
    {
        return b.equals(getTotemLArm()) || b.equals(getTotemLeg())|| b.equals(getTotemSign()) || b.equals(getTotemRArm()) || b.equals(getTotemHead());
    }

}
