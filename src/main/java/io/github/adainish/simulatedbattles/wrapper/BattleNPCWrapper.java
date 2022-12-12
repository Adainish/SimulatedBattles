package io.github.adainish.simulatedbattles.wrapper;

import info.pixelmon.repack.org.spongepowered.CommentedConfigurationNode;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.conf.Config;
import io.github.adainish.simulatedbattles.obj.NPC;

import java.util.HashMap;
import java.util.Map;

public class BattleNPCWrapper {
    public HashMap<String, NPC> npcHashMap = new HashMap <>();

    public BattleNPCWrapper()
    {
        loadNPCs();
    }

    public void loadNPCs()
    {
        CommentedConfigurationNode node = Config.getConfig().get().node("NPC");
        Map <Object, CommentedConfigurationNode> nodeMap = node.childrenMap();
        for (Object obj: nodeMap.keySet()) {
            if (obj == null) {
                SimulatedBattles.log.error("OBJ Null while generating an NPC");
                continue;
            }
            String npcID = obj.toString();
            NPC npc = new NPC(npcID);
            npcHashMap.put(npcID, npc);
        }
    }
}
