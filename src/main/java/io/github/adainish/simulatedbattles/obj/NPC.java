package io.github.adainish.simulatedbattles.obj;

import info.pixelmon.repack.org.spongepowered.CommentedConfigurationNode;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.conf.Config;

import java.util.HashMap;
import java.util.Map;

public class NPC
{
    public String identifier;
    public HashMap<String, Phase> phases = new HashMap <>();

    public NPC(String identifier)
    {
        this.identifier = identifier;
        loadPhasesFromConfig();
    }

    public void loadPhasesFromConfig()
    {
        CommentedConfigurationNode node = Config.getConfig().get().node("NPC", this.identifier);
        Map <Object, CommentedConfigurationNode> nodeMap = node.childrenMap();
        for (Object obj: nodeMap.keySet()) {
            if (obj == null) {
                SimulatedBattles.log.error(this.identifier);
                SimulatedBattles.log.error("OBJ Null while generating a phase");
                continue;
            }
            String phaseID = obj.toString();
            Phase phase = new Phase(identifier, phaseID);
            phases.put(phaseID, phase);
        }
    }

}
