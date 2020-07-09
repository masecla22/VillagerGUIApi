cd target
jar xvf VillagerGUIApi-*
zip -r adapters.zip masecla/villager/adapters/instances/
jar uf VillagerGUIApi-* adapters.zip
mv VillagerGUIApi-0.0.1-SNAPSHOT.jar VillagerGUIApi-0.0.1-SNAPSHOT.zip
zip -d VillagerGUIApi-0.0.1-SNAPSHOT.zip masecla/villager/adapters/instances/\*
mv VillagerGUIApi-0.0.1-SNAPSHOT.zip VillagerGUIApi-0.0.1-SNAPSHOT.jar
rm -rvf masecla plugin.yml adapters.zip META-INF