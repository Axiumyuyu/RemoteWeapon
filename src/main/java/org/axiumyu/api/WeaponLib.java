package org.axiumyu.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;

import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.*;

import static java.lang.Math.log;
import static java.lang.Math.max;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

public class WeaponLib {
    public static final Set<Material> IGNORE_BLOCK = new HashSet<>();
    static {
        IGNORE_BLOCK.add(Material.WATER);
        IGNORE_BLOCK.add(Material.AIR);

    }

    //UUID转Byte数组
    public static byte[] uuidToBytes(UUID uuid) {
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(mostSignificantBits);
        buffer.putLong(leastSignificantBits);
        return buffer.array();
    }

    //获取升级花费
    public static int getUpdateCost(int currentLvl) {
        return (int) (log(currentLvl) * currentLvl + 2);
    }

    //浮点数转百分数
    public static String getPercentValue(float similarity) {
        NumberFormat fmt = NumberFormat.getPercentInstance();
        fmt.setMaximumFractionDigits(2);
        return fmt.format(similarity);
    }

    //更改物品Lore
    public static List<Component> editLore(Component name,int currentLvl, int currentExp, float criticalRate, float criticalDamage, int damage) {
        int cost = getUpdateCost(currentLvl);
        List<Component> lore = new ArrayList<>();
        lore.add(text("属性:      ").color(color(0xFFE9E6))
                .appendNewline()
                .appendNewline());
        lore.add(text("伤害: ").color(color(0xD05AD8))
                .append(text(damage).color(color(0xF769FF)).decorate(TextDecoration.BOLD))
                .appendNewline());
        lore.add(text("暴击率: ").color(color(0xD89B5B))
                .append(text(getPercentValue(criticalRate)).color(color(0xFFB866)).decorate(TextDecoration.BOLD))
                .appendNewline());
        lore.add(text("暴击伤害: ").color(color(0xD87D68))
                .append(text(getPercentValue(criticalDamage)).color(color(0xFF9875)).decorate(TextDecoration.BOLD))
                .appendNewline());
        lore.add(text("当前等级: ").color(color(0x3FCECF))
                .append(text(currentLvl).color(color(0x44FDFD)).decorate(TextDecoration.BOLD)
                        .appendNewline()));
        lore.add(text("升级需要: ").color(color(0x2ECC68))
                .append(text(max(cost - currentExp, 0)).append(text(" / ".concat(String.valueOf(cost)))).color(color(0x2EFF69)).decorate(TextDecoration.BOLD))
                .appendNewline());
        lore.add(text("绑定于: ").color(color(0xBF708E))
                .append(name
                .appendNewline()));
        return lore;
    }

    //获取视野终点位置
    public static Location endLoc(Location location,double pitch, double yaw, double distance) {
        // 将偏航角和俯仰角转换为弧度
        double pitchRad = Math.toRadians(pitch);
        double yawRad = Math.toRadians(yaw);

        // 计算终点的x、y和z坐标
        double endX = location.getX() + distance * Math.cos(pitchRad) * Math.sin(yawRad);
        double endY = location.getY() + distance * Math.sin(pitchRad) * Math.sin(yawRad);
        double endZ = location.getZ() + distance * Math.cos(yawRad);

        // 返回新的Location对象作为终点位置
        return new Location(location.getWorld(), endX, endY, endZ);
    }

}
