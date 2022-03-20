package com.gubertmc.maze.outline;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public class MazeOutline2d {

    private long time;
    private final int size;

    public MazeOutline2d(int size) {
        time = 0;
        this.size = size;
    }

    public void generateFloorSpace(PlayerInteractEvent e) {
        time += 5L;
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location floor = new Location(e.getPlayer().getWorld(), e.getClickedBlock().getX() + i, e.getClickedBlock().getY(), e.getClickedBlock().getZ() + j);
                runnableDelayed(floor, time, GROUND_MATERIAL, -1, -1);

                Location locationAboveFloor = new Location(e.getPlayer().getWorld(), e.getClickedBlock().getX() + i, e.getClickedBlock().getY() + 1, e.getClickedBlock().getZ() + j);
                runnableDelayed(locationAboveFloor, time, Material.AIR, i, j);

                Location debris = new Location(e.getPlayer().getWorld(), e.getClickedBlock().getX() + i, e.getClickedBlock().getY() + 2, e.getClickedBlock().getZ() + j);
                runnableDelayed(debris, time, Material.AIR, -1, -1);

                if (count % 80 == 0) { // was 50
                    time += 5L;
                }
                count++;
            }
            if (i % 12 == 0) { // was 25
                time += 5L;
            }
        }
        time += 5L;
    }
}
