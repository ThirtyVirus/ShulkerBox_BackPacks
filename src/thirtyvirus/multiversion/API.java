package thirtyvirus.multiversion;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class API {

    public static Entity getEntity(UUID id) {
        for(World w : Bukkit.getWorlds()) {
            for(Entity entity : w.getEntities()) {
                if(entity.getUniqueId() == id) return entity;
            }
        }

        return null;
    }

    public static boolean isSlab(Material material) {
        return ((material.name().toLowerCase().contains("slab") || material.name().toLowerCase().contains("step")) && !material.name().toLowerCase().contains("double"));
    }

}
