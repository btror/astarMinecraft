package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Food {

    private Block arenaLocationBlock;
    private int x, y;
    private boolean isEaten;
    private Location foodLocation;
    private Material foodMaterial;

    public Food(Block arenaLocationBlock, int x, int y, boolean isEaten) {
        this.arenaLocationBlock = arenaLocationBlock;
        this.x = x;
        this.y = y;
        this.isEaten = isEaten;

        this.foodMaterial = Material.YELLOW_WOOL;
    }

    public void spawn() {
        foodLocation = new Location(
                arenaLocationBlock.getWorld(),
                arenaLocationBlock.getX() + x,
                arenaLocationBlock.getY(),
                arenaLocationBlock.getZ() + y
        );

        foodLocation.getBlock().setType(foodMaterial);
    }

    public boolean checkFoodStatus() {
        if (foodLocation.getBlock().getType() != foodMaterial) {
            return true;
        }
        return false;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public Location getLocation() { return foodLocation; }

    public Material getFoodMaterial() { return foodMaterial; }

    public boolean getStatus() { return isEaten; }

    public void setStatus(boolean isEaten) { this.isEaten = isEaten; }
}
