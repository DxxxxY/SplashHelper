package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI)
public class SplashHelper {
    private static Configuration config;
    private static Logger logger = LogManager.getLogger(Reference.ID);
    static String currentVersion;
    static String newVersion;
    public static final KeyBinding openGui = new KeyBinding("Open Gui", Keyboard.KEY_RSHIFT, "Splash Helper");

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        File configFile = new File(Loader.instance().getConfigDir(), "sh.cfg");
        config = new Configuration(configFile);
        config.load();
        Property isOn = config.get("shs", "Enabled", false);
        Property fee = config.get("shs", "Fee Amount", 0);
        Property joinMsg = config.get("shs", "Join Message", "");
        Property showInHub = config.get("shs", "Show Members in Hub", false);
        if(config.hasChanged()){
            config.save();
        }
        ClientRegistry.registerKeyBinding(openGui);
        //ClientCommandHandler.instance.registerCommand(new ReinviteCommand());
        MinecraftForge.EVENT_BUS.register(new Events());
        //MinecraftForge.EVENT_BUS.register(new ReinviteCommand());
    }

    public void updateChecker() {
        try {
            HttpURLConnection c = (HttpURLConnection)new URL("https://api.spigotmc.org/legacy/update.php?resource=72777").openConnection();
            newVersion = new BufferedReader((Reader)new InputStreamReader(c.getInputStream())).readLine();
            c.disconnect();
            if (newVersion.equals(currentVersion)) {
                //On latest version
            }
            else {
                //Update available
            }
        }
        catch (IOException ex) {
            //Error
            ex.printStackTrace();
        }
    }

    public static Configuration getConfig() { return config; }

    public static Logger getLogger() { return logger; }
}
