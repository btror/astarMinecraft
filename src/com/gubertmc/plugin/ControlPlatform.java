package com.gubertmc.plugin;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class ControlPlatform implements Listener {

    private static Maze maze;
    private static Block block;
    private static Block button;
    private final Material coreMaterial;
    private final Material blockerMaterial;
    private final Material spreadMaterial;
    private final Material pathMaterial;
    private final Material startPointGlassMaterial;
    private final Material endPointGlassMaterial;


    public ControlPlatform(MazeGeneratorPlugin plugin, Maze maze, Block block, int size) {
        ControlPlatform.block = block;
        ControlPlatform.maze = maze;

        coreMaterial = Material.RED_STAINED_GLASS;
        blockerMaterial = Material.LAPIS_BLOCK;
        spreadMaterial = Material.LIME_STAINED_GLASS;
        pathMaterial = Material.WHITE_STAINED_GLASS;
        startPointGlassMaterial = Material.GREEN_STAINED_GLASS;
        endPointGlassMaterial = Material.ORANGE_STAINED_GLASS;
    }

    /**
     * Spawn a control platform.
     */
    public void spawn() {
        maze.generateCore(coreMaterial);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                Location floor = new Location(block.getWorld(), block.getX() + i, block.getY(), block.getZ() + j - 4);
                floor.getBlock().setType(Material.STRIPPED_OAK_WOOD);
            }
            for (int k = 1; k < 3; k++) {
                Location wall = new Location(block.getWorld(), block.getX() + i, block.getY() + k, block.getZ() - 2);
                wall.getBlock().setType(Material.STRIPPED_OAK_WOOD);

                if (k == 1 && i != 2) {
                    ItemFrame frame = block.getWorld().spawn(wall.add(0, 0, -1), ItemFrame.class);
                    if (i == 0) {
                        frame.setItem(new ItemStack(Material.GREEN_STAINED_GLASS));
                    } else if (i == 1) {
                        frame.setItem(new ItemStack(Material.LAPIS_BLOCK));
                    } else if (i == 3) {
                        frame.setItem(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS));
                    } else {
                        frame.setItem(new ItemStack(Material.WHITE_STAINED_GLASS));
                    }
                }
                if (k == 1 && i == 3) {
                    Location loc = new Location(block.getWorld(), block.getX() + i, block.getY() + 1 + k, block.getZ() - 3);
                    loc.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
                    button = loc.getBlock();
                }
            }
        }
    }

    public Material getCoreMaterial() {
        return coreMaterial;
    }

    public Material getBlockerMaterial() {
        return blockerMaterial;
    }

    public Material getSpreadMaterial() {
        return spreadMaterial;
    }

    public Material getPathMaterial() {
        return pathMaterial;
    }

    public Material getStartPointGlassMaterial() {
        return startPointGlassMaterial;
    }

    public Material getEndPointGlassMaterial() {
        return endPointGlassMaterial;
    }

//    /**
//     * Clear the old maze out and create a new one.
//     *
//     * @param e event
//     */
//    @EventHandler
//    public void onButtonPressed(PlayerInteractEvent e) {
//        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
//            if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.WARPED_BUTTON) {
//                maze.generateCore(coreMaterial);
//                maze.generateNewMaze(coreMaterial, blockerMaterial, spreadMaterial, pathMaterial, startPointGlassMaterial, endPointGlassMaterial);
//            }
//        }
//    }
}