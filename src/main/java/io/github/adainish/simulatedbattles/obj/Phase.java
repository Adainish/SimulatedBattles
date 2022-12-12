package io.github.adainish.simulatedbattles.obj;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.ServerPlayerEntity;

public class Phase {
    public String npcID;
    public String identifier;
    public Team team;
    public String winMessage;
    public String loseMessage;
    public int prizeMoney;

    public Phase(String npcID, String identifier)
    {
        this.npcID = npcID;
        this.identifier = identifier;
        //grab things from config
        generateTeam();
    }

    public void startBattle(ServerPlayerEntity playerEntity)
    {
        PlayerPartyStorage partyStorage = StorageProxy.getParty(playerEntity);
        NPCTrainer npcTrainer = npcTrainer(playerEntity);
        PlayerParticipant playerParticipant = new PlayerParticipant(playerEntity, partyStorage.getAndSendOutFirstAblePokemon(playerEntity));
        npcTrainer.startBattle(playerParticipant);
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

        npcTrainer.getPersistentData().putBoolean("simulatedBattleNPC", true);
        npcTrainer.getPersistentData().putString("npcID", npcID);
        npcTrainer.getPersistentData().putString("phaseID", identifier);
        npcTrainer.winMoney = prizeMoney;
        npcTrainer.winMessage = winMessage;
        npcTrainer.loseMessage = loseMessage;

        return npcTrainer;
    }

    public void generateTeam()
    {
        this.team = new Team(npcID, identifier);
    }
}
