package ru.mcnw.dark32.scrollhome;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScrollCommandExecutor implements CommandExecutor {

    private Main plugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public ScrollCommandExecutor(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (cmd.getName().equalsIgnoreCase("scroll")) {
            if (args.length < 1) { // help
                help(sender);
                return true;
            }
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                if (args.length == 1) {
                    if (args[0].equals("help")) {
                        help(sender);
                        return true;
                    }
                    if (args[0].equals("list") && Main.hasPermission(player, "scroll.list")) {
                        String list = "";
                        for (Home h : Main.homes) {
                            list += ", *" + h.name;
                        }
                        player.sendMessage(ChatColor.DARK_AQUA + "** Список всех домов **");
                        player.sendMessage(ChatColor.GREEN + list.substring(1));
                        return true;
                    }
                    if (args[0].equals("to") && Main.hasPermission(player, "scroll.to")) {
                        player.teleport(Main.getHome(player).location);
                        player.sendMessage(ChatColor.YELLOW + "Добро пожаловать домой, " + player.getName());
                        return true;
                    }
                    if (args[0].equals("save") && Main.hasPermission(player, "scroll.save")) {
                        Main.saveHomes();
                        player.sendMessage(ChatColor.YELLOW + "Настройки сохранены, " + player.getName());
                        return true;
                    }
                    if (args[0].equals("set") && Main.hasPermission(player, "scroll.set")) {
                        if (Main.isHome(player)) {
                            Main.removeHome(player);
                            Main.addHome(player);
                        } else {
                            Main.addHome(player);
                        }
                        player.sendMessage(ChatColor.YELLOW + "Новый дом задан, " + player.getName());
                        return true;
                    }
                    if ((args[0].equals("remove") || args[0].equals("delete") || args[0].equals("del"))
                            && Main.hasPermission(player, "scroll.remove")) {
                        if (Main.isHome(player)) {
                            Main.removeHome(player);
                            player.sendMessage(ChatColor.YELLOW + "Дом удалён, " + player.getName());
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "Невозможно удалить не заданный дом, " + player.getName());
                        }
                        return true;
                    }
                }
                if (args.length == 2) {
                    if ((args[0].equals("remove") || args[0].equals("delete") || args[0].equals("del"))
                            && Main.hasPermission(player, "scroll.remove")) {
                        if (Main.isHome(args[1])) {
                            Main.removeHome(args[1]);
                            player.sendMessage(ChatColor.YELLOW + "Дом " + args[1] + " удалён, " + player.getName());
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "Невозможно удалить не заданный дом, " + player.getName());
                        }
                        return true;
                    }
                    if (args[0].equals("to") && Main.hasPermission(player, "scroll.to")) {
                        if (Main.isHome(args[1])) {
                            player.teleport(Main.getHome(args[1]).location);
                            player.sendMessage(ChatColor.YELLOW + "Добро пожаловать в дом игрока " + args[1] + ", " + player.getName());
                            return true;
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "Дом не найден, " + player.getName());
                            return true;
                        }
                    }
                    if (args[0].equals("set") && Main.hasPermission(player, "scroll.set")) {
                        if (Main.isHome(args[1])) {
                            Main.removeHome(player);
                            Main.addHome(args[1], player.getLocation());
                            player.sendMessage(ChatColor.YELLOW + "Дом для " + args[1] + " задан, " + player.getName());
                            return true;
                        } else {
                            Main.addHome(args[1], player.getLocation());
                            player.sendMessage(ChatColor.YELLOW + "Дом для " + args[1] + " задан, " + player.getName());
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

    void help(CommandSender sender) {
        Player player = null;
        if ((sender instanceof Player)) {
            player = (Player) sender;
        }
        if (player != null) {
            sender.sendMessage("Добро пожаловать пользователь.");
        }
        sender.sendMessage("ScrollHome ver."+Main.version);
        sender.sendMessage("Автор: dark32");
        sender.sendMessage("Плагин разработан специально для mcnw.ru");
        helpmsg(player, sender, "scroll.help", ChatColor.GOLD + "/scroll [help]" + ChatColor.WHITE + "- для вызова справки");
        helpmsg(player, sender, "scroll.list", ChatColor.GOLD + "/scroll list" + ChatColor.WHITE + "- для просмотра всех домов");
        helpmsg(player, sender, "scroll.to", ChatColor.GOLD + "/scroll to [имя]" + ChatColor.WHITE + " - для телепортации в дом игрока");
        helpmsg(player, sender, "scroll.save", ChatColor.GOLD + "/scroll save" + ChatColor.WHITE + "- для сохранения конфига");
        helpmsg(player, sender, "scroll.set", ChatColor.GOLD + "/scroll set [имя]" + ChatColor.WHITE + " - для установки точки");
        helpmsg(player, sender, "scroll.remove", ChatColor.GOLD + "/scroll remove|del[ete] [имя]" + ChatColor.WHITE + " - для удаления точки");

    }

    void helpmsg(Player player, CommandSender sender, String perm, String msg) {
        if (player == null) {
            sender.sendMessage(msg);
        } else {
            if (Main.hasPermission(player, perm)) {
                sender.sendMessage(msg);
            }
        }
    }
}
