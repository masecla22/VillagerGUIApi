package masecla.villager.adapters.instances;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import masecla.villager.adapters.BaseAdapter;
import masecla.villager.classes.VillagerInventory;
import masecla.villager.classes.VillagerTrade;
import masecla.villager.events.VillagerInventoryCloseEvent;
import masecla.villager.events.VillagerInventoryModifyEvent;
import masecla.villager.events.VillagerInventoryOpenEvent;
import masecla.villager.events.VillagerTradeCompleteEvent;
import net.minecraft.server.v1_9_R1.ChatComponentText;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IMerchant;
import net.minecraft.server.v1_9_R1.MerchantRecipe;
import net.minecraft.server.v1_9_R1.MerchantRecipeList;

public class MerchantAdapter_v1_9_R1 extends BaseAdapter implements IMerchant {

	public MerchantAdapter_v1_9_R1(VillagerInventory toAdapt) {
		super(toAdapt);
	}

	@Override
	public void a(MerchantRecipe arg0) {
		VillagerTrade trd = new VillagerTrade(CraftItemStack.asBukkitCopy(arg0.getBuyItem1()),
				CraftItemStack.asBukkitCopy(arg0.getBuyItem2()), CraftItemStack.asBukkitCopy(arg0.getBuyItem3()),
				arg0.f());
		VillagerTradeCompleteEvent event = new VillagerTradeCompleteEvent(toAdapt, toAdapt.getForWho(), trd);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public void a(net.minecraft.server.v1_9_R1.ItemStack arg0) {
		VillagerInventoryModifyEvent event = new VillagerInventoryModifyEvent(toAdapt, toAdapt.getForWho(),
				CraftItemStack.asBukkitCopy(arg0));
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public MerchantRecipeList getOffers(EntityHuman arg0) {
		MerchantRecipeList rpl = new MerchantRecipeList();
		for (VillagerTrade trd : toAdapt.getTrades()) {
			MerchantRecipe toAdd = null;

			net.minecraft.server.v1_9_R1.ItemStack itm1 = CraftItemStack.asNMSCopy(trd.getItemOne());
			net.minecraft.server.v1_9_R1.ItemStack itm2 = null;
			net.minecraft.server.v1_9_R1.ItemStack result = CraftItemStack.asNMSCopy(trd.getResult());
			if (trd.requiresTwoItems())
				itm2 = CraftItemStack.asNMSCopy(trd.getItemTwo());
			else
				itm2 = CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));

			toAdd = new MerchantRecipe(itm1, itm2, result, 0, trd.getMaxUses());

			rpl.add(toAdd);
		}
		return rpl;
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName() {
		return new ChatComponentText(toAdapt.getName());
	}

	@Override
	public void openFor(Player p) {
		((CraftPlayer) p).getHandle().openTrade(this);
		VillagerInventoryOpenEvent event = new VillagerInventoryOpenEvent(toAdapt, toAdapt.getForWho());
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public void setTradingPlayer(EntityHuman arg0) {
		if (arg0 == null) {
			VillagerInventoryCloseEvent event = new VillagerInventoryCloseEvent(toAdapt, toAdapt.getForWho());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	@Override
	public EntityHuman t_() {
		return ((CraftPlayer) toAdapt.getForWho()).getHandle();
	}

}
