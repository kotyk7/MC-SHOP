package pro.lvlup.mcshop;

import io.papermc.lib.PaperLib;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pro.lvlup.mcshop.basic.*;
import pro.lvlup.mcshop.commands.*;
import pro.lvlup.mcshop.listeners.*;
import pro.lvlup.mcshop.managers.*;
import pro.lvlup.mcshop.utils.*;

import java.io.File;

public class Main extends JavaPlugin {

	private static Main instance;
	private Config config;

	public void onEnable() {
		PaperLib.suggestPaper(this);

		instance = this;
		config = new Config(this);

		FilesManager.checkFiles();
		loadAllServices();
		registerListeners();
		Utils.infos();

		getCommand("sklep").setExecutor(new ShopCMD());
	}

	public void onDisable() {
		Utils.infos();
	}
	public static Main getInst() {
		return instance;
	}
	public static String getVersion() {
		return getInst().getDescription().getVersion();
	}

	private void loadAllServices() {
		File f = new File(FilesManager.getPluginDirectory(), "uslugi.yml");
		YamlConfiguration y = YamlConfiguration.loadConfiguration(f);
		for (String s : y.getConfigurationSection("uslugi").getKeys(false)) {
			Service service = new Service(s, y.getString("uslugi." + s + ".nazwaWys"),
					y.getString("uslugi." + s + ".tresc"), y.getInt("uslugi." + s + ".sms"),
					y.getString("uslugi." + s + ".cena"), y.getString("uslugi." + s + ".waznosc"),
					Material.getMaterial(y.getString("uslugi." + s + ".material")),
					y.getStringList("uslugi." + s + ".komendy"),
					y.getString("uslugi." + s + ".idUslugi"));
			service.setBoughtAmount(0);
			service.setLastBuyer(null);
			ServiceManager.addService(service);
			GuiManager.setupGui(s);
		}
	}

	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new SignChangeListener(), this);
		pm.registerEvents(new PlayerQuitList(), this);
		pm.registerEvents(new PlayerKickList(), this);
	}
}