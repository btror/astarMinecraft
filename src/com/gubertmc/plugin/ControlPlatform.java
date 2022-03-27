package com.gubertmc.plugin;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.Maze;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;


public class ControlPlatform implements Listener {

    private static com.gubertmc.plugin.main.Maze maze;
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

                if (k == 2 && i != 2) {
                    ItemFrame frame = block.getWorld().spawn(wall.add(0, 0, -1), ItemFrame.class);
                    if (i == 0) {
                        frame.setItem(new ItemStack(spreadMaterial));
                    } else if (i == 1) {
                        frame.setItem(new ItemStack(blockerMaterial));
                    } else if (i == 3) {
                        frame.setItem(new ItemStack(coreMaterial));
                    } else {
                        frame.setItem(new ItemStack(pathMaterial));
                    }
                }
                if (k == 1 && i == 3) {
                    Location location = new Location(block.getWorld(), block.getX() + i, block.getY() + 1 + k, block.getZ() - 3);
                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
                    button = location.getBlock();
                }
                if (k == 1) {
                    Location location = new Location(block.getWorld(), block.getX() + i + 1, block.getY() + k, block.getZ() - 3);
                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_WALL_SIGN);
                    Sign sign = (Sign) location.getBlock().getRelative(BlockFace.WEST).getState();

                    switch (i) {
                        case 0 -> {
                            sign.setLine(1, "Spread");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 1 -> {
                            sign.setLine(1, "Walls/Blockers");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 2 -> {
                            sign.setLine(1, "Start");
                            sign.setColor(DyeColor.ORANGE);
                        }
                        case 3 -> {
                            sign.setLine(1, "Border/Outline");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 4 -> {
                            sign.setLine(1, "Pathfinding Path");
                            sign.setColor(DyeColor.WHITE);
                        }
                    }
                    sign.setGlowingText(true);
                    sign.update();
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

}