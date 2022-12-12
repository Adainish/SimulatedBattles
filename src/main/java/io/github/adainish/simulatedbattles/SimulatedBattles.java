package io.github.adainish.simulatedbattles;

import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.adainish.simulatedbattles.cmd.Command;
import io.github.adainish.simulatedbattles.conf.Config;
import io.github.adainish.simulatedbattles.listener.BattleListener;
import io.github.adainish.simulatedbattles.listener.HealerInteractionListener;
import io.github.adainish.simulatedbattles.listener.PlayerListener;
import io.github.adainish.simulatedbattles.obj.Player;
import io.github.adainish.simulatedbattles.wrapper.BattleNPCWrapper;
import io.github.adainish.simulatedbattles.wrapper.PermissionWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("simulatedbattles")
public class SimulatedBattles {

    public static SimulatedBattles instance;
    public static final String MOD_NAME = "SimulatedBattles";
    public static final String VERSION = "1.0.0-Beta";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2022";
    public static MinecraftServer server;
    public static File configDir;
    public static File storageDir;
    public static File playerStorageDir;
    public static final Logger log = LogManager.getLogger(MOD_NAME);

    public static PermissionWrapper permissionWrapper;
    public static BattleNPCWrapper battleNPCWrapper;


    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        SimulatedBattles.configDir = configDir;
    }

    public static File getPlayerStorageDir() {
        return playerStorageDir;
    }
    public static void setPlayerStorageDir(File playerStorageDir) {
        SimulatedBattles.playerStorageDir = playerStorageDir;
    }
    public static MinecraftServer getServer() {
        return server;
    }
    public static void setServer(MinecraftServer server) {
        SimulatedBattles.server = server;
    }

    public static HashMap<UUID, Player> playerCache = new HashMap <>();

    public SimulatedBattles() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        initDirs();
    }

    @SubscribeEvent
    public void onCommandRegistry(RegisterCommandsEvent event) {
        permissionWrapper = new PermissionWrapper();
        event.getDispatcher().register(Command.getCommand());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        setupConfigs();
        loadConfigs();
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        setServer(ServerLifecycleHooks.getCurrentServer());
        //load objects and wrapper
        battleNPCWrapper = new BattleNPCWrapper();
        //register events
         registerListeners();
    }
    public void reload()
    {
        log.warn("Reload Requested...");
        log.warn("Please beware that reloading large amounts of data can be heavy and is not recommended. Please reboot the server instead.");
        setupConfigs();
        loadConfigs();
        battleNPCWrapper = new BattleNPCWrapper();
    }

    public void registerListeners()
    {
        log.warn("Registering events for SimulatedBattles");
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
        Pixelmon.EVENT_BUS.register(new HealerInteractionListener());
        Pixelmon.EVENT_BUS.register(new BattleListener());
        log.warn("Registered events for SimulatedBattles");
    }

    public void initDirs() {
        log.log(Level.WARN, "Setting up Storage Paths and Directories for Simulated Battles");
        setConfigDir((new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).toString())));
        getConfigDir().mkdir();
        storageDir = new File(configDir + "/SimulatedBattles/Storage/");
        storageDir.mkdirs();
        playerStorageDir = new File(storageDir + "/PlayerData/");
        playerStorageDir.mkdirs();
        log.log(Level.WARN, "Finished setting up directories");
    }

    public void setupConfigs() {
        log.log(Level.WARN, "Setting up config data to be read by Simulated Battles");
        Config.getConfig().setup();
    }

    public void loadConfigs() {
        log.log(Level.WARN, "Loading and Reading Config Data for Simulated Battles");
        Config.getConfig().load();
    }

}
