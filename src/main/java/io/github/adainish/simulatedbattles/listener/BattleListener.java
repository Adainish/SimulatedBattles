package io.github.adainish.simulatedbattles.listener;

import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.obj.NPC;
import io.github.adainish.simulatedbattles.obj.Phase;
import io.github.adainish.simulatedbattles.obj.Player;
import io.github.adainish.simulatedbattles.storage.PlayerStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BattleListener {

    @SubscribeEvent
    public void onPlayerBeatNPC(BeatTrainerEvent event)
    {
        if (event.trainer.getPersistentData().getBoolean("simulatedBattleNPC")) {
            String npcID = event.trainer.getPersistentData().getString("npcID");
            NPC npc = SimulatedBattles.battleNPCWrapper.npcHashMap.get(npcID);
            if (npc != null) {
                String phaseID = event.trainer.getPersistentData().getString("phaseID");
                Phase phase = npc.phases.get(phaseID);
                if (phase != null) {
                    Player player = PlayerStorage.getPlayer(event.player.getUniqueID());
                    if (player != null) {
                        player.playerWin(event.player, phase.prizeMoney, phase.winMessage);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLostToNPC(LostToTrainerEvent event)
    {
        if (event.trainer.getPersistentData().getBoolean("simulatedBattleNPC")) {
            String npcID = event.trainer.getPersistentData().getString("npcID");
            NPC npc = SimulatedBattles.battleNPCWrapper.npcHashMap.get(npcID);
            if (npc != null) {
                String phaseID = event.trainer.getPersistentData().getString("phaseID");
                Phase phase = npc.phases.get(phaseID);
                if (phase != null) {
                    Player player = PlayerStorage.getPlayer(event.player.getUniqueID());
                    if (player != null) {
                        player.whiteOut(event.player, phase.prizeMoney, phase.loseMessage);
                    }
                }
            }
        }
    }

}
