# VillagerGUI Api
Have you ever tried to create a custom Villager GUI without a villager in Spigot? You probably noticed the absolute insanity that it involves, and the complete and utter inability to port it to other versions. This library intends to fix this by having custom classes for easy access and events to create your personalized GUIs!

# Versions supported (as of now)
  - 1.8-1.8.3
  - 1.12.2

# Versions planned:
  - 1.8.4-1.8.8
  - 1.9-1.11
  - 1.13-1.16

# Goal of the project
The goal of this library is to provide a unified API for developers to create Villagers GUI Containers and have the events to manipulate them and create different plugin with it. 

# How does it work?
The way this project works is upon building it a certain package of classes is moved inside a zip inside a jar, which causes them to not load on startup of the server. Then the server loads an instance of the correct class depending on what version it is. The class is only loaded once on startup and is the only call to reflection ever made. Because of this, we have two advantages, our code remains readable, which helps up development (as no reflection is needed), and performance is really high. A disadvantage of this method is that we need to write a separate class for every single version so that the plugin knows what to load on startup.

# Final note
Some of the versions have inconsistent behaviour, which is all listed in the Notes section of the readme, in all the places where inconsistencies were found, I took the most reasonable approach, which is why some events might behave differently or not exist at all in certain versions. The goal of my choices when it comes to this is to maximise portability and to keep it straight forward. 


# Installation
todo

# Developer API
## Main classes
You (as a developer), mostly need 2 classes in order to work with the api, them being
```
masecla.villager.classes.VillagerInventory - Represents a player's inventory.
masecla.villager.classes.VillagerTrade - Represents a single trade object.
```
## Events
Please, please, read the Notes section, otherwise you might have issues with these events.
```
masecla.villager.events.VillagerInventoryCloseEvent - When an inventory gets closed
masecla.villager.events.VillagerInventoryModifyEvent - When an inventory gets modified (read notes)
masecla.villager.events.VillagerInventoryOpenEvent - When an inventory gets opened
masecla.villager.events.VillagerTradeCompleteEvent - When a trade is completed.
```

## Example
Here is an example of opening an inventory for a player with 3 trades. (The example was coded in 1.12.2. but should work for all versions)
```java
    List<VillagerTrade> trades = new ArrayList<>(); // Create a new list of items
    // Create a trade with 1 DIRT and 1 DIRT for GLASS with 10 max uses
    trades.add(new VillagerTrade(new ItemStack(Material.DIRT), new ItemStack(Material.DIRT),
            new ItemStack(Material.GLASS), 10));
    // Create a trade for 1 DIRT for 1 GLASS with 15 max uses
    trades.add(new VillagerTrade(new ItemStack(Material.DIRT), new ItemStack(Material.GLASS), 15));
    // Create a trade for 1 ANVIL for 1 GLASS with 10 max uses 
    trades.add(new VillagerTrade(new ItemStack(Material.ANVIL), new ItemStack(Material.GLASS), 10));
    // Create the object
    VillagerInventory cr = new VillagerInventory(trades, p);
    
    // Set the top title
    cr.setName(ChatColor.translateAlternateColorCodes('&', "&aHello"));
    
    // Open for player
    cr.open();
```



