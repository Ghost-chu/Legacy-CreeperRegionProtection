package com.mcsunnyside.ExplosionRegionProtection;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class ProtectWidget {
    private double x;
    private double y;
    private double z;
    private String world;
    @Getter
    private UUID creator;
    public ProtectWidget(Location location, UUID owner){
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.world = location.getWorld().getName();
        this.creator = owner;
    }
    public Location getLocation(){
        return new Location(Bukkit.getWorld(world),x,y,z);
    }
}
