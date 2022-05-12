package com.gubertmc.plugin.main.mazes.custom;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.algorithms.Animation;
import com.gubertmc.plugin.main.algorithms.Simulation;
import com.gubertmc.plugin.main.algorithms.astar.astar2d.AstarAnimation2D;
import com.gubertmc.plugin.main.algorithms.astar.astar2d.AstarSimulation2D;
import com.gubertmc.plugin.main.mazes.Maze2D;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getServer;

public class AstarMaze2D extends Maze2D {

    public AstarMaze2D(MazeGeneratorPlugin plugin, Block block, int size, double wallPercentage) {
        super(plugin, block, size, wallPercentage);
    }

    /**
     * Generate a new maze.
     *
     * @param coreMaterial            floor material for 2D | volume material for 3D.
     * @param blockerMaterial         wall/blocker material.
     * @param spreadMaterial          spread animation material.
     * @param pathMaterial            final path animation material (astar only).
     * @param startPointGlassMaterial material over the start coordinate (preferably glass).
     * @param endPointGlassMaterial   material over the end coordinate (preferably glass).
     */
    @Override
    public void generateNewMaze(
            Material coreMaterial,
            Material blockerMaterial,
            Material spreadMaterial,
            Material pathMaterial,
            Material startPointGlassMaterial,
            Material endPointGlassMaterial,
            long adjustedTime
    ) {
        setValid(false);
        int count = 0;
        while (!isValid()) {
            int[][][] simulationMaze = generateSimulation();
            Simulation simulation = new AstarSimulation2D(
                    simulationMaze, getStartCoordinate(), getEndCoordinate()
            );
            simulation.setup();
            setValid(simulation.start());

            if (isValid()) {
                setTime(0L);
                generateCore(coreMaterial, adjustedTime);
                // generateBorder(coreMaterial);
                clearOldBeacons();
                generateStartAndEndPoints(startPointGlassMaterial, endPointGlassMaterial);
                generateBlockedAreas(simulationMaze, blockerMaterial);

                long time = getTime();
                time += 5L;
                setTime(time);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Animation animation = new AstarAnimation2D(
                                getPlugin(),
                                getMazeBlockLocations(),
                                getStartCoordinate(),
                                getEndCoordinate(),
                                getSize(),
                                blockerMaterial,
                                pathMaterial,
                                spreadMaterial,
                                coreMaterial,
                                startPointGlassMaterial,
                                endPointGlassMaterial
                        );
                        animation.setup();
                        setValid(animation.start());
                        getServer().broadcastMessage(ChatColor.GREEN + "" + getSize() + "x" + getSize()
                                + " maze generated..."
                        );
                        animation.showAnimation(getTime());
                        cancel();
                    }
                }.runTaskTimer(getPlugin(), time, 20L);

                setTime(0);
            } else {
                count++;
                System.out.println("Invalid maze - starting new simulation...");
            }
            if (count == 50) {
                getServer().broadcastMessage(ChatColor.RED + "A maze could not be successfully generated within " +
                        "50 simulations. You may experience server lag. Creating a larger maze with a lower " +
                        "percentage of walls/blockers will greatly help and put less stress on the server."
                );
            }
        }
    }
}
