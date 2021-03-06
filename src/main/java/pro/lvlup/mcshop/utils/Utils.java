package pro.lvlup.mcshop.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import pro.lvlup.mcshop.Main;
import pro.lvlup.mcshop.nms.Reflections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

public class Utils {
	
	public static String fixColor(String string) {
		if (string == null) {
			return "";
		}
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void copy(InputStream source, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = source.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.close();
			source.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void info(final String... logs) {
        for (final String s : logs) {
            log(Level.INFO, s);
        }
    }

	public static void log(final Level level, final String l) {
        Bukkit.getLogger().log(level, l);
    }

	public static boolean isSpigot() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

	public static void openGui(Player p, Sign s) {  
	      Object tileEntity = Reflections.getDeclaredField(s,  "sign");
	      Reflections.setDeclaredField(tileEntity, "isEditable", true);
	      Reflections.setDeclaredField(tileEntity, Reflections.ver().startsWith("v1_7") ? "k" : "h", Reflections.getHandle(p));
	      if(Reflections.ver().startsWith("v1_7")){
	    	  Reflections.sendPacket(p,  Reflections.getPacket("OpenSignEditor", s.getX(), s.getY(), s.getZ()));
	      }else{
	    	  Reflections.sendPacket(p,  Reflections.getPacket("OpenSignEditor", Reflections.callDeclaredConstructor(Reflections.getNMSClass("BlockPosition"), s.getX(), s.getY(), s.getZ())));
	      }
	}
	public static String getEngineVersion(){
		return Reflections.ver().startsWith("v1.7") ? "1.7" : "1.8 lub wyzej";
	}
	public static void infos() {
		Runtime r = Runtime.getRuntime();
        long maxMemory = r.maxMemory();
        long used = (r.totalMemory() - r.freeMemory()) / 1048576;
        info("", "[MC-SHOP] Informacje:", "", "Wersja pluginu " + Main.getVersion(), "Silnik: ", 
        		((isSpigot()) ? "Spigot" : "Craftbukkit") + " Wersja: " + getEngineVersion(), "Java: " + System.getProperty("java.version"),
        		"Watki: " + Thread.currentThread(),
                "RAM: " + used + "/" + maxMemory, "", "Dzieki za uzywanie - luxDev", "Oraz nie zapominaj o: https://forum.lvlup.pro :)");
		
	}
}
