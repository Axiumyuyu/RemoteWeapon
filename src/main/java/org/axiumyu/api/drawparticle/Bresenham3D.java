package org.axiumyu.api.drawparticle;
import org.axiumyu.api.ParticleDrawable;
import org.bukkit.Particle;

import org.bukkit.Location;

public class Bresenham3D implements ParticleDrawable {
    private Location location;
    private Particle p;
    private float pitch;
    private float yaw;
    private int length;

    public Bresenham3D(Location location, Particle p, float pitch, float yaw, int length) {
        this.location = location;
        this.p = p;
        this.pitch = pitch;
        this.yaw = yaw;
        this.length = length;
    }
    // 假设的用于在某位置画点的方法

    public void draw() {
        // 示例：从(0,0,0)到(5,5,5)绘制一条直线
        //drawLine3D(new Location(0, 0, 0), new Location(5, 5, 5));

        drawLine3D(location,endLoc(pitch,yaw,length));
    }

    private void drawLine3D(Location start, Location end) {
        double dx = Math.abs(end.getX() - start.getX());
        double dy = Math.abs(end.getY() - start.getY());
        double dz = Math.abs(end.getZ() - start.getZ());
        int xs = start.getX() < end.getX() ? 1 : -1;
        int ys = start.getY() < end.getY() ? 1 : -1;
        int zs = start.getZ() < end.getZ() ? 1 : -1;

        if (dx >= dy && dx >= dz) {
            double p1 = 2 * dy - dx;
            double p2 = 2 * dz - dx;
            while (start.getX() != end.getX()) {
                // 这里应该有代码来在三维空间中的location位置画一个点
                //System.out.println("Drawing particle at: " + location.x + ", " + location.y + ", " + location.z);
                start.getWorld().spawnParticle(p, start,1);
                start.setX(start.getX() + xs);
                //start.x += xs;
                if (p1 >= 0) {
                    start.setY(start.getY() + ys);
                    //start.y += ys;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    start.setZ(start.getZ() + zs);
                    //start.z += zs;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;
            }
        } else if (dy >= dx && dy >= dz) {
            double p1 = 2 * dx - dy;
            double p2 = 2 * dz - dy;
            while (start.getY() != end.getY()) {
                // 这里应该有代码来在三维空间中的location位置画一个点
                //System.out.println("Drawing particle at: " + location.x + ", " + location.y + ", " + location.z);
                start.getWorld().spawnParticle(p, start,1);
                start.setY(start.getY() + ys);
                //start.y += ys;
                if (p1 >= 0) {
                    start.setX(start.getX() + xs);
                    //start.x += xs;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    start.setZ(start.getZ() + zs);
                    //start.z += zs;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;
            }
        } else {
            double p1 = 2 * dy - dz;
            double p2 = 2 * dx - dz;
            while (start.getZ() != end.getZ()) {
                // 这里应该有代码来在三维空间中的location位置画一个点
                //System.out.println("Drawing particle at: " + location.x + ", " + location.y + ", " + location.z);
                start.getWorld().spawnParticle(p, start,1);
                start.setZ(start.getZ() + zs);
                //start.z += zs;
                if (p1 >= 0) {
                    start.setY(start.getY() + ys);
                    //start.y += ys;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    start.setX(start.getX() + xs);
                    //start.x += xs;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dy;
                p2 += 2 * dx;
            }
        }
        // 确保最后一个点被绘制
        // 这里应该有代码来在三维空间中的location位置画一个点
        //System.out.println("Drawing particle at: " + location.x + ", " + location.y + ", " + location.z);
        end.getWorld().spawnParticle(p, end,1);
    }

    // 根据偏航角和俯仰角计算终点位置
    public Location endLoc(double pitch, double yaw, double distance) {
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
