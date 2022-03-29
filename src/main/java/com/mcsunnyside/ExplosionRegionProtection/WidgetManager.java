package com.mcsunnyside.ExplosionRegionProtection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WidgetManager {
    private Main plugin;
    private List<ProtectWidget> widgets = new ArrayList<>();
    private List<ProtectWidget> loadedWidgets = new ArrayList<>();
    private final int maxRange = 32;
    public WidgetManager(Main plugin){
        this.plugin = plugin;
    }
    public void loadWidget(ProtectWidget widget){
        if(!loadedWidgets.contains(widget))
            loadedWidgets.add(widget);
    }
    public void unloadWidget(ProtectWidget widget){
        loadedWidgets.remove(widget);
    }
    public List<ProtectWidget> getLoadedWidgets(World world){
        List<ProtectWidget> list = new ArrayList<>();
        for (ProtectWidget widget : loadedWidgets){
            if(widget.getLocation().getWorld().equals(world))
                list.add(widget);
        }
        return list;
    }
    public List<ProtectWidget> getAllWidgets(World world){
        List<ProtectWidget> list = new ArrayList<>();
        for (ProtectWidget widget : widgets){
            if(widget.getLocation().getWorld().equals(world))
                list.add(widget);
        }
        return list;
    }
    public boolean isInRange(Location location, ProtectWidget widget){
        if(!location.getWorld().equals(widget.getLocation().getWorld()))
            return false;
        return location.distance(widget.getLocation())<=maxRange;
    }
    public boolean isAnyRange(Location location){
        for (ProtectWidget widget : loadedWidgets){
            if(isInRange(location, widget))
                return true;
        }
        return false;
    }
    public boolean addWidget(Location location, UUID owner, boolean loaded){
        for (ProtectWidget widget : widgets){
            if(widget.getLocation().equals(location)) {
                return false;
            }
        }
        widgets.add(new ProtectWidget(location, owner));
        if(!loaded)
            return true;
        for (ProtectWidget widget : loadedWidgets){
            if(widget.getLocation().equals(location)) {
                return false;
            }
        }
        loadedWidgets.add(new ProtectWidget(location, owner));
        return true;
    }
    public boolean removeWidget(Location location){
        boolean removed = false;
        for (ProtectWidget widget : widgets){
            if(!widget.getLocation().equals(location))
                continue;
            widgets.remove(widget);
            removed = true;
            break;
        }
        for (ProtectWidget widget : loadedWidgets){
            if(!widget.getLocation().equals(location))
                continue;
            loadedWidgets.remove(widget);
            removed = true;
            break;
        }
        return removed;
    }
}
