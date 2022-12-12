package io.github.adainish.simulatedbattles.listener;

import com.pixelmonmod.pixelmon.api.events.HealerEvent;
import io.github.adainish.simulatedbattles.obj.Healer;
import io.github.adainish.simulatedbattles.obj.Player;
import io.github.adainish.simulatedbattles.storage.PlayerStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HealerInteractionListener {

    @SubscribeEvent
    public void onHealerEvent(HealerEvent.Pre event)
    {
        if (event.isCanceled())
            return;

        Player player = PlayerStorage.getPlayer(event.player.getUniqueID());
        if (player != null) {
            Healer healer = new Healer();
            healer.setWorldName(event.player.world.getDimensionKey().getRegistryName().toString());
            healer.setPosX(event.pos.getX());
            healer.setPosY(event.pos.getY());
            healer.setPosZ(event.pos.getX());
            player.updateCache();
        }
    }
}
