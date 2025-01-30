package me.gharmazem.commands;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.List;

public class Commands implements CommandExecutor {

    String nopermission = Main.getInstance().getConfig().getString("Messages.no-permission");
    String onlyplayers = Main.getInstance().getConfig().getString("Messages.only-players");
    String sellitens = Main.getInstance().getConfig().getString("Messages.sell-itens");
    String noitenstosell = Main.getInstance().getConfig().getString("Messages.no-itens-to-sell");
    String noitenstostore = Main.getInstance().getConfig().getString("Messages.no-itens-to-store");
    String storeitens = Main.getInstance().getConfig().getString("Messages.store-itens");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.colored(onlyplayers));
            return true;
        }

        Player player = (Player) sender;

        // (/armazem)
        if (cmd.getName().equalsIgnoreCase("armazem")) {
            if (sender.hasPermission("gharmazem.usecommand")) {
                ArmazemItens.pessoalArmazemInfoItem(player);
                ArmazemItens.sellAllItem();

                UtilClass.sendSound(player, Sound.CLICK);
                BaseManager.openStorage(player);
            } else {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }
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
            if (sender.hasPermission("gharmazem.admin")) { // caso o jogador tiver a permissão gharmazem.admin, mostra os comandos de adm
                player.sendMessage(ColorUtil.colored("  &c/armazem reload &f> &7Recarrega a config.yml e a database dbase.yml"));
                return true;
            }
            return true;
        }

        // (/armazem reload)
        if (subCommand.equalsIgnoreCase("reload")) {
            if (sender.hasPermission("gharmazem.admin")) {
                try {
                    Main.getInstance().reloadConfig();
                    sender.sendMessage(ColorUtil.colored("&aConfig recarregada com sucesso!"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }
        }

        // (/armazem sell)
        if (subCommand.equalsIgnoreCase("sell")) {
            if (sender.hasPermission("gharmazem.sellall")) {

                DecimalFormat df = new DecimalFormat("#,###,###,##0.##");
                int totalQuantia = BaseManager.getAllStored(player);
                if (totalQuantia > 0) {
                    double totalRendimento = BaseManager.getTotalValue(player);
                    Main.getEconomy().depositPlayer(player, totalRendimento);

                    BaseManager.sellAll(player);

                    UtilClass.sendSound(player, Sound.LEVEL_UP);
                    player.sendMessage(ColorUtil.colored(sellitens)
                            .replace("{rendimento}", df.format(totalRendimento))
                            .replace("{itens}", String.valueOf(totalQuantia)));

                    return true;
                } else {
                    player.sendMessage(ColorUtil.colored(noitenstosell));
                    return true;
                }
            } else {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }
        }

        // (/armazem store)
        if (subCommand.equalsIgnoreCase("store")) {
            if (sender.hasPermission("gharmazem.store")) {
                List<Material> allowed = Main.getInstance().getAllowedItems();

                boolean hasStoredItems = false;
                for (Material material : allowed) {
                    int totalAmount = 0;

                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType() == material) {
                            totalAmount += item.getAmount();
                            player.getInventory().remove(item);
                        }
                    }
                    if (totalAmount > 0) {
                        hasStoredItems = true;
                        ItemStack storedItem = new ItemStack(material, totalAmount);
                        BaseManager.saveItem(player, storedItem);

                        player.sendMessage(ColorUtil.colored(storeitens).replace("{itens}", totalAmount + " " + material.name()));
                    }
                }
                if (!hasStoredItems) {
                    player.sendMessage(ColorUtil.colored(noitenstostore));
                    return true;
                }
                player.closeInventory();
            } else {
                player.sendMessage(ColorUtil.colored(nopermission));
                return true;
            }
        }
        return false;
    }

}