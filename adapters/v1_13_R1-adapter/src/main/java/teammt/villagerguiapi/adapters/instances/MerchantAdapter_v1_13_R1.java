package teammt.villagerguiapi.adapters.instances;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import teammt.villagerguiapi.adapters.BaseAdapter;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;
import teammt.villagerguiapi.events.VillagerInventoryCloseEvent;
import teammt.villagerguiapi.events.VillagerInventoryModifyEvent;
import teammt.villagerguiapi.events.VillagerInventoryOpenEvent;
import teammt.villagerguiapi.events.VillagerTradeCompleteEvent;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.ChatComponentText;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.IMerchant;
import net.minecraft.server.v1_13_R1.MerchantRecipe;
import net.minecraft.server.v1_13_R1.MerchantRecipeList;
import net.minecraft.server.v1_13_R1.World;

public class MerchantAdapter_v1_13_R1 extends BaseAdapter implements IMerchant {

	public MerchantAdapter_v1_13_R1(VillagerInventory toAdapt) {
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

	@Override
	public void a(net.minecraft.server.v1_13_R1.ItemStack arg0) {
		VillagerInventoryModifyEvent event = new VillagerInventoryModifyEvent(toAdapt, toAdapt.getForWho(),
				CraftItemStack.asBukkitCopy(arg0));
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	@Override
	public MerchantRecipeList getOffers(EntityHuman arg0) {
		MerchantRecipeList rpl = new MerchantRecipeList();
		for (VillagerTrade trd : toAdapt.getTrades()) {
			MerchantRecipe toAdd = null;

			net.minecraft.server.v1_13_R1.ItemStack itm1 = CraftItemStack.asNMSCopy(trd.getItemOne());
			net.minecraft.server.v1_13_R1.ItemStack itm2 = null;
			net.minecraft.server.v1_13_R1.ItemStack result = CraftItemStack.asNMSCopy(trd.getResult());

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
	public EntityHuman getTrader() {
		return ((CraftPlayer) toAdapt.getForWho()).getHandle();
	}

	@Override
	public void setTradingPlayer(EntityHuman arg0) {
		if (arg0 == null) {
			VillagerInventoryCloseEvent event = new VillagerInventoryCloseEvent(toAdapt, toAdapt.getForWho());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	@Override
	public World getWorld() {
		return ((CraftWorld) toAdapt.getForWho().getWorld()).getHandle();
	}

	@Override
	public BlockPosition getPosition() {
		return new BlockPosition(0, 0, 0);
	}

	@Override
	public void openFor(Player p) {
		((CraftPlayer) p).getHandle().openTrade(this);
		VillagerInventoryOpenEvent event = new VillagerInventoryOpenEvent(toAdapt, toAdapt.getForWho());
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

}
