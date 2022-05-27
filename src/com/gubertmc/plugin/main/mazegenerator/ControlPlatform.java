package com.gubertmc.plugin.main.mazegenerator;

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
    private final int size;
    private final double blockerPercentage;
    private final String algorithmName;
    private Block startButton;
    private Block rightButton1;
    private Block leftButton1;
    private Block rightButton2;
    private Block leftButton2;
    private Block rightButton3;
    private Block leftButton3;
    private Sign sizeSign;
    private Sign algorithmSign;
    private Sign percentageSign;
    private Material coreMaterial;
    private Material blockerMaterial;
    private Material spreadMaterial;
    private Material pathMaterial;
    private Material startPointGlassMaterial;
    private Material endPointGlassMaterial;
    private ItemFrame[] frames;


    public ControlPlatform(Block mazeLocationBlock, int size, double blockerPercentage, String algorithmName) {
        this.mazeLocationBlock = mazeLocationBlock;
        this.size = size;
        this.blockerPercentage = blockerPercentage;
        this.algorithmName = algorithmName;
        this.frames = new ItemFrame[6];
        this.coreMaterial = Material.RED_STAINED_GLASS;
        this.blockerMaterial = Material.LAPIS_BLOCK;
        this.spreadMaterial = Material.LIME_STAINED_GLASS;
        this.pathMaterial = Material.WHITE_STAINED_GLASS;
        this.startPointGlassMaterial = Material.GREEN_STAINED_GLASS;
        this.endPointGlassMaterial = Material.ORANGE_STAINED_GLASS;
    }

    /**
     * Spawns a control platform.
     * <p>
     * Logic for spawning the control platform outline, buttons, item-frames, signs, etc...
     */
    public void spawn() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 5; j++) {
                Location floor = new Location(
                        mazeLocationBlock.getWorld(),
                        mazeLocationBlock.getX() + i,
                        mazeLocationBlock.getY(),
                        mazeLocationBlock.getZ() - j - 2
                );
                floor.getBlock().setType(Material.STRIPPED_OAK_WOOD);
            }

            for (int k = 1; k < 5; k++) {
                Location wall = new Location(
                        mazeLocationBlock.getWorld(),
                        mazeLocationBlock.getX() + i,
                        mazeLocationBlock.getY() + k,
                        mazeLocationBlock.getZ() - 2
                );
                wall.getBlock().setType(Material.STRIPPED_OAK_WOOD);

                if (k == 3 && i > 1) {
                    if (i != 9 && i != 5) {
                        Location location = new Location(
                                mazeLocationBlock.getWorld(),
                                mazeLocationBlock.getX() + i,
                                mazeLocationBlock.getY() + k,
                                mazeLocationBlock.getZ() - 3
                        );

                        location.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_WALL_SIGN);
                        Sign sign = (Sign) location.getBlock().getRelative(BlockFace.WEST).getState();
                        switch (i) {
                            case 2, 6, 10 -> {
                                sign.setLine(1, "Scroll Right");
                                sign.setLine(2, "->");
                                sign.setColor(DyeColor.GREEN);
                            }
                            case 4, 8, 12 -> {
                                sign.setLine(1, "Scroll Left");
                                sign.setLine(2, "<-");
                                sign.setColor(DyeColor.GREEN);
                            }
                            case 3 -> {
                                sign.setLine(1, "Blocker");
                                sign.setLine(2, "Percentage");
                                sign.setColor(DyeColor.WHITE);
                            }
                            case 7 -> {
                                sign.setLine(1, "Size");
                                sign.setColor(DyeColor.WHITE);
                            }
                            case 11 -> {
                                sign.setLine(1, "Algorithm");
                                sign.setColor(DyeColor.WHITE);
                            }
                        }
                        sign.setGlowingText(true);
                        sign.update();
                    }
                }

                if (k == 4 && i == 3) {
                    Location location = new Location(
                            mazeLocationBlock.getWorld(),
                            mazeLocationBlock.getX() + i,
                            mazeLocationBlock.getY() + k,
                            mazeLocationBlock.getZ() - 3
                    );

                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.DARK_OAK_WALL_SIGN);
                    Sign sign = (Sign) location.getBlock().getRelative(BlockFace.WEST).getState();
                    sign.setLine(1, (int) (blockerPercentage * 100) + "%");
                    sign.setColor(DyeColor.YELLOW);
                    sign.setGlowingText(true);
                    sign.update();
                    percentageSign = sign;
                }

                if (k == 4 && i == 7) {
                    Location location = new Location(
                            mazeLocationBlock.getWorld(),
                            mazeLocationBlock.getX() + i,
                            mazeLocationBlock.getY() + k,
                            mazeLocationBlock.getZ() - 3
                    );

                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.DARK_OAK_WALL_SIGN);
                    Sign sign = (Sign) location.getBlock().getRelative(BlockFace.WEST).getState();
                    sign.setLine(1, size + "x" + size);
                    sign.setColor(DyeColor.YELLOW);
                    sign.setGlowingText(true);
                    sign.update();
                    sizeSign = sign;
                }

                if (k == 4 && i == 11) {
                    Location location = new Location(
                            mazeLocationBlock.getWorld(),
                            mazeLocationBlock.getX() + i,
                            mazeLocationBlock.getY() + k,
                            mazeLocationBlock.getZ() - 3
                    );

                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.DARK_OAK_WALL_SIGN);
                    Sign sign = (Sign) location.getBlock().getRelative(BlockFace.WEST).getState();
                    sign.setLine(1, algorithmName);
                    sign.setColor(DyeColor.YELLOW);
                    sign.setGlowingText(true);
                    sign.update();
                    algorithmSign = sign;
                }

                if (k == 2 && i > 4 && i < 12) {
                    if (i != 8) {
                        ItemFrame frame = mazeLocationBlock.getWorld().spawn(wall.add(0, 0, -1), ItemFrame.class);

                        switch (i) {
                            case 5 -> {
                                frame.setItem(new ItemStack(spreadMaterial));
                                frames[0] = frame;
                            }
                            case 6 -> {
                                frame.setItem(new ItemStack(blockerMaterial));
                                frames[1] = frame;
                            }
                            case 7 -> {
                                frame.setItem(new ItemStack(coreMaterial));
                                frames[2] = frame;
                            }
                            case 9 -> {
                                frame.setItem(new ItemStack(pathMaterial));
                                frames[3] = frame;
                            }
                            case 10 -> {
                                frame.setItem(new ItemStack(startPointGlassMaterial));
                                frames[4] = frame;
                            }
                            case 11 -> {
                                frame.setItem(new ItemStack(endPointGlassMaterial));
                                frames[5] = frame;
                            }
                        }
                    }
                }

                if (k == 1 && i > 1 && i != 3 && i != 5 && i != 9) {
                    Location location = new Location(
                            mazeLocationBlock.getWorld(),
                            mazeLocationBlock.getX() + i,
                            mazeLocationBlock.getY() + k,
                            mazeLocationBlock.getZ() - 3
                    );

                    location.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_WALL_SIGN);
                    Sign sign = (Sign) location.getBlock().getRelative(BlockFace.WEST).getState();
                    switch (i) {
                        case 2 -> {
                            sign.setLine(0, "By Gubert");
                            sign.setLine(2, "github.com/btror");
                            sign.setColor(DyeColor.MAGENTA);
                        }
                        case 4 -> {
                            sign.setLine(1, "Start/reset");
                            sign.setColor(DyeColor.ORANGE);
                        }
                        case 6 -> {
                            sign.setLine(1, "Spread");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 7 -> {
                            sign.setLine(1, "Walls/Blockers");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 8 -> {
                            sign.setLine(1, "Border/outline");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 10 -> {
                            sign.setLine(1, "2nd Path");
                            sign.setLine(2, "(AI only)");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 11 -> {
                            sign.setLine(1, "Start Coordinate");
                            sign.setColor(DyeColor.WHITE);
                        }
                        case 12 -> {
                            sign.setLine(1, "End Coordinate");
                            sign.setColor(DyeColor.WHITE);
                        }
                    }
                    sign.setGlowingText(true);
                    sign.update();
                }
            }
        }

        // Create start button.
        Location startButtonLocation = new Location(
                mazeLocationBlock.getWorld(),
                mazeLocationBlock.getX() + 4,
                mazeLocationBlock.getY() + 2,
                mazeLocationBlock.getZ() - 3
        );
        startButtonLocation.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
        startButton = startButtonLocation.getBlock().getRelative(BlockFace.WEST);

        // Scroll-right button 1.
        Location rightButtonLoc1 = new Location(
                mazeLocationBlock.getWorld(),
                mazeLocationBlock.getX() + 6,
                mazeLocationBlock.getY() + 4,
                mazeLocationBlock.getZ() - 3
        );
        rightButtonLoc1.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
        rightButton1 = rightButtonLoc1.getBlock().getRelative(BlockFace.WEST);

        // Scroll-right button 2.
        Location rightButtonLoc2 = new Location(
                mazeLocationBlock.getWorld(),
                mazeLocationBlock.getX() + 10,
                mazeLocationBlock.getY() + 4,
                mazeLocationBlock.getZ() - 3
        );
        rightButtonLoc2.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
        rightButton2 = rightButtonLoc2.getBlock().getRelative(BlockFace.WEST);

        // Scroll-right button 3.
        Location rightButtonLoc3 = new Location(
                mazeLocationBlock.getWorld(),
                mazeLocationBlock.getX() + 2,
                mazeLocationBlock.getY() + 4,
                mazeLocationBlock.getZ() - 3
        );
        rightButtonLoc3.getBlock().getRelative(BlockFace.WEST).setType(Material.WARPED_BUTTON);
        rightButton3 = rightButtonLoc3.getBlock().getRelative(BlockFace.WEST);

        // Scroll-left button 1.
        Location leftButtonLoc1 = new Location(
                mazeLocationBlock.getWorld(),
                mazeLocationBlock.getX() + 8,
                mazeLocationBlock.getY() + 4,
                mazeLocationBlock.getZ() - 3
        );
        leftButtonLoc1.getBlock().getRelative(BlockFace.WEST).setType(Material.ACACIA_BUTTON);
        leftButton1 = leftButtonLoc1.getBlock().getRelative(BlockFace.WEST);

        // Scroll-left button 2.
        Location leftButtonLoc2 = new Location(
                mazeLocationBlock.getWorld(),
                mazeLocationBlock.getX() + 12,
                mazeLocationBlock.getY() + 4,
                mazeLocationBlock.getZ() - 3
        );
        leftButtonLoc2.getBlock().getRelative(BlockFace.WEST).setType(Material.ACACIA_BUTTON);
        leftButton2 = leftButtonLoc2.getBlock().getRelative(BlockFace.WEST);

        // Scroll-left button 3.
        Location leftButtonLoc3 = new Location(
                mazeLocationBlock.getWorld(),
                mazeLocationBlock.getX() + 4,
                mazeLocationBlock.getY() + 4,
                mazeLocationBlock.getZ() - 3
        );
        leftButtonLoc3.getBlock().getRelative(BlockFace.WEST).setType(Material.ACACIA_BUTTON);
        leftButton3 = leftButtonLoc3.getBlock().getRelative(BlockFace.WEST);
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

    /**
     * Getter: get start button.
     *
     * @return maze start/reset button.
     */
    public Block getStartButton() {
        return startButton;
    }

    /**
     * Getter: get scroll-left button 1.
     *
     * @return scroll-left button 1.
     */
    public Block getLeftButton1() {
        return leftButton1;
    }

    /**
     * Getter: get scroll-right button 1.
     *
     * @return scroll-right button 1.
     */
    public Block getRightButton1() {
        return rightButton1;
    }

    /**
     * Getter: get scroll-left button 2.
     *
     * @return scroll-left button 2.
     */
    public Block getLeftButton2() {
        return leftButton2;
    }

    /**
     * Getter: get scroll-right button 2.
     *
     * @return scroll-right button 2.
     */
    public Block getRightButton2() {
        return rightButton2;
    }

    /**
     * Getter: get scroll-left button 3.
     *
     * @return scroll-left button 3.
     */
    public Block getLeftButton3() {
        return leftButton3;
    }

    /**
     * Getter: get scroll-right button 3.
     *
     * @return scroll-right button 3.
     */
    public Block getRightButton3() {
        return rightButton3;
    }

    /**
     * Getter: get maze size sign.
     *
     * @return maze size sign.
     */
    public Sign getSizeSign() {
        return sizeSign;
    }

    /**
     * Getter: get maze blocker percentage sign.
     *
     * @return maze blocker percentage sign.
     */
    public Sign getPercentageSign() {
        return percentageSign;
    }

    /**
     * Getter: get maze algorithm sign.
     *
     * @return maze algorithm sign.
     */
    public Sign getAlgorithmSign() {
        return algorithmSign;
    }

    /**
     * Setter: set maze size sign.
     *
     * @param sign maze size sign.
     */
    public void setSizeSign(Sign sign) {
        sizeSign = sign;
    }

    /**
     * Setter: set algorithm sign.
     *
     * @param sign maze algorithm sign.
     */
    public void setAlgorithmSign(Sign sign) {
        algorithmSign = sign;
    }

    /**
     * Setter: set blocker percentage sign.
     *
     * @param sign maze blocker percentage sign.
     */
    public void setPercentageSign(Sign sign) {
        percentageSign = sign;
    }
}
