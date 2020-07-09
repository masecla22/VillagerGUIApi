package masecla.villager.api;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import masecla.villager.adapters.BaseAdapter;
import masecla.villager.events.VillagerInventoryCloseEvent;
import masecla.villager.events.VillagerInventoryModifyEvent;
import masecla.villager.events.VillagerInventoryOpenEvent;
import masecla.villager.events.VillagerTradeCompleteEvent;

public class MainLoader extends JavaPlugin {

	private static Class<? extends BaseAdapter> versionAdapter = null;
	private AdapterLoader loader = null;

	@Override
	public void onEnable() {
		super.onEnable();

		try {
			loader = new AdapterLoader(this);
			loader.reflectivelyLoad();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}

		this.getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onClose(VillagerInventoryCloseEvent event) {
				event.getPlayer().sendMessage("You closed your inventory!");
			}

			@EventHandler
			public void onModify(VillagerInventoryModifyEvent event) {
				event.getPlayer().sendMessage("You modified your inventory!");
			}

			@EventHandler
			public void onTrade(VillagerTradeCompleteEvent event) {
				event.getPlayer().sendMessage("You completed a trade");
			}

			@EventHandler
			public void onOpen(VillagerInventoryOpenEvent event) {
				event.getPlayer().sendMessage("Opened an inventory!");
			}

		}, this);
	}

	@Override
	public void onDisable() {
		loader.close();
	}

	protected void swapAdapter(Class<? extends BaseAdapter> adapter) {
		versionAdapter = adapter;
	}

	public static Class<? extends BaseAdapter> getAdapter() {
		return versionAdapter;
	}

}
