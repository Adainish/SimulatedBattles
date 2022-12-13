package io.github.adainish.simulatedbattles.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.core.LifeCycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Util {
    public static Optional <ServerPlayerEntity> getPlayerOptional(String name) {
        return Optional.ofNullable(SimulatedBattles.server.getPlayerList().getPlayerByUsername(name));
    }


    public static ServerPlayerEntity getPlayer(String playerName) {
        return SimulatedBattles.server.getPlayerList().getPlayerByUsername(playerName);
    }

    public static ServerPlayerEntity getPlayer(UUID uuid) {
        return SimulatedBattles.server.getPlayerList().getPlayerByUUID(uuid);
    }

    public static String formattedString(String s) {
        if (s == null)
            return "";
        return s.replaceAll("&", "§");
    }

    public static List <String> formattedArrayList(List<String> list) {

        List<String> formattedList = new ArrayList <>();
        for (String s:list) {
            formattedList.add(formattedString(s));
        }

        return formattedList;
    }

    public static void runCommand(String cmd) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            SimulatedBattles.log.warn("Could not execute commands, the server data could not be retrieved");
            return;
        }
        try {
            server.getCommandManager().getDispatcher().execute(cmd, server.getCommandSource());
        } catch (CommandSyntaxException e) {
            SimulatedBattles.log.error(e);
        }
    }

    public static void send(CommandSource sender, String message) {
        sender.sendFeedback(new StringTextComponent(((message).replaceAll("&([0-9a-fk-or])", "\u00a7$1"))), false);
    }

    public static void send(UUID uuid, String message) {
        getPlayer(uuid).sendMessage(new StringTextComponent(((message).replaceAll("&([0-9a-fk-or])", "\u00a7$1"))), uuid);
    }

    public static void send(ServerPlayerEntity player, String message) {
        if (player == null)
            return;
        player.sendMessage(new StringTextComponent(((message).replaceAll("&([0-9a-fk-or])", "\u00a7$1"))), player.getUniqueID());
    }
}
