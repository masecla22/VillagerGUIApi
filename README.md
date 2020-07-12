image::https://api.bintray.com/packages/masecla22/VillagerGUIApi/VillagerGUIApi/images/download.svg[link="https://bintray.com/masecla22/VillagerGUIApi/VillagerGUIApi/_latestVersion"]

# VillagerGUI Api
Have you ever tried to create a custom Villager GUI without a villager in Spigot? You probably noticed the absolute insanity that it involves, and the complete and utter inability to port it to other versions. This library intends to fix this by having custom classes for easy access and events to create your personalized GUIs!

# Versions supported (as of now)
  - 1.3.1, 1.3.2
  - 1.4.5, 1.4.6, 1.4.7
  - 1.5, 1.5.1, 1.5.2
  - 1.6.1, 1.6.2, 1.6.4
  - 1.7.2, 1.7.4, 1.7.5, 1.7.6, 1.7.7, 1.7.8, 1.7.9, 1.7.10
  - 1.8, 1.8.1, 1.8.2, 1.8.3, 1.8.4, 1.8.5, 1.8.6, 1.8.7, 1.8.8, 1.8.9
  - 1.9, 1.9.1, 1.9.2, 1.9.3, 1.9.4
  - 1.10, 1.10.1, 1.10.2
  - 1.11, 1.11.1, 1.11.2
  - 1.12, 1.12.1, 1.12.2
  - 1.13, 1.13.1, 1.13.2
  - 1.14, 1.14.1, 1.14.2, 1.14.3, 1.14.4
  - 1.15, 1.15.1, 1.15.2
  - 1.16, 1.16.1, (1.16.2)

# Versions planned:
  - 1.4.2, 1.4.4, 
  - Add some sort of emulation for 1.2, 1.1 and 1.0 (I need to find spigot versions for this)

# Goal of the project
The goal of this library is to provide a unified API for developers to create Villagers GUI Containers and have the events to manipulate them and create different plugin with it. 

# How does it work?
The way this project works is upon building it a certain package of classes is moved inside a zip inside a jar, which causes them to not load on startup of the server. Then the server loads an instance of the correct class depending on what version it is. The class is only loaded once on startup and is the only call to reflection ever made. Because of this, we have two advantages, our code remains readable, which helps up development (as no reflection is needed), and performance is really high. A disadvantage of this method is that we need to write a separate class for every single version so that the plugin knows what to load on startup.

# Building
Clone the repository, and populate the lib/ folder with the correct spigots. For those versions of spigot that don't exist simply rename a craftbukkit which you can find. All of them can be found on getbukkit.org. I have not included them in the repository since they are 600MB. If you are too lazy to download them all, add me on discord masecla22#4309.
Once there, execute the command "mvn clean verify install" to build it and add it to the local maven repository. In order to create the final JAR ready to be used in servers you also need to run "./transform.sh"


# Final note
Some of the versions have inconsistent behaviour, which is all listed in the Notes section of the readme, in all the places where inconsistencies were found, I took the most reasonable approach, which is why some events might behave differently or not exist at all in certain versions. The goal of my choices when it comes to this is to maximise portability and to keep it straight forward. 

# Notes
  - VillagerInventoryModifyEvent only gets called for items inside the GUI
  - Putting an item inside any of the slots will cause 3 VillagerInventoryModifyEvent with the ItemStack being the result slot (the number is not related to the amount of trades inside the GUI, it just seems to be 3?) (ONLY on versions 1.6 - 1.13)
  - WARNING, the result slot is not able to be used to identify what recipe the player is currently used.
  - In versions >1.11 it is no longer possible to detect when a player changes trades
  - In versions >1.11 clicking on an empty slot no longer sends VillagerInventoryModifyEvent
  - In version 1.13 completing a trade will not call VillagerInventoryModifyEvent unlike the others
  - In version >1.14 <=1.5.2 the max uses for the trade given by the VillagerCompleteTradeEvent will always be -1
  - In versions >1.14 clicking on a side trade to move the items will NOT trigger VillagerInventoryModifyEvent
  - In versions <1.8 max uses does not work. This is a KNOWN BUG!
  - In versions <=1.4 names will NOT work
  - In versions <=1.5.2 changing a trade will not trigger VillagerInventoryModifyEvent
  - In versions <=1.5.2 completing a trade sends TWO VillagerCompleteTradeEvent (which needs to be changed)


# Installation
### For server owners
Download the .JAR and place it inside the plugins folder
### For developers
Download the .JAR and put it on your path, or add it through maven
```xml
<repository>
      <id>masecla-repo</id>
      <url>https://masecla.dev/nexus/repository/maven-snapshots/</url>
</repository>
```
```xml
<dependency>
      <groupId>masecla</groupId>
      <artifactId>VillagerGUIApi</artifactId>
      <version>0.0.1-20200712.201012-2</version>
</dependency>
```

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



