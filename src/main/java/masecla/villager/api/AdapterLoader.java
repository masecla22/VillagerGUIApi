package masecla.villager.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;

import masecla.villager.adapters.BaseAdapter;

public class AdapterLoader {

	private MainLoader plugin;

	private URLClassLoader classLoader = null;

	public AdapterLoader(MainLoader plugin) {
		super();
		this.plugin = plugin;
	}

	public String getVersion() {
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

	public List<ZipEntry> getAdapterEntries(ZipFile f) {
		List<ZipEntry> result = new ArrayList<>();
		String name = "masecla/villager/adapters/instances/MerchantAdapter_" + getVersion() + ".class";
		if (f.getEntry(name) != null)
			result.add(f.getEntry(name));
		int i = 1;
		while (true) {
			ZipEntry tested = f.getEntry(name.replace(".class", "$" + i + ".class"));
			if (tested != null)
				result.add(tested);
			else
				break;
			i++;
		}
		return result;
	}

	public void reflectivelyLoad()
			throws ZipException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		File tmpFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "adapters.zip");
		plugin.saveResource("adapters.zip", true);
		ZipFile f = new ZipFile(tmpFile);
		List<ZipEntry> correctClasses = getAdapterEntries(f);
		for (ZipEntry correctClass : correctClasses) {
			File tmpDir = new File(
					File.separator + plugin.getDataFolder().getAbsolutePath() + File.separator + "masecla"
							+ File.separator + "villager" + File.separator + "adapters" + File.separator + "instances");
			tmpDir.mkdirs();

			System.out.println("Loading " + correctClass.getName());
			File tmpClass = new File(File.separator + plugin.getDataFolder().getAbsolutePath() + File.separator
					+ correctClass.getName());

			byte[] buffer = new byte[(int) correctClass.getSize()];
			f.getInputStream(correctClass).read(buffer);

			OutputStream result = new FileOutputStream(tmpClass);
			result.write(buffer);
			result.close();
		}

		this.classLoader = new URLClassLoader(new URL[] { plugin.getDataFolder().toURI().toURL() },
				this.getClass().getClassLoader());
		String name = "masecla.villager.adapters.instances.MerchantAdapter_" + getVersion();
		@SuppressWarnings("unchecked")
		Class<? extends BaseAdapter> clazz = (Class<? extends BaseAdapter>) Class.forName(name, true, classLoader);

		plugin.swapAdapter(clazz);
	}

	public void close() {
		try {
			classLoader.close();
			removeDirectory(plugin.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void removeDirectory(File f) {
		if (f.isDirectory())
			for (File cr : f.listFiles())
				removeDirectory(cr);
		f.delete();
	}
}
