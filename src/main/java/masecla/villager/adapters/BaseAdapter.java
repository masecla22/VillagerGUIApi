package masecla.villager.adapters;

import org.bukkit.entity.Player;

import masecla.villager.classes.VillagerInventory;

public abstract class BaseAdapter {

	public VillagerInventory toAdapt;

	public BaseAdapter(VillagerInventory toAdapt) {
		super();
		this.toAdapt = toAdapt;
	}

	public abstract void openFor(Player p);
}
