package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Events {
    private final Configuration config = SplashHelper.getConfig();
    private final Logger logger = SplashHelper.getLogger();
    private final Minecraft mc = Minecraft.getMinecraft();
    private String name = null;
    private final Property isOn = config.get("shs", "Enabled", false);
    private final Property feeK = config.get("shs", "Fee Amount", 0);
    private final Property joinMsg = config.get("shs", "Join Message", "");
    public static ArrayList<String> members = new ArrayList<String>();


    public static String getBoardTitle(Scoreboard board) {
        ScoreObjective titleObjective = board.getObjectiveInDisplaySlot(1);
        if (board.getObjectiveInDisplaySlot(0) != null) {
            return board.getObjectiveInDisplaySlot(0).getName();
        } else {
            return board.getObjectiveInDisplaySlot(1).getName();
        }
    }

    public static void sendMessage(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage((IChatComponent) new ChatComponentText(msg));
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent e) {
        String msg = e.message.getFormattedText();
        if (mc.isSingleplayer()) return;
        if (!mc.getCurrentServerData().serverIP.contains(".hypixel.net")) return;
        if (!getBoardTitle(mc.theWorld.getScoreboard()).equals("SBScoreboard")) return;
        if (msg.contains(":")) return;
        if (isOn.getBoolean()) {
            if (msg.contains("Trade completed with")) {
                if (msg.contains("]")) {
                    name = msg.substring(msg.indexOf("]") + 2, msg.indexOf("!") - 4);
                    return;
                }
                name = msg.substring(msg.indexOf("h") + 6, msg.indexOf("!") - 4);
            }
            if (msg.contains("+") && msg.contains("coins") && name != null) {
                int fee = Integer.parseInt(msg.substring(15, msg.indexOf("k")));
                if (fee >= feeK.getInt()) {
                    mc.thePlayer.sendChatMessage("/p " + name);
                    name = null;
                }
            }
            if (!joinMsg.getString().equals("")) {
                if (msg.contains("joined the party.")) {
                    mc.thePlayer.sendChatMessage("/pc " + joinMsg.getString());
                }
            }
            if (msg.contains("joined the party.")) {
                if (!ReinviteCommand.reInv) {
                    if (msg.contains("]")) {
                        //sendMessage(msg.substring(msg.indexOf("]") + 2, msg.indexOf("joined") - 5));
                        members.add(msg.substring(msg.indexOf("]") + 2, msg.indexOf("joined the") - 5));
                        return;
                    }
                    //sendMessage(msg.substring(0, msg.indexOf("joined") - 5));
                    members.add(msg.substring(2, msg.indexOf("joined the") - 5));
                }
            }
            if (msg.contains("left the party.")) {
                if (msg.contains("You")) return;
                if (msg.contains("]")) {
                    members.remove(members.indexOf(msg.substring(msg.indexOf("]") + 2, msg.indexOf("has left") - 5)));
                    return;
                }
                members.remove(members.indexOf(msg.substring(2, msg.indexOf("has left") - 5)));
            }
            if (msg.contains("You have joined")) {
                if (isOn.getBoolean()) {
                    isOn.set(!isOn.getBoolean());
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyTyped(InputEvent.KeyInputEvent e) {
        if (Keyboard.getEventKey() == SplashHelper.openGui.getKeyCode()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiConfig(Minecraft.getMinecraft().currentScreen));
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent e) {
        if (e.modID.equals(Reference.ID)) {
            config.save();
        }
    }
}
