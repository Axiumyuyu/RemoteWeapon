package org.axiumyu.api;

import org.axiumyu.RemoteWeapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public abstract class AbstractMenu implements InventoryHolder, Listener {
    protected Inventory inv;

    protected AbstractMenu(int size) {
        this.inv =getServer().createInventory(this,size);
        getServer().getPluginManager().registerEvents(this,getPlugin(RemoteWeapon.class));
    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public abstract void onItemClick(InventoryClickEvent ce);
}
