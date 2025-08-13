package me.gharmazem.commands;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.manager.LimitCheckManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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

        if (args.length == 0) {
            if (!sender.hasPermission("gharmazem.use")) {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }

            UtilClass.sendSound(player, Sound.CLICK);
            BaseManager.openStorage(player);
            return true;
        }

        String subCommand = args[0];

        switch (subCommand) {
            case "ajuda":
            case "help":
            case "?":
                List<String> ajuda = Arrays.asList(
                        "&e&l----- Comandos Armazém -----",
                        "  &f/armazem > &7Abrir armazém",
                        "  &f/armazem ajuda > &7Comandos para jogador",
                        "  &f/armazem vender > &7Venda os itens do Armazém",
                        "  &f/armazem guardar > &7Armazene os itens do inventário",
                        "&e&l------------------------",
                        ""
                );

                for (String linha : ajuda) {
                    player.sendMessage(ColorUtil.colored(linha));
                }
                break;

            case "vender":
            case "sell":
                if (!sender.hasPermission("gharmazem.sellall")) {
                    player.sendMessage(ColorUtil.colored(nopermission));
                    return true;
                }
                BaseManager.sellAll(player);
                break;

            case "guardar":
            case "store":
                if (!sender.hasPermission("gharmazem.store")) {
                    player.sendMessage(ColorUtil.colored(nopermission));
                    return true;
                }
                BaseManager baseManager = new BaseManager();

                baseManager.store(player);
                break;

            case "cheque":
            case "check":
                if (!sender.hasPermission("gharmazem.admin")) {
                    player.sendMessage(ColorUtil.colored(nopermission));
                    return true;
                }

                val correctUsage = ColorUtil.colored("&cUse: /armazem cheque <player> <limite> <quantia>");

                if(args.length < 4) {
                    player.sendMessage(correctUsage);
                    return true;
                }

                val alvo = Bukkit.getPlayer(args[1]);
                val limit = args[2];
                val quantity = args[3];

                int valor;
                int amount;
                try {
                    if(alvo == null) {
                        player.sendMessage(correctUsage);
                        return true;
                    }
                    valor = Integer.parseInt(limit);
                    amount = Integer.parseInt(quantity);
                } catch (NumberFormatException e) {
                    player.sendMessage(correctUsage);
                    return true;
                }

                val item = LimitCheckManager.createLimitCheck(valor);
                item.setAmount(amount);

                alvo.getInventory().addItem(item);
                break;

            default:
                player.sendMessage(ColorUtil.colored("&cComando desconhecido! Use /armazem ajuda"));
                break;
        }
        return false;
    }

}