package masecla.villager.classes;

import org.bukkit.inventory.ItemStack;

public class VillagerTrade {
	private ItemStack itemOne, itemTwo, result;
	private int maxUses;

	public VillagerTrade(ItemStack itemOne, ItemStack itemTwo, ItemStack result, int maxUses) {
		super();
		this.itemOne = itemOne;
		this.itemTwo = itemTwo;
		this.result = result;
		this.maxUses = maxUses;
	}

	public VillagerTrade(ItemStack itemOne, ItemStack result, int maxUses) {
		this(itemOne, null, result, maxUses);
	}

	public int getMaxUses() {
		return maxUses;
	}

	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}

	public boolean requiresTwoItems() {
		return itemTwo != null;
	}

	public ItemStack getItemOne() {
		return itemOne;
	}

	public void setItemOne(ItemStack itemOne) {
		this.itemOne = itemOne;
	}

	public ItemStack getItemTwo() {
		return itemTwo;
	}

	public void setItemTwo(ItemStack itemTwo) {
		this.itemTwo = itemTwo;
	}

	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

}
