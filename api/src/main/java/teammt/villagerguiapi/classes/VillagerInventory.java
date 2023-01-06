package teammt.villagerguiapi.classes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import teammt.villagerguiapi.api.MainLoader;

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
		try {
			MainLoader.getAdapter().getConstructor(VillagerInventory.class).newInstance(this).openFor(forWho);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
}
