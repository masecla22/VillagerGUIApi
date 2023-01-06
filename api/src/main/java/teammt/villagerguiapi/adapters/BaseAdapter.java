package teammt.villagerguiapi.adapters;

import org.bukkit.entity.Player;

import teammt.villagerguiapi.classes.VillagerInventory;

public abstract class BaseAdapter {

    public VillagerInventory toAdapt;

    public BaseAdapter(VillagerInventory toAdapt) {
        super();
        this.toAdapt = toAdapt;
    }

    public abstract void openFor(Player p);
}
