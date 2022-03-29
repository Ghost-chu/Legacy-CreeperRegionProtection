package com.mcsunnyside.ExplosionRegionProtection;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent e){
        for(ProtectWidget widget : Main.instance.getWidgetManager().getAllWidgets(e.getWorld())){
            if(!widget.getLocation().getWorld().equals(e.getWorld()))
                continue;
            for (Chunk chunk : e.getWorld().getLoadedChunks()){
                if(widget.getLocation().getChunk().equals(chunk))
                    Main.instance.getWidgetManager().loadWidget(widget);
            }
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent e){
        for(ProtectWidget widget : Main.instance.getWidgetManager().getLoadedWidgets(e.getWorld())){
            if(!widget.getLocation().getWorld().equals(e.getWorld()))
                continue;
            Main.instance.getWidgetManager().unloadWidget(widget);
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent e){
        for(ProtectWidget widget : Main.instance.getWidgetManager().getAllWidgets(e.getWorld())){
            if(widget.getLocation().getChunk().equals(e.getChunk()))
                Main.instance.getWidgetManager().loadWidget(widget);
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onChunkUnoad(ChunkUnloadEvent e){
        for(ProtectWidget widget : Main.instance.getWidgetManager().getLoadedWidgets(e.getWorld())){
            if(!widget.getLocation().getChunk().equals(e.getChunk()))
                continue;
            Main.instance.getWidgetManager().unloadWidget(widget);
        }
    }

}
