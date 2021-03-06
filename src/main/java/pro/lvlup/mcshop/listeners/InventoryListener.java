package pro.lvlup.mcshop.listeners;

import net.minecraft.server.v1_16_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pro.lvlup.mcshop.Main;
import pro.lvlup.mcshop.basic.GuiItem;
import pro.lvlup.mcshop.basic.Service;
import pro.lvlup.mcshop.basic.ServiceManager;
import pro.lvlup.mcshop.managers.GuiManager;
import pro.lvlup.mcshop.nms.OpenSignEditor;
import pro.lvlup.mcshop.utils.Utils;

public class InventoryListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) {
			return;
		}
		if (e.getView().getTitle().startsWith(Utils.fixColor("&eZakup usługi"))) {
			if (e.getCurrentItem().getType().equals(Material.PAPER)) {
				e.setCancelled(true);
				return;
			}
			final Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			GuiItem gui = GuiManager.getGuiItem(e.getCurrentItem());
			Service service = gui.getItemService();
			if (service == null) {
				e.setCancelled(true);
				p.closeInventory();
				p.sendMessage("&cZgłoś się do administratora, niepoprawna konfiguracja (patrz konsole)");
				Bukkit.getLogger().warning("ID usługi w pliku uslugi.yml musi się zgadzać z nazwą kolumny");
			}
			ServiceManager.service.put(p.getPlayer(), service.getName());
			Location newSign = p.getLocation().add(0, 100, 0);
			Location fixnewSign = p.getLocation().add(0, 99, 0);
			fixnewSign.getBlock().setType(Material.BEDROCK);
			newSign.getBlock().setType(Material.OAK_WALL_SIGN);
			Block sign = newSign.getBlock();
			BlockState signState = sign.getState();
			if (signState instanceof Sign) {
				final Sign signBlock = (Sign) signState;
				final Location loc = signBlock.getLocation();
				final BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());

				signBlock.setLine(0, "");
				signBlock.setLine(1, Utils.fixColor("&4^ WPISZ KOD ^"));
				signBlock.setLine(2, Utils.fixColor("&7ABY ZAKUPIC"));
				signBlock.setLine(3, Utils.fixColor("&a&n" + service.getName().toUpperCase()));
				signBlock.setEditable(true);

				signBlock.update();
				new BukkitRunnable() {
					public void run() {
						OpenSignEditor.openSignEditor(p, pos);
					}
				}.runTaskLaterAsynchronously(Main.getInst(), 3);
			}
		}
	}
}
