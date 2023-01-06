package teammt.villagerguiapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;

public class VillagerTradeCompleteEvent extends Event {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private VillagerInventory inventory;
	private Player player;
	private VillagerTrade trade;

	public VillagerTradeCompleteEvent(VillagerInventory inventory, Player player, VillagerTrade trade) {
		super();
		this.inventory = inventory;
		this.player = player;
		this.trade = trade;
	}

	public VillagerInventory getInventory() {
		return inventory;
	}

	public Player getPlayer() {
		return player;
	}

	public VillagerTrade getTrade() {
		return trade;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

}
