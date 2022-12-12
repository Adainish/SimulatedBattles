package io.github.adainish.simulatedbattles.obj;

import com.pixelmonmod.pixelmon.api.util.helpers.DimensionHelper;
import net.minecraft.entity.player.ServerPlayerEntity;

public class Healer {
    private String worldName;
    private double posX;
    private double posY;
    private double posZ;

    public Healer(){}

    public void teleport(ServerPlayerEntity playerEntity)
    {
        DimensionHelper.teleport(playerEntity, worldName, posX, posY, posZ);
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }
}
