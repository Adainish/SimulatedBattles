package io.github.adainish.simulatedbattles.obj;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import info.pixelmon.repack.org.spongepowered.CommentedConfigurationNode;
import info.pixelmon.repack.org.spongepowered.serialize.SerializationException;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.conf.Config;
import io.leangen.geantyref.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Team {
    public String npcID;
    public String phaseID;
    public List <Pokemon> pokemonList = new ArrayList <>();

    public Team(String npcID, String phaseID)
    {
        this.npcID = npcID;
        this.phaseID = phaseID;
        loadPokemon();
    }

    public void loadPokemon()
    {
        List<Pokemon> pokemonList = new ArrayList<>();
        CommentedConfigurationNode node = Config.getConfig().get().node("NPC", this.npcID, this.phaseID, "Team");
        Map <Object, CommentedConfigurationNode> nodeMap = node.childrenMap();
        for (Object obj: nodeMap.keySet()) {
            if (obj == null) {
                SimulatedBattles.log.error(this.npcID + " " + this.phaseID);
                SimulatedBattles.log.error("OBJ Null while generating NPC Pokemon");
                continue;
            }
            String nodestring = obj.toString();
            if (generateNPCPokemon(this.npcID, this.phaseID, nodestring) == null) {
                SimulatedBattles.log.error("There was an error generating the Pokemon for " + this.npcID + " " + this.phaseID +  " " + nodestring);
                continue;
            }
            Pokemon pokemon = generateNPCPokemon(this.npcID, this.phaseID, nodestring);
            pokemonList.add(pokemon);

        }
        this.pokemonList.addAll(pokemonList);

    }

    public Pokemon generateNPCPokemon(String npcID, String phaseID, String pokemonID) {

        String pokemonName = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "PokemonName").getString();
        String form = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Form").getString();
        int level = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Level").getInt();
        String gender = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Gender").getString();
        String nickname = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "NickName").getString();
        boolean shiny = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Shiny").getBoolean();
        String texture = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Texture").getString();
        String nature = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "Nature").getString();
        String ability = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "Ability").getString();
        int dynamaxLevel = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Dynamax").getInt();
        String heldItem = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "HeldItem").getString();
        List<String> moves = new ArrayList <>();
        try {
            moves = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "MoveSet").getList(TypeToken.get(String.class));
        } catch (SerializationException e) {
            SimulatedBattles.log.error(e);
        }

        List<String> specFlags = new ArrayList <>();
        try {
            specFlags = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "SpecFlags").getList(TypeToken.get(String.class));
        } catch (SerializationException e) {
            SimulatedBattles.log.error(e);
        }
        //Evs

        int evsHP = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "EVS", "HP").getInt();
        int evsATK = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "EVS", "ATK").getInt();
        int evsSPA = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "EVS", "SPA").getInt();
        int evsDEF = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "EVS", "DEF").getInt();
        int evsSPDEF = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "EVS", "SPDEF").getInt();
        int evsSPD = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "EVS", "SPD").getInt();
        //IVS
        int ivsHP = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "IVS", "HP").getInt();
        int ivsATK = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "IVS", "ATK").getInt();
        int ivsSPA = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "IVS", "SPA").getInt();
        int ivsDEF = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "IVS", "DEF").getInt();
        int ivsSPDEF = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "IVS", "SPDEF").getInt();
        int ivsSPD = Config.getConfig().get().node("NPC", npcID, phaseID, "Team", pokemonID, "Stats", "IVS", "SPD").getInt();


        if (pokemonName == null) {
            SimulatedBattles.log.info("The PokemonName doesn't exist, please check your config!");
            return null;
        }

        if (!PixelmonSpecies.get(pokemonName).isPresent()) {
            SimulatedBattles.log.info("The Pokemon Species %mon% doesn't exist!".replaceAll("%mon%", pokemonName));
            return null;
        }

        Pokemon pokemon = PokemonFactory.create(PixelmonSpecies.get(pokemonName).get().orElse(PixelmonSpecies.MAGIKARP)).toPokemon();
        pokemon.setLevel(level);

        pokemon.getIVs().setStat(BattleStatsType.HP, ivsHP);
        pokemon.getIVs().setStat(BattleStatsType.ATTACK, ivsATK);
        pokemon.getIVs().setStat(BattleStatsType.SPECIAL_ATTACK, ivsSPA);
        pokemon.getIVs().setStat(BattleStatsType.DEFENSE, ivsDEF);
        pokemon.getIVs().setStat(BattleStatsType.SPECIAL_DEFENSE, ivsSPDEF);
        pokemon.getIVs().setStat(BattleStatsType.SPEED, ivsSPD);


        pokemon.getEVs().setStat(BattleStatsType.HP, evsHP);
        pokemon.getEVs().setStat(BattleStatsType.ATTACK, evsATK);
        pokemon.getEVs().setStat(BattleStatsType.SPECIAL_ATTACK, evsSPA);
        pokemon.getEVs().setStat(BattleStatsType.DEFENSE, evsDEF);
        pokemon.getEVs().setStat(BattleStatsType.SPECIAL_DEFENSE, evsSPDEF);
        pokemon.getEVs().setStat(BattleStatsType.SPEED, evsSPD);

        pokemon.setGrowth(EnumGrowth.Ordinary);
        if (nickname != null) {
            if (!nickname.isEmpty())
                pokemon.setNickname(new StringTextComponent(nickname));
        }

        if (heldItem != null)
            if (!heldItem.isEmpty()) {
                ResourceLocation location = new ResourceLocation(heldItem);
                Item op = ForgeRegistries.ITEMS.getValue(location);
                if (op == null) {
                    SimulatedBattles.log.error("The Item for the pokemon held item could not be created, thus the pokemon was given an oran berry");
                    op = PixelmonItems.oran_berry;
                }
                ItemStack itemStack = new ItemStack(op);
                if (!itemStack.isEmpty()) //Ignore, this can still be null due to the String being editable by administrators
                    pokemon.setHeldItem(itemStack);
                else
                    SimulatedBattles.log.error("The ItemStack couldn't be created for npc pokemon %pokemon%".replaceAll("%pokemon%", pokemonName));
            }



        if (form != null && !form.isEmpty())
            if (pokemon.getSpecies().hasForm(form))
                pokemon.setForm(form);
            else pokemon.setForm(pokemon.getSpecies().getDefaultForm());

        if (gender != null && Gender.getGender(gender) != null)
        {
            pokemon.setGender(Gender.getGender(gender));
        } else {
            SimulatedBattles.log.error("The specified Gender for %p% was invalid, it's now set to the default generated gender!");
        }

        pokemon.setShiny(shiny);

        if (texture != null) {
            if (!texture.isEmpty()) {
                // TODO: 17/06/2022
                //  add texture support
            }
        }

        if (Nature.natureFromString(nature) != null)
            pokemon.setNature(Nature.natureFromString(nature));
        else {
            pokemon.setNature(Nature.getRandomNature());
            SimulatedBattles.log.info("There was an issue generating the nature for %pokemon%, please check your config for any errors".replace("%pokemon%", pokemonName));
        }
        pokemon.getMoveset().clear();
        if (moves != null) {
            for (String s:moves) {
                Attack atk = new Attack(s);
                if (AttackRegistry.getAttackBase(s).isPresent()) {
                    pokemon.getMoveset().add(atk);
                } else {
                    SimulatedBattles.log.info("The %move% for %pokemon% doesn't exist! skipping move!".replace("%pokemon%", pokemonName).replace("%move%", s));
                }
            }
        } else {
            pokemon.rerollMoveset();
            SimulatedBattles.log.info("The moves for %pokemon% returned null, generating random movelist".replace("%pokemon%", pokemonName));
        }
        if (ability != null) {
            if (AbilityRegistry.getAbility(ability).isPresent()) {
                pokemon.setAbility(AbilityRegistry.getAbility(ability));
            } else {
                SimulatedBattles.log.info("There was an issue generating the ability for %pokemon%, %ability% doesn't exist according to pixelmon. Please check your config for any errors".replace("%pokemon%", pokemonName).replace("%ability%", ability));
                pokemon.setAbility(pokemon.getForm().getAbilities().getRandomAbility());
            }
        } else {
            pokemon.setAbility(pokemon.getForm().getAbilities().getRandomAbility());
            SimulatedBattles.log.info("There was an issue generating the ability for %pokemon% due to a nullpointer being detected in the config, please check your config for any errors".replace("%pokemon%", pokemonName));
        }
        pokemon.setDynamaxLevel(dynamaxLevel);
        pokemon.setDoesLevel(false);
        pokemon.addFlag("simulatedbattlesmon");
        if (specFlags != null && !specFlags.isEmpty()) {
            for (String s : specFlags) {
                pokemon.addFlag(s);
            }
        }
        return pokemon;
    }


}
