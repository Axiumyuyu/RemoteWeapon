package org.axiumyu.weapons;

import net.kyori.adventure.text.TextComponent;
import org.axiumyu.RemoteWeapon;
import org.axiumyu.api.AbstractWeapon;
import org.axiumyu.api.Upgradeable;
import org.axiumyu.api.drawparticle.CommonDraw;
import org.axiumyu.api.drawparticle.SixPointedStar;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.title.Title.title;
import static org.axiumyu.api.WeaponLib.*;
import static org.axiumyu.api.preprocess.PreProcessResult.*;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class FireStaff extends AbstractWeapon implements Upgradeable {

    public static final NamespacedKey TAG = new NamespacedKey("remoteweapon", "tag");
    public static final NamespacedKey OWNER = new NamespacedKey("remoteweapon", "owner");
    private static final NamespacedKey LEVEL = new NamespacedKey("remoteweapon", "level");
    private static final NamespacedKey CRITICALRATE = new NamespacedKey("remoteweapon", "criticalrate");
    private static final NamespacedKey EXP = new NamespacedKey("remoteweapon", "exp");
    private static final NamespacedKey CRITICALDAMAGE = new NamespacedKey("remotweapon", "criticaldamage");
    private static final NamespacedKey DAMAGE =new NamespacedKey("remoteweapon","damage");
    private static final TextComponent NAME = text("炎震之杖").color(color(0xCF3434));
    private static final Material MATERIAL = Material.GOLDEN_SWORD;

    private final ItemStack fireStaff;
    private final Particle p = Particle.LAVA;
    private final Particle pp = Particle.FALLING_LAVA;
    private final Player player;
    private final String key = "Fire Staff";
    private int exp = 0;
    private byte level = 0;
    private int damage = 2;
    private float criticalRate;
    private float criticalDamage;
    private byte coolDown;

    public FireStaff(Player player) {
        super();
        this.player = player;
        this.fireStaff = genWeapon();
    }

    @Override
    public void onWeaponSwitch(PlayerItemHeldEvent ihe) {
        try {
            Player pl = ihe.getPlayer();
            ItemStack item = pl.getInventory().getItem(ihe.getNewSlot());
            if (item!=null && preProcess(item, pl)==VALID){
                coolDown=60;
                BukkitRunnable timer =new BukkitRunnable() {
                    @Override
                    public void run() {
                        toastCoolDown(pl);
                        coolDown-=10;
                        if (coolDown==0){
                            this.cancel();
                        }
                    }
                };
                timer.runTaskTimer(getPlugin(RemoteWeapon.class),0,10);
            } else if (preProcess(item, pl)!=VALID) {
                ihe.setCancelled(true);
                showIllegalMessage(pl);
            }
        } catch (Exception ignored) {

        }
    }

    private void toastCoolDown(Player pl) {
        pl.sendActionBar(text("剩余冷却时间：").append(text(coolDown/20+"s").color(color(0xCCCCCC))));
    }

    @Override
    public void levelUp() {
        Random r = new Random(System.currentTimeMillis());
        fireStaff.editMeta(im -> {
            PersistentDataContainer ic = im.getPersistentDataContainer();
            while (exp >= getUpdateCost(level)) {
                if (criticalRate!=1f) {
                    float chance1 = r.nextFloat();
                    if (chance1 >= 0.9) {
                        criticalRate = min(criticalRate + 0.15f*r.nextFloat(), 1f);
                    } else if (chance1 >= 0.8) {
                        criticalRate = min(criticalRate + 0.1f*r.nextFloat(), 1f);
                    } else if (chance1 >= 0.7) {
                        criticalRate = min(criticalRate + 0.05f*r.nextFloat(), 1f);
                    }
                    ic.set(CRITICALRATE, PersistentDataType.FLOAT, criticalRate);
                }

                float chance2 = r.nextFloat();
                if (chance2 >= 0.8) {
                    criticalDamage += 0.35*r.nextFloat();
                } else if (chance2 >= 0.5) {
                    criticalDamage += 0.3*r.nextFloat();
                } else if (chance2 >= 0.2) {
                    criticalDamage += 0.25*r.nextFloat();
                }

                float chance3 =r.nextFloat();
                if (chance3>=0.8){
                    damage+=r.nextInt(5);
                } else if (chance3 >= 0.6) {
                    damage+=r.nextInt(4);
                } else if (chance3 >= 0.3) {
                    damage+=r.nextInt(3);
                }else {
                    damage+=1;
                }
                exp -= getUpdateCost(level);
                level++;
            }
            ic.set(LEVEL, PersistentDataType.BYTE, level);
            ic.set(EXP, PersistentDataType.INTEGER, exp);
            ic.set(CRITICALDAMAGE, PersistentDataType.FLOAT, criticalDamage);
            ic.set(DAMAGE,PersistentDataType.INTEGER,damage);
            im.lore(editLore(player.displayName(),level,exp,criticalRate,criticalDamage,damage));
        });
    }

    @Override
    public void onWeaponFire(PlayerInteractEvent ie) {
        try {
            Player pl = ie.getPlayer();
            ItemStack item =ie.getItem();
            if (ie.getAction().isRightClick()) {
                if (item!=null && preProcess(item, pl)==VALID){
                    if (coolDown!=0){
                        pl.sendMessage(text("物品还在冷却中！").color(color(0x7B2919)));
                        ie.setCancelled(true);
                    }else {
                        List<Block> blocks=pl.getLastTwoTargetBlocks(IGNORE_BLOCK,200);
                        Block targetBlock=pl.getTargetBlockExact(200, FluidCollisionMode.ALWAYS);
                    }
                    BukkitRunnable toast =new BukkitRunnable() {
                        @Override
                        public void run() {
                            toastCoolDown(pl);
                            if (coolDown==0){
                                this.cancel();
                            }

                        }
                    };
                    toast.runTaskTimer(getPlugin(RemoteWeapon.class),0,10);
                } else if (preProcess(item, pl)!=VALID) {
                    ie.setCancelled(true);
                    showIllegalMessage(pl);
                }
            }
        } catch (Exception ignored) {

        }
    }

    private void showIllegalMessage(Player pl) {
        pl.showTitle(title(text("这不是你的物品！").color(color(0xFF736B)),
                text("请把它还给").append(player.displayName()).color(color(0xFF736B))));
    }

    @Override
    protected ItemStack genWeapon() {
        ItemStack i = new ItemStack(MATERIAL);
        UUID playerUUID = player.getUniqueId();
        i.editMeta(im -> {
            PersistentDataContainer ic = im.getPersistentDataContainer();

            ic.set(OWNER, PersistentDataType.BYTE_ARRAY, uuidToBytes(playerUUID));
            ic.set(TAG, PersistentDataType.STRING, key);
            ic.set(LEVEL, PersistentDataType.BYTE, (byte) 0);
            ic.set(EXP, PersistentDataType.INTEGER, 0);

            float c = 0.1f*(new Random(System.currentTimeMillis()).nextFloat());
            float d = 1 +0.4f* (new Random(System.currentTimeMillis()).nextFloat());
            ic.set(CRITICALRATE, PersistentDataType.FLOAT, c);
            ic.set(CRITICALDAMAGE, PersistentDataType.FLOAT, d);
            criticalRate = c;
            criticalDamage = d;

            im.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
            im.setUnbreakable(true);
            im.lore(editLore(player.displayName(),0, 0, c, d,2));
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "基础攻击", 2, AttributeModifier.Operation.ADD_NUMBER));
            im.displayName(NAME);
        });
        return i;
    }

    private int preProcess(ItemStack item, Player pl) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (pl.equals(player) && pdc.has(TAG)) {
            if (pl.getUniqueId().compareTo(UUID.nameUUIDFromBytes(pdc.get(OWNER, PersistentDataType.BYTE_ARRAY)))==0 && pdc.get(TAG, PersistentDataType.STRING) == key){
                CommonDraw.spawnCircle(player.getLocation(),3,p);
                SixPointedStar sp =new SixPointedStar(pl.getLocation(),3,pp);
                sp.draw();
                return VALID;
            } else if (pdc.get(TAG, PersistentDataType.STRING) != key) {
                return WRONG_TAG;
            } else if (!pdc.has(TAG)) {
                return NO_TAG;
            } else{
                return WRONG_OWNER;
            }

        }else return UNKNOWN_STATE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FireStaff fireStaff1 = (FireStaff) o;
        return Objects.equals(fireStaff, fireStaff1.fireStaff) && Objects.equals(player, fireStaff1.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fireStaff, player);
    }
}
