package masecla.villager.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
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
		return v.substring(v.lastIndexOf('.') + 1);
	}

	public ZipEntry getAdapterEntry(ZipFile f) {
		String name = "masecla/villager/adapters/instances/MerchantAdapter_" + getVersion() + ".class";
		return f.getEntry(name);
	}

	public void reflectivelyLoad()
			throws ZipException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		File tmpFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "adapters.zip");
		plugin.saveResource("adapters.zip", true);
		ZipFile f = new ZipFile(tmpFile);
		ZipEntry correctClass = getAdapterEntry(f);

		File tmpDir = new File(File.separator + plugin.getDataFolder().getAbsolutePath() + File.separator + "masecla"
				+ File.separator + "villager" + File.separator + "adapters" + File.separator + "instances");
		tmpDir.mkdirs();

		File tmpClass = new File(File.separator + plugin.getDataFolder().getAbsolutePath() + File.separator + "masecla"
				+ File.separator + "villager" + File.separator + "adapters" + File.separator + "instances"
				+ File.separator + "MerchantAdapter_" + getVersion() + ".class");

		byte[] buffer = new byte[(int) correctClass.getSize()];
		f.getInputStream(correctClass).read(buffer);

		OutputStream result = new FileOutputStream(tmpClass);
		result.write(buffer);
		result.close();

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
