package me.gharmazem.commands;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    String nopermission = Main.getInstance().getConfig().getString("Messages.no-permission");
    String onlyplayers = Main.getInstance().getConfig().getString("Messages.only-players");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.colored(onlyplayers));
            return true;
        }

        Player player = (Player) sender;

        // (/armazem)
        if (args.length == 0 && cmd.getName().equalsIgnoreCase("armazem")) {
            if (!sender.hasPermission("gharmazem.usecommand")) {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }

            UtilClass.sendSound(player, Sound.CLICK);
            BaseManager.openStorage(player);
            return true;
        }

        String subCommand = args[0];

        // (/armazem help)
        if (subCommand.equalsIgnoreCase("help")) {
            player.sendMessage(ColorUtil.colored("&4Comandos Armazém:"));
            player.sendMessage(ColorUtil.colored("  &f/armazem &f> &7Abrir armazém"));
            player.sendMessage(ColorUtil.colored("  &f/armazem help &f> &7Comandos para jogador"));
            player.sendMessage(ColorUtil.colored("  &f/armazem sell &f> &7Venda todos os itens de seu armazem"));
            player.sendMessage(ColorUtil.colored("  &f/armazem store &f> &7Armazene todos os itens de seu inventário"));
            return true;
        }

        // (/armazem sell)
        if (subCommand.equalsIgnoreCase("sell")) {
            if (!sender.hasPermission("gharmazem.sellall")) {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }
            BaseManager.sellAll(player);
        }

        // (/armazem store)
        if (subCommand.equalsIgnoreCase("store")) {
            if (!sender.hasPermission("gharmazem.store")) {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }
            BaseManager.store(player);
        }
        return false;
    }

}