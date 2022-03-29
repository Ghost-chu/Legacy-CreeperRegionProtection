package com.mcsunnyside.ExplosionRegionProtection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e){
        if(Main.instance.getWidgetManager().isAnyRange(e.getBlock().getLocation()))
            e.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e){
        if(Main.instance.getWidgetManager().isAnyRange(e.getEntity().getLocation()))
            e.setCancelled(true);
    }
}
