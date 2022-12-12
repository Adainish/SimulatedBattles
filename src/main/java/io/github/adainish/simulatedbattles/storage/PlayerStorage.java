package io.github.adainish.simulatedbattles.storage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.obj.Player;
import io.github.adainish.simulatedbattles.util.Adapters;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.*;
import java.util.UUID;

public class PlayerStorage {
    public static void makePlayer(ServerPlayerEntity player) {
        File dir = SimulatedBattles.getPlayerStorageDir();
        dir.mkdirs();


        Player playerData = new Player(player.getUniqueID());

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.getUniqueID())));
        if (file.exists()) {
            return;
        }

        Gson gson = Adapters.PRETTY_MAIN_GSON;
        String json = gson.toJson(playerData);

        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(Player player) {

        File dir = SimulatedBattles.getPlayerStorageDir();
        dir.mkdirs();

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.getUuid())));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            SimulatedBattles.log.error("Something went wrong attempting to read the Player Data");
            return;
        }


        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(player));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.updateCache();
    }

    public static Player getPlayer(UUID uuid) {
        File dir = SimulatedBattles.getPlayerStorageDir();
        dir.mkdirs();

        if (SimulatedBattles.playerCache.containsKey(uuid))
            return SimulatedBattles.playerCache.get(uuid);

        File guildFile = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(guildFile));
        } catch (FileNotFoundException e) {
            SimulatedBattles.log.error("Detected non-existing player, making new player data file");
            return null;
        }

        return gson.fromJson(reader, Player.class);
    }
}
