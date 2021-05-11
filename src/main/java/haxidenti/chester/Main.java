package haxidenti.chester;

import haxidenti.homio.HomesConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, Runnable {

    HomesConfig homesConfig;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 100L, 1200L); // every 60 seconds (1200L)
        homesConfig = new HomesConfig(getDataFolder());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void run() {
        Server server = getServer();
        for (World world : server.getWorlds()) {
            Location location = Chester.spawnChests(world, 10000);
            if (location != null) {
                location.add(0, 2, 0);
                getServer().getOnlinePlayers().forEach(p -> {
                    Chester.sendPlayerMessageSpawnInfo(p, location);
                    if (Chester.rand(0, 5) == 0) {
                        String pointName = "treasure_" + world.getName();
                        homesConfig.setTPointFor(p.getName(), pointName, location);
                        homesConfig.giveMagicStickFor(p, pointName, true);
                        homesConfig.removeTPointFor(p.getName(), pointName);
                    }
                });
            }
        }
    }
}