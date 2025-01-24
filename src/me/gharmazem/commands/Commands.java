package me.gharmazem.commands;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtils;
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
        if(!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.colored(onlyplayers));
            return true;
        }

        Player player = (Player) sender;
        if(args.length > 1) {
            player.chat("/armazem help");
            return false;
        }

        // comando para abrir /armazem (/armazem)
        if(sender.hasPermission("gharmazem.usecommand")) {
            if (cmd.getName().equalsIgnoreCase("armazem") && args.length == 0) {
                ArmazemItens.pessoalArmazemInfoItem(player);
                ArmazemItens.sellAllItem();

                UtilClass.sendSound(player, Sound.CLICK);
                BaseManager.openStorage(player);
                return true;
            }
        }else { player.sendMessage(ColorUtils.colored(nopermission)); }

        String subCommand = args[0];

        // necessita da permissao gharmazem.usecommand (/armazem help)
        if(subCommand.equalsIgnoreCase("help")) {
            player.sendMessage(ColorUtils.colored("&4Comandos Armazém:"));
            player.sendMessage(ColorUtils.colored("  &f/armazem &f> &7Abrir armazém"));
            player.sendMessage(ColorUtils.colored("  &f/armazem help &f> &7Comandos para jogador"));
            player.sendMessage(ColorUtils.colored("  &f/armazem sell &f> &7Venda todos os itens de seu armazem"));
            player.sendMessage(ColorUtils.colored("  &f/armazem store &f> &7Armazene todos os itens de seu inventário"));
            if(sender.hasPermission("gharmazem.admin")) { // caso o jogador tiver a permissão gharmazem.admin, mostra os comandos de adm
                player.sendMessage(ColorUtils.colored("  &c/armazem reload &f> &7Recarrega a config.yml e a database dbase.yml"));
                return true;
            }
            return true;
        }

        // comando de reload (/armazem reload)
        if(sender.hasPermission("gharmazem.admin")) {
            if (subCommand.equalsIgnoreCase("reload")) {
                try {
                    //config.yml
                    Main.getInstance().reloadConfig();

                    //dbase.yml
                    Main.getInstance().reloadDatabaseFile();

                    sender.sendMessage(ColorUtils.colored("&aconfig.yml e dbase.yml recarregada com sucesso!"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{ player.sendMessage(ColorUtils.colored(nopermission)); }

        // vende todos os itens armazenados no armazem (/armazem sell)
        if(sender.hasPermission("gharmazem.sellall")) {
            if(subCommand.equalsIgnoreCase("sell")) {

                DecimalFormat df = new DecimalFormat("#,###,###,##0.##");
                int totalQuantia = BaseManager.getAllStored(player);
                if (totalQuantia > 0) {
                    double totalRendimento = BaseManager.getTotalValue(player);
                    Main.getEconomy().depositPlayer(player, totalRendimento);

                    BaseManager.sellAll(player);

                    UtilClass.sendSound(player, Sound.LEVEL_UP);
                    player.sendMessage(ColorUtils.colored(sellitens)
                            .replace("{rendimento}", df.format(totalRendimento))
                            .replace("{itens}", String.valueOf(totalQuantia)));

                    return true;
                }else { player.sendMessage(ColorUtils.colored(noitenstosell)); }
            }
        }else { player.sendMessage(ColorUtils.colored(nopermission)); }

        // guarda todos os itens do inventario do player (/armazem store)
        if(sender.hasPermission("gharmazem.store")) {
            if(subCommand.equalsIgnoreCase("store")) {
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

                            player.sendMessage(ColorUtils.colored(storeitens).replace("{itens}", totalAmount + " " + material.name()));
                        }
                    }
                    if (!hasStoredItems) {
                        player.sendMessage(ColorUtils.colored(noitenstostore));
                    }
                    player.closeInventory();
            }
        }else {
            player.sendMessage(ColorUtils.colored(nopermission));
        }
        return false;
    }

}
