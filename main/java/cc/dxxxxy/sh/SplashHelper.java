package cc.dxxxxy.sh;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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
    static String newVersion;
    public static final KeyBinding openGui = new KeyBinding("Open Gui", Keyboard.KEY_RSHIFT, "Splash Helper");

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        File configFile = new File(Loader.instance().getConfigDir(), "sh.cfg");
        config = new Configuration(configFile);
        config.load();
        config.get("shs", "Enabled", false);
        config.get("shs", "Fee Amount", 0);
        config.get("shs", "Join Message", "");
        config.get("shs", "Show Members in Hub", false);
        config.get("shs", "Show Party Size", false);
        config.get("hidden", "firstJoin", true).set(true);
        if(config.hasChanged()){
            config.save();
        }
        ClientCommandHandler.instance.registerCommand(new ClearList());
        ClientRegistry.registerKeyBinding(openGui);
        MinecraftForge.EVENT_BUS.register(new Gui());
        MinecraftForge.EVENT_BUS.register(new Events());
    }

    public static void sendMessage(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage((IChatComponent) new ChatComponentText(msg));
    }

    public static void updateChecker() {
        try {
            HttpURLConnection c = (HttpURLConnection)new URL("https://raw.githubusercontent.com/DxxxxY/SplashHelper/master/version.txt").openConnection();
            newVersion = new BufferedReader((Reader)new InputStreamReader(c.getInputStream())).readLine();
            c.disconnect();
            if (newVersion.equals(Reference.VERSION)) {
                sendMessage(ChatFormatting.BOLD+ "[" + ChatFormatting.RESET + ChatFormatting.AQUA + ChatFormatting.BOLD + "SH" + ChatFormatting.RESET + ChatFormatting.BOLD + "]" + ChatFormatting.RESET + ChatFormatting.GREEN + " You are using the latest version of SplashHelper");
            }
            else {
                sendMessage(ChatFormatting.BOLD+ "[" + ChatFormatting.RESET + ChatFormatting.AQUA + ChatFormatting.BOLD + "SH" + ChatFormatting.RESET + ChatFormatting.BOLD + "]" + ChatFormatting.RESET + ChatFormatting.RED + " You are using an outdated version of SplashHelper, get the new one here: https://github.com/DxxxxY/SplashHelper/releases");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getBoardTitle(Scoreboard board) {
        ScoreObjective titleObjective = board.getObjectiveInDisplaySlot(1);
        if (board.getObjectiveInDisplaySlot(0) != null) {
            return board.getObjectiveInDisplaySlot(0).getName();
        } else {
            return board.getObjectiveInDisplaySlot(1).getName();
        }
    }

    public static Configuration getConfig() { return config; }

    public static Logger getLogger() { return logger; }
}
