package teammt.villagerguiapi.adapters.instances;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import teammt.villagerguiapi.adapters.BaseAdapter;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;
import teammt.villagerguiapi.events.VillagerInventoryCloseEvent;
import teammt.villagerguiapi.events.VillagerInventoryModifyEvent;
import teammt.villagerguiapi.events.VillagerInventoryOpenEvent;
import teammt.villagerguiapi.events.VillagerTradeCompleteEvent;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.IMerchant;
import net.minecraft.server.v1_7_R4.MerchantRecipe;
import net.minecraft.server.v1_7_R4.MerchantRecipeList;

public class MerchantAdapter_v1_7_R4 extends BaseAdapter implements IMerchant {

	public MerchantAdapter_v1_7_R4(VillagerInventory toAdapt) {
		super(toAdapt);
	}

	@Override
	public void a(MerchantRecipe arg0) {
		VillagerTrade trd = new VillagerTrade(CraftItemStack.asBukkitCopy(arg0.getBuyItem1()),
				CraftItemStack.asBukkitCopy(arg0.getBuyItem2()), CraftItemStack.asBukkitCopy(arg0.getBuyItem3()),
				arg0.maxUses);
		VillagerTradeCompleteEvent event = new VillagerTradeCompleteEvent(toAdapt, toAdapt.getForWho(), trd);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MerchantRecipeList getOffers(EntityHuman arg0) {
		MerchantRecipeList rpl = new MerchantRecipeList();
		for (VillagerTrade trd : toAdapt.getTrades()) {
			MerchantRecipe toAdd = null;
			net.minecraft.server.v1_7_R4.ItemStack itm1 = CraftItemStack.asNMSCopy(trd.getItemOne());
			net.minecraft.server.v1_7_R4.ItemStack itm2 = null;
			net.minecraft.server.v1_7_R4.ItemStack result = CraftItemStack.asNMSCopy(trd.getResult());
			if (trd.requiresTwoItems())
				itm2 = CraftItemStack.asNMSCopy(trd.getItemTwo());
			else
				itm2 = CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));

			toAdd = new MerchantRecipe(itm1, itm2, result);
			toAdd.maxUses = trd.getMaxUses();

			rpl.add(toAdd);
		}
		return rpl;
	}

	@Override
	public void openFor(Player p) {
		((CraftPlayer) p).getHandle().openTrade(this, toAdapt.getName());
		VillagerInventoryOpenEvent event = new VillagerInventoryOpenEvent(toAdapt, toAdapt.getForWho());
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public void a_(EntityHuman arg0) {
		if (arg0 == null) {
			VillagerInventoryCloseEvent event = new VillagerInventoryCloseEvent(toAdapt, toAdapt.getForWho());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	/**
	 * Occurs when changing trades, the itemstack is the result of the new trade
	 * Enough to send an update event with the result.
	 */
	@Override
	public void a_(net.minecraft.server.v1_7_R4.ItemStack arg0) {
		VillagerInventoryModifyEvent event = new VillagerInventoryModifyEvent(toAdapt, toAdapt.getForWho(),
				CraftItemStack.asBukkitCopy(arg0));
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public EntityHuman b() {
		return ((CraftPlayer) toAdapt.getForWho()).getHandle();
	}

}
