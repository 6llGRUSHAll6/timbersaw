package me.timbersaw.timbersaw;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Timbersaw extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        if (tool == null || !tool.getType().name().endsWith("_AXE")) return;

        Block block = event.getBlock();
        Material type = block.getType();

        if (isLog(type)) {
            event.setCancelled(true);
            fellTree(block, type, tool);
        }
    }

    private boolean isLog(Material material) {
        switch (material) {
            case OAK_LOG:
            case SPRUCE_LOG:
            case BIRCH_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
            case DARK_OAK_LOG:
            case MANGROVE_LOG:
            case CHERRY_LOG:
            case CRIMSON_STEM:
            case WARPED_STEM:
            case STRIPPED_OAK_LOG:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_MANGROVE_LOG:
            case STRIPPED_CHERRY_LOG:
            case STRIPPED_CRIMSON_STEM:
            case STRIPPED_WARPED_STEM:
                return true;
            default:
                return false;
        }
    }

    private Material getCorrespondingLeaf(Material log) {
        switch (log) {
            case OAK_LOG:
            case STRIPPED_OAK_LOG:
                return Material.OAK_LEAVES;
            case SPRUCE_LOG:
            case STRIPPED_SPRUCE_LOG:
                return Material.SPRUCE_LEAVES;
            case BIRCH_LOG:
            case STRIPPED_BIRCH_LOG:
                return Material.BIRCH_LEAVES;
            case JUNGLE_LOG:
            case STRIPPED_JUNGLE_LOG:
                return Material.JUNGLE_LEAVES;
            case ACACIA_LOG:
            case STRIPPED_ACACIA_LOG:
                return Material.ACACIA_LEAVES;
            case DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_LOG:
                return Material.DARK_OAK_LEAVES;
            case MANGROVE_LOG:
            case STRIPPED_MANGROVE_LOG:
                return Material.MANGROVE_LEAVES;
            case CHERRY_LOG:
            case STRIPPED_CHERRY_LOG:
                return Material.CHERRY_LEAVES;
            case CRIMSON_STEM:
            case STRIPPED_CRIMSON_STEM:
                return Material.NETHER_WART_BLOCK;
            case WARPED_STEM:
            case STRIPPED_WARPED_STEM:
                return Material.WARPED_WART_BLOCK;
            default:
                return null;
        }
    }

    private void fellTree(Block startBlock, Material logType, ItemStack tool) {
        Material leafType = getCorrespondingLeaf(logType);
        if (leafType == null) return;

        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(startBlock);
        visited.add(startBlock);

        startBlock.breakNaturally(tool);

        while (!queue.isEmpty()) {
            Block current = queue.poll();

            for (BlockFace face : BlockFace.values()) {
                if (face == BlockFace.SELF) continue;

                Block neighbor = current.getRelative(face);
                if (visited.contains(neighbor)) continue;

                Material neighborType = neighbor.getType();
                if (neighborType == logType || neighborType == leafType) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    neighbor.breakNaturally(tool);
                }
            }
        }
    }
}