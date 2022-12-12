package io.github.adainish.simulatedbattles.cmd;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.adainish.simulatedbattles.SimulatedBattles;
import io.github.adainish.simulatedbattles.obj.NPC;
import io.github.adainish.simulatedbattles.obj.Phase;
import io.github.adainish.simulatedbattles.util.PermissionUtil;
import io.github.adainish.simulatedbattles.util.Util;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class Command {
    public static LiteralArgumentBuilder <CommandSource> getCommand() {
        return Commands.literal("simulatedbattles")
                .requires(cs -> PermissionUtil.checkPermAsPlayer(cs, SimulatedBattles.permissionWrapper.adminPermission))
                .executes(cc -> {
                    Util.send(cc.getSource(), "Please provide a target player, a base npc, and the npc phase!");
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("player", StringArgumentType.string())
                        .executes(cc ->
                        {
                            String playerArg = StringArgumentType.getString(cc, "player");
                            if (Util.getPlayerOptional(playerArg).isPresent()) {
                                ServerPlayerEntity playerEntity = Util.getPlayerOptional(playerArg).get();
                                Util.send(cc.getSource(), "&cPlease provide a valid npc and npc phase!");
                            } else {
                                Util.send(cc.getSource(), "&cPlease provide a valid player! This player was not detected as online!");
                            }
                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("npc", StringArgumentType.string())
                                .executes(cc ->
                                {
                                    String playerArg = StringArgumentType.getString(cc, "player");
                                    String npcArg = StringArgumentType.getString(cc, "npc");
                                    if (Util.getPlayerOptional(playerArg).isPresent()) {
                                        NPC npc = SimulatedBattles.battleNPCWrapper.npcHashMap.get(npcArg);
                                        if (npc != null) {
                                            Util.send(cc.getSource(), "&cPlease provide a valid npc phase!");
                                        } else {
                                            Util.send(cc.getSource(), "&cThe provided NPC did not exist!!");
                                        }
                                    } else {
                                        Util.send(cc.getSource(), "&cPlease provide a valid player! This player was not detected as online!");
                                    }
                                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("phase", StringArgumentType.string())
                                        .executes(cc ->
                                        {
                                            String playerArg = StringArgumentType.getString(cc, "player");
                                            String npcArg = StringArgumentType.getString(cc, "npc");
                                            String npcPhase = StringArgumentType.getString(cc, "phase");
                                            if (Util.getPlayerOptional(playerArg).isPresent()) {
                                                ServerPlayerEntity playerEntity = Util.getPlayerOptional(playerArg).get();
                                                NPC npc = SimulatedBattles.battleNPCWrapper.npcHashMap.get(npcArg);
                                                if (npc != null) {
                                                    Phase phase = npc.phases.get(npcPhase);
                                                    if (phase != null) {
                                                        phase.startBattle(playerEntity);
                                                    } else {
                                                        Util.send(cc.getSource(), "&cThe provided npc phase did not exist!");
                                                    }
                                                } else {
                                                    Util.send(cc.getSource(), "&cPlease provide a valid npc phase!");
                                                }
                                            } else {
                                                Util.send(cc.getSource(), "&cPlease provide a valid player! This player was not detected as online!");
                                            }
                                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                )
                .then(Commands.literal("reload")
                        .requires(cs -> PermissionUtil.checkPermAsPlayer(cs, SimulatedBattles.permissionWrapper.adminPermission))
                        .executes(cc ->
                        {
                            cc.getSource().sendFeedback(new StringTextComponent("Reloading the configs and NPC Data, This can be a heavy action as it's going through all data and we highly recommend testing by rebooting your server. Check your Console for any errors!"), true);
                            SimulatedBattles.instance.reload();
                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                        })
                );
    }
}
