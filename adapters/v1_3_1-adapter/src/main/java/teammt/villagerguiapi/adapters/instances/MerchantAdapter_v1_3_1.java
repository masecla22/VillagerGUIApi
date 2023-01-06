package teammt.villagerguiapi.adapters.instances;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import teammt.villagerguiapi.adapters.BaseAdapter;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;
import teammt.villagerguiapi.events.VillagerInventoryCloseEvent;
import teammt.villagerguiapi.events.VillagerInventoryModifyEvent;
import teammt.villagerguiapi.events.VillagerInventoryOpenEvent;
import teammt.villagerguiapi.events.VillagerTradeCompleteEvent;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IMerchant;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.MerchantRecipeList;

public class MerchantAdapter_v1_3_1 extends BaseAdapter implements IMerchant, Listener {

	public MerchantAdapter_v1_3_1(VillagerInventory toAdapt) {
		super(toAdapt);
		Bukkit.getServer().getPluginManager().registerEvents(this,
				Bukkit.getPluginManager().getPlugin("VillagerGUIApi"));
	}

	@Override
	public void a(MerchantRecipe arg0) {
		try {
			Field f = arg0.getClass().getDeclaredField("maxUses");
			f.setAccessible(true);
			int maxUses = f.getInt(arg0);
			ItemStack buyItem1 = new CraftItemStack(arg0.getBuyItem1());
			ItemStack buyItem2 = new CraftItemStack(arg0.getBuyItem2());
			ItemStack buyItem3 = new CraftItemStack(arg0.getBuyItem3());
			VillagerTrade trd = new VillagerTrade(buyItem1, buyItem2, buyItem3, maxUses);
			VillagerTradeCompleteEvent event = new VillagerTradeCompleteEvent(toAdapt, toAdapt.getForWho(), trd);
			Bukkit.getServer().getPluginManager().callEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MerchantRecipeList getOffers(EntityHuman arg0) {
		MerchantRecipeList rpl = new MerchantRecipeList();
		for (VillagerTrade trd : toAdapt.getTrades()) {
			MerchantRecipe toAdd = null;
			net.minecraft.server.ItemStack itm1 = CraftItemStack.createNMSItemStack(trd.getItemOne());
			net.minecraft.server.ItemStack itm2 = null;
			net.minecraft.server.ItemStack result = CraftItemStack.createNMSItemStack(trd.getResult());
			if (trd.requiresTwoItems())
				itm2 = CraftItemStack.createNMSItemStack(trd.getItemTwo());
			else
				itm2 = CraftItemStack.createNMSItemStack(new ItemStack(Material.AIR));

			toAdd = new MerchantRecipe(itm1, itm2, result);
			try {
				Field f = toAdd.getClass().getDeclaredField("maxUses");
				f.setAccessible(true);
				f.setInt(toAdd, trd.getMaxUses());
			} catch (Exception e) {
			}

			rpl.add(toAdd);
		}
		return rpl;
	}

	@Override
	public void openFor(Player p) {
		((CraftPlayer) p).getHandle().openTrade(this);
		VillagerInventoryOpenEvent event = new VillagerInventoryOpenEvent(toAdapt, toAdapt.getForWho());
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public void a_(EntityHuman arg0) {
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getUniqueId().equals(this.toAdapt.getForWho().getUniqueId())) {
			VillagerInventoryModifyEvent modifyEvent = new VillagerInventoryModifyEvent(toAdapt,
					(Player) event.getWhoClicked(), event.getCurrentItem());
			Bukkit.getPluginManager().callEvent(modifyEvent);
			if (event.getRawSlot() == -999)
				return;
			if (event.getRawSlot() == 2 && !event.getCurrentItem().getType().equals(Material.AIR)) {
				ItemStack itemOne = this.toAdapt.getForWho().getOpenInventory().getTopInventory().getItem(0);
				ItemStack itemTwo = this.toAdapt.getForWho().getOpenInventory().getTopInventory().getItem(1);
				ItemStack result = event.getCurrentItem();
				VillagerTradeCompleteEvent completeEvent = new VillagerTradeCompleteEvent(toAdapt,
						(Player) event.getWhoClicked(), new VillagerTrade(itemOne, itemTwo, result, -1));
				Bukkit.getPluginManager().callEvent(completeEvent);
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer().getUniqueId().equals(this.toAdapt.getForWho().getUniqueId())) {
			VillagerInventoryCloseEvent closeEvent = new VillagerInventoryCloseEvent(toAdapt,
					(Player) event.getPlayer());
			Bukkit.getPluginManager().callEvent(closeEvent);
			HandlerList.unregisterAll(this); // Kill this event listener
		}
	}

	@Override
	public EntityHuman l_() {
		return ((CraftPlayer) toAdapt.getForWho()).getHandle();
	}

}
