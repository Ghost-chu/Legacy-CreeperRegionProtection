package com.mcsunnyside.ExplosionRegionProtection;

import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Getter
public class Main extends JavaPlugin implements Listener {
    WidgetManager widgetManager;
    YamlConfiguration data;
    File dataFile;
    Gson gson = new Gson();
    public static Main instance;

    @Override
    public void onLoad() {
        instance=this;
    }

    @Override
    @SneakyThrows
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this
                , this);
        Bukkit.getPluginManager().registerEvents(new BlockEvents()
                , this);
        Bukkit.getPluginManager().registerEvents(new WorldEvents()
                , this);
        Bukkit.getPluginManager().registerEvents(new ExplosionEvents()
                , this);
        widgetManager = new WidgetManager(this);
        dataFile = new File(getDataFolder(), "data.yml");
        if(!dataFile.getParentFile().exists())
            dataFile.getParentFile().mkdirs();
        if(!dataFile.exists())
            dataFile.createNewFile();
        data = new YamlConfiguration();
        try {
            data.load(dataFile);
            if(data.getString("data") == null || data.getString("data").isEmpty())
                data.set("data", "{}");
            data.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }
        this.getWidgetManager().setWidgets(gson.fromJson(data.getString("data"), StoragedData.class).getWidgets());
        if(this.getWidgetManager().getWidgets() == null)
            this.getWidgetManager().setWidgets(new ArrayList<>());
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("bukkit"),createWidgetItemStack());
        recipe.shape("$$$","$#$","$$$");
        recipe.setIngredient('$', Material.WATER_BUCKET);
        recipe.setIngredient('#', Material.SEA_LANTERN);
        try {
            getServer().addRecipe(recipe);
        }catch (Throwable t){
            getLogger().info("重复合成注册，已跳过");
        }
        getLogger().info("区域性爆炸保护插件已启动");
        this.getWidgetManager().getLoadedWidgets().clear();
        /* 检查已经加载的世界和区块 载入数据 */
        new BukkitRunnable(){
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()){
                    for (ProtectWidget widget : getWidgetManager().getWidgets()){
                        if(!widget.getLocation().getWorld().getName().equals(world.getName()))
                            continue;
                        if(widget.getLocation().getChunk().isLoaded()){
                            if(widget.getLocation().getBlock().getType()!=Material.SEA_LANTERN){
                                getLogger().info("Removing nolonger exist widget at "+widget.getLocation().toString());
                                getWidgetManager().removeWidget(widget.getLocation());
                            }
                        }
                        getWidgetManager().getLoadedWidgets().add(widget);
                    }
                }
            }
        }.runTaskLater(this, 1);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以执行此命令");
            return true;
        }
        Player player = (Player)sender;
        if(this.getWidgetManager().isAnyRange(player.getLocation())){
            sender.sendMessage(ChatColor.YELLOW+"检查结果：Yes 您所处的位置受到附近的区域爆炸保护方块保护");
        }else{
            sender.sendMessage(ChatColor.YELLOW+"检查结果：No 您所处的位置没有区域爆炸保护方块保护");
        }
        return true;
    }

    @SneakyThrows
    void updateDataFile(){
        data.set("data", gson.toJson(new StoragedData(this.getWidgetManager().getWidgets())));
        data.save(this.dataFile);
    }

    ItemStack createWidgetItemStack(){
        ItemStack itemStack = new ItemStack(Material.SEA_LANTERN);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA+"区域性爆炸保护方块");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE+"区域性爆炸保护方块，放置后，保护距离此方块的"+this.getWidgetManager().getMaxRange()+"距离内的方块免受爆炸破坏");
        lore.add(ChatColor.YELLOW+""+ChatColor.ITALIC+"让你们见识一下水的力量！");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
