package com.gubertmc.plugin.commands;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.ControlPlatform;
import com.gubertmc.plugin.main.Maze;
import com.gubertmc.plugin.main.mazes.custom.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public record MazeGeneratorCommand(MazeGeneratorPlugin plugin) implements CommandExecutor, Listener {

    private static Maze maze;
    private static Location mazeLocation;
    private static ControlPlatform controlPlatform;
    private static final String[] algorithms =
            {"A* 2D", "A* 3D", "Greedy BFS 2D", "Greedy BFS 3D", "Breadth-FS 2D", "Breadth-FS 3D", "Depth-FS 2D",
                    "Depth-FS 3D"};
    private static int index = 0;
    private static int size = 15;
    private static int originalSize;
    private static double blockerPercentage = 0.35;

    /**
     * Handle the command call.
     *
     * @param commandSender command sender.
     * @param command       command.
     * @param alias         alias.
     * @param args          args.
     * @return true.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        Player player = (Player) commandSender;
        try {
            Location location = player.getLocation();
            location = new Location(
                    location.getWorld(),
                    location.getX() + 1,
                    location.getY(),
                    location.getZ() + 1
            );
            mazeLocation = location;
            maze = new AstarMaze2D(
                    plugin,
                    location.getBlock(),
                    size,
                    blockerPercentage
            );

            player.sendMessage("Spawning control platform...");

            controlPlatform = new ControlPlatform(
                    player.getLocation().getBlock(),
                    size,
                    blockerPercentage,
                    algorithms[index]
            );
            controlPlatform.spawn();
            originalSize = size;

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "/maze");
        }

        return false;
    }

    /**
     * Generate a new maze.
     *
     * @param e player interaction.
     */
    @EventHandler
    public void onButtonPressed(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Objects.equals(e.getClickedBlock(), controlPlatform.getStartButton())) {
                e.getPlayer().sendMessage("Loading...");
                boolean acceptableFrames = true;
                ItemFrame[] frames = controlPlatform.getFrames();

                for (ItemFrame item : frames) {
                    if (!item.getItem().getType().isBlock()) {
                        acceptableFrames = false;
                    }
                }
                if (acceptableFrames) {
                    controlPlatform.setFrames(frames);
                    long time = 1L;
                    if (originalSize != size) {
                        for (int i = -1; i < originalSize + 1; i++) {
                            for (int j = -1; j < originalSize + 1; j++) {
                                for (int k = -1; k < originalSize + 1; k++) {
                                    Location location = new Location(
                                            maze.getMazeLocationBlock().getWorld(),
                                            maze.getMazeLocationBlock().getX() + i,
                                            maze.getMazeLocationBlock().getY() + k,
                                            maze.getMazeLocationBlock().getZ() + j
                                    );
                                    if (location.getBlock().getType() != Material.AIR) {
                                        maze.runnableDelayed(location, time, Material.AIR, -1, -1, -1);
                                    }
                                }
                            }
                            if (i % (int) (originalSize * .15) == 0) {
                                time += 2L;
                            }
                        }
                        time += 1L;
                        originalSize = size;
                    }
                    maze.generateNewMaze(
                            controlPlatform.getCoreMaterial(),
                            controlPlatform.getBlockerMaterial(),
                            controlPlatform.getSpreadMaterial(),
                            controlPlatform.getPathMaterial(),
                            controlPlatform.getStartPointGlassMaterial(),
                            controlPlatform.getEndPointGlassMaterial(),
                            time
                    );
                } else {
                    e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Maze items must be blocks...");
                }
            } else if (Objects.equals(e.getClickedBlock(), controlPlatform.getRightButton2())) {
                Sign sign = controlPlatform.getAlgorithmSign();
                if (index == algorithms.length - 1) {
                    index = 0;
                } else {
                    index++;
                }
                sign.setLine(1, algorithms[index]);
                sign.update();
                controlPlatform.setAlgorithmSign(sign);
            } else if (Objects.equals(e.getClickedBlock(), controlPlatform.getLeftButton2())) {
                Sign sign = controlPlatform.getAlgorithmSign();
                if (index == 0) {
                    index = algorithms.length - 1;
                } else {
                    index--;
                }
                sign.setLine(1, algorithms[index]);
                sign.update();
                controlPlatform.setAlgorithmSign(sign);
            } else if (Objects.equals(e.getClickedBlock(), controlPlatform.getRightButton1())) {
                Sign sign = controlPlatform.getSizeSign();
                sign.setLine(1, "" + (size + 1) + "x" + (size + 1));
                sign.update();
                size++;
                controlPlatform.setSizeSign(sign);
            } else if (Objects.equals(e.getClickedBlock(), controlPlatform.getLeftButton1())) {
                Sign sign = controlPlatform.getSizeSign();
                if (size > 7) {
                    sign.setLine(1, "" + (size - 1) + "x" + (size - 1));
                    sign.update();
                    size--;
                    controlPlatform.setSizeSign(sign);
                }
            } else if (Objects.equals(e.getClickedBlock(), controlPlatform.getRightButton3())) {
                Sign sign = controlPlatform.getPercentageSign();
                if (blockerPercentage < .80) {
                    sign.setLine(1, "" + (int) ((blockerPercentage * 100) + 1) + "%");
                    sign.update();
                    blockerPercentage += .01;
                    controlPlatform.setPercentageSign(sign);
                }
            } else if (Objects.equals(e.getClickedBlock(), controlPlatform.getLeftButton3())) {
                Sign sign = controlPlatform.getPercentageSign();
                if (blockerPercentage > 0) {
                    sign.setLine(1, "" + (int) ((blockerPercentage * 100) - 1) + "%");
                    sign.update();
                    blockerPercentage -= .01;
                    controlPlatform.setPercentageSign(sign);
                }
            }

            switch (index) {
                case 0 -> maze = new AstarMaze2D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
                case 1 -> maze = new AstarMaze3D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
                case 2 -> maze = new GreedyBestFirstSearchMaze2D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
                case 3 -> maze = new GreedyBestFirstSearchMaze3D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
                case 4 -> maze = new BreadthFirstSearchMaze2D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
                case 5 -> maze = new BreadthFirstSearchMaze3D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
                case 6 -> maze = new DepthFirstSearchMaze2D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
                case 7 -> maze = new DepthFirstSearchMaze3D(
                        plugin,
                        mazeLocation.getBlock(),
                        size,
                        blockerPercentage
                );
            }
        }
    }
}
