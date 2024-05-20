package org.axiumyu.api;

import net.kyori.adventure.text.TextComponent;
import org.axiumyu.RemoteWeapon;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public abstract class AbstractWeapon implements Listener{

    public static NamespacedKey TAG;
    public static NamespacedKey OWNER;
    protected static TextComponent NAME;
    protected static Material MATERIAL;


    @EventHandler
    public abstract void onWeaponSwitch(PlayerItemHeldEvent ihe);


    protected abstract ItemStack genWeapon();

    @EventHandler
    public abstract void onWeaponFire(PlayerInteractEvent ie);

    public AbstractWeapon() {
        getServer().getPluginManager().registerEvents(this, getPlugin(RemoteWeapon.class));
    }
}
