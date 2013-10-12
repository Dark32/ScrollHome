package ru.mcnw.dark32.scrollhome;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import org.bukkit.event.block.Action;

public class Scroll implements Listener {

	public static Location loc;
	private static Main plugin;

	public Scroll(Main pluging) {
		Scroll.plugin = pluging;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack item = event.getItem();
		Block block = event.getClickedBlock();
		if (item != null && item.getType() == Main.item_scrollMa && item.getDurability() == Main.item_scroll_sub) {
			if (action == Action.RIGHT_CLICK_BLOCK && block.getType() == Main.block_scrollMa && block.getData() == Main.block_scroll_sub) {
				Main.removeHome(player);
				Main.addHome(player);
				player.sendMessage(ChatColor.YELLOW + "Новый дом задан, " + player.getName());
				Main.saveHomes();
				return;
			}
			if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				//взять из базы		
				if (Main.isHome(player)) {
					player.teleport(Main.getHome(player).location);
					if (item.getAmount() <= 1) {
						player.setItemInHand(null);
						itemremove(player);
					} else {
						item.setAmount(item.getAmount() - 1);
					}

					player.sendMessage(ChatColor.YELLOW + "Добро пожаловать домой, " + player.getName());

				} else {
					player.sendMessage(ChatColor.YELLOW + "Ваш дом не найден, " + player.getName());
				}
				return;
			}
		} else {
			if (action == Action.RIGHT_CLICK_BLOCK && block.getType() == Main.block_scrollMa) {
				Main.removeHome(player);
				player.sendMessage(ChatColor.YELLOW + "Ваш дом очищен, " + player.getName());
			}

		}
	}

	public static void itemremove(final Player p) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				p.setItemInHand(null);
			}
		}, 1L);
	}
}
