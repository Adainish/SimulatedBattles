package io.github.adainish.simulatedbattles.conf;

import info.pixelmon.repack.org.spongepowered.serialize.SerializationException;
import io.github.adainish.simulatedbattles.SimulatedBattles;

import java.util.Arrays;

public class Config extends Configurable{
    private static Config config;

    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public void setup() {
        super.setup();
    }

    public void load() {
        super.load();
    }

    public void populate() {
        try {
            this.get().node("NPC", "Example", "PhaseOne", "Commands", "Win").set(Arrays.asList("broadcast %p% beat an npc"));
            this.get().node("NPC", "Example", "PhaseOne", "Commands", "Lose").set(Arrays.asList("broadcast %p% lost to an npc"));
            this.get().node("NPC", "Example", "PhaseOne", "Money", "Win").set(100);
            this.get().node("NPC", "Example", "PhaseOne", "Money", "Lose").set(100);
            this.get().node("NPC", "Example", "PhaseOne", "Message", "Win").set("Won");
            this.get().node("NPC", "Example", "PhaseOne", "Message", "Lose").set("Lost");
            this.get().node("NPC", "Example", "PhaseOne", "BattleType").set("Single");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "PokemonName").set("Ninetales");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Level").set(10).comment("The Pokemons level");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Gender").set("Female").comment("The Pokemons Gender");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "HeldItem").set("pixelmon:leftovers");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Form").set(0).comment("Decide the form for this pokemon, leave blank if none are to be set");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "NickName").set("").comment("Set the Pokemons Nick Name in Battle!");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Shiny").set(false).comment("Is this Pokemon Shiny?");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Texture").set("").comment("Apply a pokemon Texture if these are installed");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "Nature").set("Timid").comment("What Nature should be applied to this Pokemon");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "Ability").set("Flash Fire").comment("What Ability should this Pokemon have?");
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Dynamax").set(1).comment("Set the dynamax level for this Pokemon");
            //EVS
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "EVS", "HP").set(252);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "EVS", "ATK").set(252);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "EVS", "SPA").set(252);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "EVS", "DEF").set(252);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "EVS", "SPDEF").set(252);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "EVS", "SPD").set(252);
            //IVS
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "IVS", "HP").set(31);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "IVS", "ATK").set(31);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "IVS", "SPA").set(31);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "IVS", "DEF").set(31);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "IVS", "SPDEF").set(31);
            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "Stats", "IVS", "SPD").set(31);

            this.get().node("NPC", "Example", "PhaseOne", "Team", "Ninetales", "MoveSet").set(Arrays.asList("Quick Attack", "Hidden Power", "Shadow Ball", "Incinerate"));
        } catch (SerializationException e) {
            SimulatedBattles.log.error(e);
        }

    }

    public String getConfigName() {
        return "NPCRegistry.conf";
    }

    public Config() {}
}
