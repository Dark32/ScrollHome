package ru.mcnw.dark32.scrollhome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin {
	static boolean usePEX = false;
	static boolean usePB = false;
	public PluginManager pm;
	public static int item_scroll = 693;
	public static int block_scroll = 214;
	public static ArrayList<Home> homes = new ArrayList<Home>();
	public Logger log;
	@Override
	public void onEnable() {
		log = getServer().getLogger();
		pm = Bukkit.getPluginManager();
		if (pm.getPlugin("PermissionsEx") != null) {
			usePEX = true;
		} else if (pm.getPlugin("PermissionsBukkit") != null) {
			usePB = true;
		} else {
			getLogger().warning("Permissions plugins not found!");
		}
		item_scroll = this.getConfig().getInt("item_scroll");
		block_scroll = this.getConfig().getInt("block_scroll");
		getCommand("scroll").setExecutor(new ScrollCommandExecutor(this));
		getServer().getPluginManager().registerEvents(new Scroll(this), this);
		loadHomes();
		getLogger().info("load");
	}
	
	@Override
	public void onDisable() {
		saveHomes();
		getLogger().info("dsable");
	}
	public static boolean hasPermission(Player player, String permission) {
		if (usePEX) {
			return PermissionsEx.getUser(player).has(permission);
		} else if (usePB) {
			return player.hasPermission(permission);
		} else {
			return player.isOp();
		}
	}
	//---------------------------------------------------
	// технические функции 1
	//---------------------------------------------------
	public static void addHome(String s, Location l){
		homes.add(new Home(s, l));
	}	
	public static void addHome(Player p){
		homes.add(new Home(p.getDisplayName(), p.getLocation()));
	}	

	public static boolean isHome(String s){ //есть ли дом
		for(Home h: homes){
			if(h.name.equalsIgnoreCase(s))
				return true;
		}

		return false;
	}
	public static boolean isHome(Player p){ //есть ли дом
		return isHome(p.getDisplayName());
	}
	public static Home getHome(String s){//вернуть дом
		if(isHome(s)){
			for(Home h: homes){
				if(h.name.equalsIgnoreCase(s))
					return h;
			}
		}
		return null;
	}
	public static Home getHome(Player p){//вернуть дом
		return getHome(p.getDisplayName());
	}
	public static void removeHome(String s){ //удаление дома
		if(isHome(s)){
			homes.remove(getHome(s));
			removeHome(s);
		}
	}
	public static void removeHome(Player p){ //удаление дома
		removeHome(p.getDisplayName());
	}
	public static void removeAllHome(){ //удаление
		for(Home h: homes){
			homes.remove(h);
		}
	}
	//---------------------------------------------------
	// технические функции 2
	//---------------------------------------------------
	public void loadHomes(){// загружаемдома
		String fName = "homes.data";
		File file = new File("plugins/ScrollHome/" + fName);
		if(file.exists()){
			try{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));
				Object result = ois.readObject();
				ois.close();
				if(result != null){
					ArrayList<String> parse = (ArrayList<String>) result;
					for(String i: parse){
						try{
							String[] args = i.split(",");
							Location warpL = new Location(getServer().getWorld(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Float.parseFloat(args[5]), Float.parseFloat(args[6]));
							Home warp = new Home(args[0], warpL);
							homes.add(warp);
							ois.close();
						}catch(Exception e){
							System.out.println("Ошибка загрузки базы домов.");
							System.out.println(e);
						}
					}
				}
			}catch(Exception e){
				System.out.println("Ошибка загрузки базы домов.");
				System.out.println(e);
			}
		}
	}
	public static void saveHomes(){//сохраняем
		String fName = "homes.data";
		ArrayList<String> format = new ArrayList<String>();
		for(Home h: homes){
			String toAdd = h.name;

			toAdd += "," + h.location.getWorld().getName();
			toAdd += "," + h.location.getBlockX();
			toAdd += "," + h.location.getBlockY();
			toAdd += "," + h.location.getBlockZ();
			toAdd += "," + h.location.getYaw();
			toAdd += "," + h.location.getPitch();
			format.add(toAdd);
		}

		File file = new File("plugins/ScrollHome/" + fName);

		new File("plugins/").mkdir();
		new File("plugins/ScrollHome/").mkdir();
	    if(!file.exists()){
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Не удалось сохранить базу домов");
				System.out.println(e);
			}
	    }

		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath()));
			oos.writeObject(format);
			oos.flush();
			oos.close();
		}catch(Exception e){
			System.out.println("Не удалось сохранить базу домов");
			System.out.println(e);
		}
	}
}
