package teammt.villagerguiapi.api;

import org.bukkit.Bukkit;
import teammt.villagerguiapi.adapters.BaseAdapter;
import teammt.villagerguiapi.classes.VillagerInventory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AdapterLoader {

    private static Class<? extends BaseAdapter> adapterClass;

    public static void init(){
        String version = getVersion();
        Class<? extends BaseAdapter> clazz;
        try {
            Class<?> c = Class.forName("teammt.villagerguiapi.adapters.instances.MerchantAdapter_" + version);
            clazz = c.asSubclass(BaseAdapter.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find adapter for " + getVersion(), e);
        }
        adapterClass = clazz;
    }

    public static String getVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        v = v.substring(v.lastIndexOf('.') + 1);
        if (!v.equals("craftbukkit"))
            return v;
        else {
            String result = "UNK";
            InputStream stream = Bukkit.class.getClassLoader()
                    .getResourceAsStream("META-INF/maven/org.bukkit/bukkit/pom.properties");
            Properties properties = new Properties();
            if (stream != null) {
                try {
                    properties.load(stream);
                    result = properties.getProperty("version");
                    result = "v" + result.split("-")[0].replace('.', '_');
                } catch (IOException ex) {
                    return "UNK";
                }
            }
            return result;
        }
    }

    public static void open(VillagerInventory inv) {
        try {
            adapterClass.getConstructor(VillagerInventory.class).newInstance(inv).openFor(inv.getForWho());
        } catch (Exception e) {
            throw new RuntimeException("Could not open inventory", e);
        }
    }
}