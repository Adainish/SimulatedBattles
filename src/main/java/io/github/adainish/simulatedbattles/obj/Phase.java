package io.github.adainish.simulatedbattles.obj;

import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.TrainerChat;
import info.pixelmon.repack.org.spongepowered.serialize.SerializationException;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.conf.Config;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phase {
    public String npcID;
    public String identifier;
    public String npcName = "";
    public Team team;
    public String winMessage;
    public String loseMessage;
    public int prizeMoney;
    public int loseMoney;
    public BattleType battleType;
    public List <String> winCommands = new ArrayList <>();
    public List <String> loseCommands = new ArrayList <>();

    public Phase(String npcID, String identifier) {
        this.npcID = npcID;
        this.identifier = identifier;

        //grab things from config

        this.npcName = Config.getConfig().get().node("NPC", npcID, identifier, "NPCName").getString();

        this.prizeMoney = Config.getConfig().get().node("NPC", npcID, identifier, "Money", "Win").getInt();
        this.loseMoney = Config.getConfig().get().node("NPC", npcID, identifier, "Money", "Lose").getInt();

        this.winMessage = Config.getConfig().get().node("NPC", npcID, identifier, "Message", "Win").getString();
        this.loseMessage = Config.getConfig().get().node("NPC", npcID, identifier, "Message", "Lose").getString();
        try {
            this.winCommands = Config.getConfig().get().node("NPC", npcID, identifier, "Commands", "Win").getList(TypeToken.get(String.class));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        try {
            this.loseCommands = Config.getConfig().get().node("NPC", npcID, identifier, "Commands", "Lose").getList(TypeToken.get(String.class));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        String parsedBattleType = Config.getConfig().get().node("NPC", npcID, identifier, "BattleType").getString();
        if (getBattleTypeFromString(parsedBattleType) != null) {
            this.battleType = getBattleTypeFromString(parsedBattleType);
        } else {
            SimulatedBattles.log.warn("Battle Type was not valid, defaulting to Singles");
            this.battleType = BattleType.SINGLE;
        }
        generateTeam();
    }

    public BattleType getBattleTypeFromString(String parsed)
    {
        for (BattleType bt:BattleType.values()) {
            if (bt.name().equalsIgnoreCase(parsed))
                return bt;
        }
        return null;
    }

    public void startBattle(ServerPlayerEntity playerEntity)
    {
        PlayerPartyStorage partyStorage = StorageProxy.getParty(playerEntity);
        NPCTrainer npcTrainer = npcTrainer(playerEntity);
        playerEntity.world.addEntity(npcTrainer);
        npcTrainer.setInvisible(true);
        TrainerParticipant trainerParticipant = new TrainerParticipant(npcTrainer, 1);
        PlayerParticipant playerParticipant = new PlayerParticipant(playerEntity, partyStorage.getAndSendOutFirstAblePokemon(playerEntity));
        BattleRules rules = new BattleRules(battleType);
        BattleRegistry.startBattle(new BattleParticipant[]{playerParticipant}, new BattleParticipant[]{trainerParticipant}, rules);
    }

    public NPCTrainer npcTrainer(ServerPlayerEntity player)
    {
        NPCTrainer npcTrainer = new NPCTrainer(player.world);
        for(int i = 0; i < 6; ++i) {
            npcTrainer.getPokemonStorage().set(i, null);
        }

        for (Pokemon p:team.pokemonList) {
            if (p == null)
                continue;
            npcTrainer.getPokemonStorage().add(p);
        }

        npcTrainer.setName(npcName);
        ItemStack[] emptyArray = new ItemStack[]{};
        SetTrainerData setTrainerData = new SetTrainerData(npcName, "", winMessage, loseMessage, 0, emptyArray, new BattleRules(battleType));
        npcTrainer.update(setTrainerData);
        npcTrainer.getPersistentData().putBoolean("simulatedBattleNPC", true);
        npcTrainer.getPersistentData().putString("npcID", npcID);
        npcTrainer.getPersistentData().putString("phaseID", identifier);


        return npcTrainer;
    }

    public void generateTeam()
    {
        this.team = new Team(npcID, identifier);
    }
}
