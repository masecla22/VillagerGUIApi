package teammt.villagerguiapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import teammt.villagerguiapi.classes.VillagerInventory;

public class VillagerInventoryModifyEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private VillagerInventory inventory;
	private Player player;
	private ItemStack itemChanged;

	public VillagerInventoryModifyEvent(VillagerInventory inventory, Player player, ItemStack itemChanged) {
		super();
		this.inventory = inventory;
		this.player = player;
		this.itemChanged = itemChanged;
	}

	public VillagerInventory getInventory() {
		return inventory;
	}

	public Player getPlayer() {
		return player;
	}

	public ItemStack getItemChanged() {
		return itemChanged;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

}
