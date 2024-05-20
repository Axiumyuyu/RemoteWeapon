package org.axiumyu.api.drawparticle;

import org.bukkit.Location;
import org.bukkit.Particle;

import static java.lang.Math.*;

public class CommonDraw {
    public static void spawnCircle(Location loc, int r, Particle p, int mode){
        double x= loc.getX();
        double y= loc.getY();
        double z= loc.getZ();
        int m =1;
        for (int i = 0; i <360*mode ; i++) {
            m=-m+3;
            loc.getWorld().spawnParticle(p,x+r*cos(toRadians(i)),y,z+r*sin(toRadians(i)),m);
        }
    }

    public static void spawnCircle(Location loc, int r, Particle p){
        double x= loc.getX();
        double y= loc.getY();
        double z= loc.getZ();
        int m=1;
        for (int i = 0; i <360 ; i++) {
            m=-m+3;
            loc.getWorld().spawnParticle(p,x+r*cos(toRadians(i)),y,z+r*sin(toRadians(i)),1);
        }
    }
}
