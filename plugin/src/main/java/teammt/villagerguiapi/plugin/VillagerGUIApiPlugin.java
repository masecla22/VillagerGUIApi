package teammt.villagerguiapi.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import teammt.villagerguiapi.api.AdapterLoader;

public class VillagerGUIApiPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        AdapterLoader.init();
    }
}
