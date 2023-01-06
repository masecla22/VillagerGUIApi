package teammt.villagerguiapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import teammt.villagerguiapi.classes.VillagerInventory;

public class VillagerInventoryCloseEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();

	private VillagerInventory inventory;
	private Player player;

	public VillagerInventoryCloseEvent(VillagerInventory inventory, Player player) {
		super();
		this.inventory = inventory;
		this.player = player;
	}

	public VillagerInventory getInventory() {
		return inventory;
	}

	public Player getPlayer() {
		return player;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
