package com.gubertmc.plugin;

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

    private final Block mazeLocationBlock;
    private Material coreMaterial;
    private Material blockerMaterial;
    private Material spreadMaterial;
    private Material pathMaterial;
    private Material startPointGlassMaterial;
    private Material endPointGlassMaterial;
    private ItemFrame[] frames;


    public ControlPlatform(Block mazeLocationBlock) {
        this.mazeLocationBlock = mazeLocationBlock;

        this.frames = new ItemFrame[6];
        this.coreMaterial = Material.RED_STAINED_GLASS;
        this.blockerMaterial = Material.LAPIS_BLOCK;
        this.spreadMaterial = Material.LIME_STAINED_GLASS;
        this.pathMaterial = Material.WHITE_STAINED_GLASS;
        this.startPointGlassMaterial = Material.GREEN_STAINED_GLASS;
        this.endPointGlassMaterial = Material.ORANGE_STAINED_GLASS;
    }

    /**
     * Spawn a control platform.
     */
    public void spawn() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                Location floor = new Location(
                        mazeLocationBlock.getWorld(),
                        mazeLocationBlock.getX() + i,
                        mazeLocationBlock.getY(),
                        mazeLocationBlock.getZ() + j - 4
                );
                floor.getBlock().setType(Material.STRIPPED_OAK_WOOD);
            }
            for (int k = 1; k < 3; k++) {
                Location wall = new Location(
                        mazeLocationBlock.getWorld(),
                        mazeLocationBlock.getX() + i,
                        mazeLocationBlock.getY() + k,
                        mazeLocationBlock.getZ() - 2
                );
                wall.getBlock().setType(Material.STRIPPED_OAK_WOOD);

                if (k == 2 && i > 1) {
                    ItemFrame frame = mazeLocationBlock.getWorld().spawn(wall.add(0, 0, -1), ItemFrame.class);

                    switch (i) {
                        case 2 -> {
                            frame.setItem(new ItemStack(spreadMaterial));
                            frames[0] = frame;
                        }
                        case 3 -> {
                            frame.setItem(new ItemStack(blockerMaterial));
                            frames[1] = frame;
                        }
                        case 4 -> {
                            frame.setItem(new ItemStack(coreMaterial));
                            frames[2] = frame;
                        }
                        case 5 -> {
                            frame.setItem(new ItemStack(pathMaterial));
                            frames[3] = frame;
                        }
                        case 6 -> {
                            frame.setItem(new ItemStack(startPointGlassMaterial));
                            frames[4] = frame;
                        }
                        default -> {
                            frame.setItem(new ItemStack(endPointGlassMaterial));
                            frames[5] = frame;
                        }
                    }
                }

                if (k == 1 && i == 0) {
                    Location location = new Location(
                            mazeLocationBlock.getWorld(),
                            mazeLocationBlock.getX() + i + 1,
                            mazeLocationBlock.getY() + 1 + k,
                            mazeLocationBlock.getZ() - 3
                    );
                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.CREEPER_WALL_HEAD);
                }

                if (k == 1 && i == 1) {
                    Location location = new Location(
                            mazeLocationBlock.getWorld(),
                            mazeLocationBlock.getX() + i + 1,
                            mazeLocationBlock.getY() + 1 + k,
                            mazeLocationBlock.getZ() - 3
                    );
                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
                }

                if (k == 1) {
                    Location location = new Location(
                            mazeLocationBlock.getWorld(),
                            mazeLocationBlock.getX() + i + 1,
                            mazeLocationBlock.getY() + k,
                            mazeLocationBlock.getZ() - 3
                    );
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
                            sign.setLine(1, "Start/reset");
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

    /**
     * Getter: maze core material.
     *
     * @return floor material for 2D | volume material for 3D.
     */
    public Material getCoreMaterial() {
        return coreMaterial;
    }

    /**
     * Getter: blocker material.
     *
     * @return maze wall/blocker material.
     */
    public Material getBlockerMaterial() {
        return blockerMaterial;
    }

    /**
     * Getter: spread material.
     *
     * @return spread animation material.
     */
    public Material getSpreadMaterial() {
        return spreadMaterial;
    }

    /**
     * Getter: path material.
     *
     * @return path animation material (astar only).
     */
    public Material getPathMaterial() {
        return pathMaterial;
    }

    /**
     * Getter: maze start point material.
     *
     * @return material over the start coordinate (preferably glass).
     */
    public Material getStartPointGlassMaterial() {
        return startPointGlassMaterial;
    }

    /**
     * Getter: maze end point material.
     *
     * @return material over the end coordinate (preferably glass).
     */
    public Material getEndPointGlassMaterial() {
        return endPointGlassMaterial;
    }

    /**
     * Getter: item frames.
     *
     * @return control platform item frames containing maze block types.
     */
    public ItemFrame[] getFrames() {
        return frames;
    }

    /**
     * Setter: set frames.
     *
     * @param frames control platform item frames containing maze block types.
     */
    public void setFrames(ItemFrame[] frames) {
        this.frames = frames;
        this.spreadMaterial = frames[0].getItem().getType();
        this.blockerMaterial = frames[1].getItem().getType();
        this.coreMaterial = frames[2].getItem().getType();
        this.pathMaterial = frames[3].getItem().getType();
        this.startPointGlassMaterial = frames[4].getItem().getType();
        this.endPointGlassMaterial = frames[5].getItem().getType();
    }
}
