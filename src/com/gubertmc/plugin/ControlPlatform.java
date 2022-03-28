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
    private Material coreMaterial;
    private Material blockerMaterial;
    private Material spreadMaterial;
    private Material pathMaterial;
    private Material startPointGlassMaterial;
    private Material endPointGlassMaterial;
    private ItemFrame[] frames;
    private int size;


    public ControlPlatform(MazeGeneratorPlugin plugin, Maze maze, Block block, int size) {
        ControlPlatform.block = block;
        ControlPlatform.maze = maze;

        frames = new ItemFrame[6];
        this.size = size;
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

        for (int i = 0; i < 8; i++) { // was 7
            for (int j = 0; j < 3; j++) {
                Location floor = new Location(block.getWorld(), block.getX() + i, block.getY(), block.getZ() + j - 4);
                floor.getBlock().setType(Material.STRIPPED_OAK_WOOD);
            }
            for (int k = 1; k < 3; k++) {
                Location wall = new Location(block.getWorld(), block.getX() + i, block.getY() + k, block.getZ() - 2);
                wall.getBlock().setType(Material.STRIPPED_OAK_WOOD);

                if (k == 2 && i > 1) { // was 2 then 4
                    ItemFrame frame = block.getWorld().spawn(wall.add(0, 0, -1), ItemFrame.class);
                     if (i == 2) {
                        frame.setItem(new ItemStack(spreadMaterial));
                        frames[0] = frame;
                    } else if (i == 3) {
                        frame.setItem(new ItemStack(blockerMaterial));
                        frames[1] = frame;
                    } else if (i == 4) {
                        frame.setItem(new ItemStack(coreMaterial));
                        frames[2] = frame;
                    } else if (i == 5) {
                        frame.setItem(new ItemStack(pathMaterial));
                        frames[3] = frame;
                    } else if (i == 6) {
                        frame.setItem(new ItemStack(startPointGlassMaterial));
                        frames[4] = frame;
                    } else {
                        frame.setItem(new ItemStack(endPointGlassMaterial));
                        frames[5] = frame;
                    }
                }
                if (k == 1 && i == 0) {
                    Location location = new Location(block.getWorld(), block.getX() + i + 1, block.getY() + 1 + k, block.getZ() - 3);
                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.CREEPER_WALL_HEAD);
                }
                if (k == 1 && i == 1) {
                    Location location = new Location(block.getWorld(), block.getX() + i + 1, block.getY() + 1 + k, block.getZ() - 3);
                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
                }
                if (k == 1) {
                    Location location = new Location(block.getWorld(), block.getX() + i + 1, block.getY() + k, block.getZ() - 3);
                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_WALL_SIGN);
                    Sign sign = (Sign) location.getBlock().getRelative(BlockFace.WEST).getState();

                    switch (i) {
                        case 0 -> {
                            sign.setLine(0, "Project Details");
                            sign.setLine(2, "Creator - btror");
                            sign.setLine(3, "github.com/btror");
                            sign.setColor(DyeColor.LIME);
                        }
                        case 1 -> {
                            sign.setLine(1, "Reset");
                            sign.setColor(DyeColor.ORANGE);
                        }
                        case 2 -> {
                            sign.setLine(1, "Spread");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 3 -> {
                            sign.setLine(1, "Walls/Blockers");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 4 -> {
                            sign.setLine(1, "Border/outline");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 5 -> {
                            sign.setLine(1, "Pathfinding Path");
                            sign.setLine(2, "(only for A*)");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 6 -> {
                            sign.setLine(1, "Start Coordinate");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 7 -> {
                            sign.setLine(1, "End Coordinate");
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

    public ItemFrame[] getFrames() {
        return frames;
    }

    public void setFrames(ItemFrame[] frames) {
        this.frames = frames;
        spreadMaterial = frames[0].getItem().getType();
        blockerMaterial = frames[1].getItem().getType();
        coreMaterial = frames[2].getItem().getType();
        pathMaterial = frames[3].getItem().getType();
        startPointGlassMaterial = frames[4].getItem().getType();
        endPointGlassMaterial = frames[5].getItem().getType();
    }

}