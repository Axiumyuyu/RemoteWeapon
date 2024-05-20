package org.axiumyu.api.drawparticle;

import org.axiumyu.api.ParticleDrawable;
import org.bukkit.Location;
import org.bukkit.Particle;

public class SixPointedStar implements ParticleDrawable {
    private double centerX ;
    private double centerZ;
    private int radius;
    private Particle particle;
    private Location location;

    public SixPointedStar(Location loc, int radius, Particle particle) {
        this.particle=particle;
        this.centerX = loc.getX();
        this.centerZ = loc.getZ();
        this.radius = radius;
        this.location=loc;
    }

    public void draw() {

        // 计算六芒星的顶点
        int[] xPoints = calculateXPoints();
        int[] yPoints = calculateYPoints();

        // 连接顶点绘制线段
        for (int i = 0; i < 6; i++) {
            drawLine(particle, xPoints[i], yPoints[i], xPoints[(i + 1) % 6], yPoints[(i + 1) % 6]);
        }
    }

    // 自定义绘制线段的方法
    private void drawLine(Particle p, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            //g.drawRect(x1, y1, 1, 1);  用矩形替代点来绘制
            location.getWorld().spawnParticle(p,x1,location.getY(),y1,1);
            if (x1 == x2 && y1 == y2) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    private int[] calculateXPoints() {
        int[] xPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i + 30); // 每个顶点相隔60度，起始角度为30度
            xPoints[i] = (int) (centerX + radius * Math.cos(angle));
        }
        return xPoints;
    }

    private int[] calculateYPoints() {
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i + 30); // 每个顶点相隔60度，起始角度为30度
            yPoints[i] = (int) (centerZ + radius * Math.sin(angle));
        }
        return yPoints;
    }
}
