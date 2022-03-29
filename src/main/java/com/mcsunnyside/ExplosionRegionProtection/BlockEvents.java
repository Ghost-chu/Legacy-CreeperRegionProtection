package com.mcsunnyside.ExplosionRegionProtection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockEvents implements Listener {
    boolean ignoreEvent = false;
    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e){
        if(!e.getBlock().getType().equals(Material.SEA_LANTERN))
            return;
        ItemStack itemStack = e.getItemInHand();
        itemStack = itemStack.clone();
        itemStack.setAmount(1);
        if(!itemStack.equals(Main.instance.createWidgetItemStack()))
            return;
        if(!Main.instance.getWidgetManager().addWidget(e.getBlock().getLocation(), e.getPlayer().getUniqueId(),true)) {
            return;
        }
        e.getPlayer().sendMessage(ChatColor.GREEN+"区域性爆炸保护方块已部署成功");
        new BukkitRunnable(){
            @Override
            public void run() {
                Main.instance.updateDataFile();
            }
        }.runTaskAsynchronously(Main.instance);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e){
        if(ignoreEvent)
            return;
        if(!e.getBlock().getType().equals(Material.SEA_LANTERN))
            return;
        if(!Main.instance.getWidgetManager().removeWidget(e.getBlock().getLocation())) {
            return;
        }
        e.setDropItems(false);
        e.setCancelled(true);
        BlockBreakEvent be=  new BlockBreakEvent(e.getBlock(), e.getPlayer());
        ignoreEvent = true;
        Bukkit.getPluginManager().callEvent(be);
        ignoreEvent = false;
        if(be.isCancelled())
            return;
        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Main.instance.createWidgetItemStack());
        e.getBlock().setType(Material.AIR);
        e.getPlayer().sendMessage(ChatColor.GREEN+"区域性爆炸保护方块已移除成功");
        new BukkitRunnable(){
            @Override
            public void run() {
                Main.instance.updateDataFile();
            }
        }.runTaskAsynchronously(Main.instance);
    }
}
