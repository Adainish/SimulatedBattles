package io.github.adainish.simulatedbattles.listener;

import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.obj.Player;
import io.github.adainish.simulatedbattles.storage.PlayerStorage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;

public class PlayerListener {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() == null)
            return;

        Player player = PlayerStorage.getPlayer(event.getPlayer().getUniqueID());
        if (player == null) {
            PlayerStorage.makePlayer((ServerPlayerEntity) event.getPlayer());
            player = PlayerStorage.getPlayer(event.getPlayer().getUniqueID());
        }
        try {
            if (player == null)
                throw new NullPointerException("Player null even after making fresh player data? That's not good! Please contact the developer with more info!");
            player.setUserName(event.getPlayer().getName().getUnformattedComponentText());
            player.savePlayer();
        } catch (NullPointerException e) {
            SimulatedBattles.log.log(Level.ERROR, e);
        }

    }


    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() == null)
            return;
        Player player = PlayerStorage.getPlayer(event.getPlayer().getUniqueID());
        if (player == null) {
            PlayerStorage.makePlayer((ServerPlayerEntity) event.getPlayer());
            player = PlayerStorage.getPlayer(event.getPlayer().getUniqueID());
        }

        try {
            player.savePlayer();
        } catch (NullPointerException e) {

        }

    }
}
