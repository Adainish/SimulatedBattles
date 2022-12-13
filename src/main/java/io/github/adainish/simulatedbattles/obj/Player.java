package io.github.adainish.simulatedbattles.obj;

import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.storage.PlayerStorage;
import io.github.adainish.simulatedbattles.util.EconomyUtil;
import io.github.adainish.simulatedbattles.util.Util;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.List;
import java.util.UUID;

public class Player {
    public UUID uuid;
    public String userName;
    public Healer healer;

    public Player(UUID uuid)
    {
        setUuid(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setHealer(Healer healer) {
        this.healer = healer;
    }

    public Healer getHealer() {
        return healer;
    }

    public String getName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void updateCache() {
        if (SimulatedBattles.playerCache.containsKey(this.getUuid()))
            SimulatedBattles.playerCache.replace(this.getUuid(), this);
        else SimulatedBattles.playerCache.put(this.getUuid(), this);
    }

    public void savePlayer() {
        PlayerStorage.savePlayer(this);
    }

    public void playerWin(ServerPlayerEntity playerEntity, int amount, String winMessage, List <String> commands)
    {
        EconomyUtil.giveBalance(uuid, amount);
        if (commands.isEmpty())
            return;
        for (String s:commands) {
            Util.runCommand(s.replace("%p%", playerEntity.getName().getUnformattedComponentText()));
        }
    }
    public void whiteOut(ServerPlayerEntity playerEntity, int amount, String losingMessage, List <String> commands)
    {
        playerEntity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 4));
        if (healer != null) {
            healer.teleport(playerEntity);
        }
        EconomyUtil.takeBalance(uuid, amount);
        if (commands.isEmpty())
            return;
        for (String s:commands) {
            Util.runCommand(s.replace("%p%", playerEntity.getName().getUnformattedComponentText()));
        }
        //send dialogue maybe with a message?
        PlayerPartyStorage storage = StorageProxy.getParty(uuid);
        storage.heal();
    }

}
