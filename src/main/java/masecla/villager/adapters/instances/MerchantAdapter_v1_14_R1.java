package masecla.villager.adapters.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import masecla.villager.adapters.BaseAdapter;
import masecla.villager.classes.VillagerInventory;
import masecla.villager.classes.VillagerTrade;
import masecla.villager.events.VillagerInventoryOpenEvent;

public class MerchantAdapter_v1_14_R1 extends BaseAdapter implements Listener {

	private List<UUID> trading = new ArrayList<>();
	private Merchant wrapped = null;

	public MerchantAdapter_v1_14_R1(VillagerInventory toAdapt) {
		super(toAdapt);
		Bukkit.getServer().getPluginManager().registerEvents(this,
				Bukkit.getPluginManager().getPlugin("VillagerGUIApi"));

		wrapped = new Merchant() {
			@Override
			public MerchantRecipe getRecipe(int arg0) throws IndexOutOfBoundsException {
				return getRecipes().get(arg0);
			}

			@Override
			public int getRecipeCount() {
				return getRecipes().size();
			}

			@Override
			public List<MerchantRecipe> getRecipes() {
				List<MerchantRecipe> mrl = new ArrayList<>();
				for (VillagerTrade trd : toAdapt.getTrades()) {
					MerchantRecipe toAdd = new MerchantRecipe(trd.getResult(), 0, trd.getMaxUses(), false);
					toAdd.addIngredient(trd.getItemOne());
					if (trd.requiresTwoItems())
						toAdd.addIngredient(trd.getItemTwo());
					mrl.add(toAdd);
				}
				return mrl;
			}

			@Override
			public HumanEntity getTrader() {
				return toAdapt.getForWho();
			}

			@Override
			public boolean isTrading() {
				return false;
			}

			@Override
			public void setRecipe(int index, MerchantRecipe recipe) throws IndexOutOfBoundsException {
				List<VillagerTrade> adapterRecipes = toAdapt.getTrades();
				VillagerTrade adapted = new VillagerTrade(recipe.getIngredients().get(0),
						recipe.getIngredients().get(1), recipe.getResult(), recipe.getMaxUses());
				adapterRecipes.set(index, adapted);
				toAdapt.setTrades(adapterRecipes);
			}

			@Override
			public void setRecipes(List<MerchantRecipe> arg0) {
				List<VillagerTrade> result = new ArrayList<>();
				for (MerchantRecipe recipe : arg0) {
					VillagerTrade adapted = new VillagerTrade(recipe.getIngredients().get(0),
							recipe.getIngredients().get(1), recipe.getResult(), recipe.getMaxUses());
					result.add(adapted);
				}
				toAdapt.setTrades(result);
			}
		};

	}

	@Override
	public void openFor(Player p) {
		p.sendMessage("Cringe?");
		p.openMerchant(wrapped, true);
		trading.add(p.getUniqueId());
		VillagerInventoryOpenEvent event = new VillagerInventoryOpenEvent(toAdapt, p);
		Bukkit.getPluginManager().callEvent(event);
	}

}
