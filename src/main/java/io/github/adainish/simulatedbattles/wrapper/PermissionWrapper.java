package io.github.adainish.simulatedbattles.wrapper;

import io.github.adainish.simulatedbattles.SimulatedBattles;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.Level;

public class PermissionWrapper {
    public String adminPermission = "simulatedbattles.admin";
    public PermissionWrapper()
    {
        registerCommandPermission(adminPermission);
    }

    public void registerCommandPermission(String s) {
        if (s == null || s.isEmpty()) {
            SimulatedBattles.log.log(Level.FATAL, "Trying to register a permission node failed, please check any configs for null/empty Configs");
            return;
        }
        PermissionAPI.registerNode(s, DefaultPermissionLevel.NONE, s);
    }

    public void registerCommandPermission(String s, String description) {
        if (s == null || s.isEmpty()) {
            SimulatedBattles.log.log(Level.FATAL, "Trying to register a permission node failed, please check any configs for null/empty Configs");
            return;
        }
        PermissionAPI.registerNode(s, DefaultPermissionLevel.NONE, description);
    }
}
