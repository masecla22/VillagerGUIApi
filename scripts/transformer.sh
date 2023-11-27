command -v jar >/dev/null 2>&1 || { echo >&2 "I require jar but it's not installed.  Aborting."; exit 1; }
command -v zip >/dev/null 2>&1 || { echo >&2 "I require zip but it's not installed.  Aborting."; exit 1; }

version=1.4.0

# Enter the path containing the output of the maven build
cd ../plugin/target

# Unzip the jar
jar xvf VillagerGUIApi-*

# Zip up all the adapters
zip -r adapters.zip teammt/villagerguiapi/adapters/instances/

# Place the adapters back in the jar
jar uf VillagerGUIApi-* adapters.zip

# Rename the jar into a zip
mv VillagerGUIApi-$version.jar VillagerGUIApi-$version.zip

# Remove the old files
zip -d VillagerGUIApi-$version.zip teammt/villagerguiapi/adapters/instances/\*

# Rename back to jar
mv VillagerGUIApi-$version.zip VillagerGUIApi-$version.jar

# Remove the old files
rm -rvf teammt plugin.yml adapters.zip META-INF

# Move the jar to the root of the project
cd ../..
mkdir -p target
cp plugin/target/VillagerGUIApi-$version.jar target/