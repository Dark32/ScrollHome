package ru.mcnw.dark32.scrollhome;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Home {

    public String name;
    public Location location;

    public Home(String s, Location l) {
        name = s;
        location = l;
    }

    public Home(Player p) {
        name = p.getDisplayName();
        location = p.getLocation();
    }
}
