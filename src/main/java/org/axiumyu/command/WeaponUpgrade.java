package org.axiumyu.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ListIterator;

import static org.axiumyu.weapons.FireStaff.TAG;

public class WeaponUpgrade implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            Player pl =(Player) commandSender;
            PersistentDataContainer PDC = pl.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();

                switch (strings[0]){
                    case "all":
                        ListIterator<ItemStack> iterator = pl.getInventory().iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().getItemMeta().getPersistentDataContainer().has(TAG)) {

                            }
                        }
                    default:
                        if (PDC.has(TAG)){

                        }
                }
        } else if (commandSender instanceof ConsoleCommandSender) {

        }
    }
}
