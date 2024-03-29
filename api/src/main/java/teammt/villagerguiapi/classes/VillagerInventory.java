package teammt.villagerguiapi.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import teammt.villagerguiapi.api.AdapterLoader;

public class VillagerInventory {
	private List<VillagerTrade> trades = new ArrayList<>();
	private String name = "Sample text";
	private Player forWho;

	public VillagerInventory(List<VillagerTrade> trades, Player forWho) {
		super();
		this.trades = trades;
		this.forWho = forWho;
	}

	public VillagerInventory() {
	}

	public Player getForWho() {
		return forWho;
	}

	public void setForWho(Player forWho) {
		this.forWho = forWho;
	}

	public List<VillagerTrade> getTrades() {
		return trades;
	}

	public void setTrades(List<VillagerTrade> trades) {
		this.trades = trades;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void open() {
		AdapterLoader.open(this);
	}
}
