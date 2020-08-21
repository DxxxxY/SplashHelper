package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static cc.dxxxxy.sh.SplashHelper.*;

public class Events {
    private final Configuration config = getConfig();
    private final Logger logger = getLogger();
    private final Minecraft mc = Minecraft.getMinecraft();
    private String name = null;
    private final Property isOn = config.get("shs", "Enabled", false);
    private final Property feeK = config.get("shs", "Fee Amount", 0);
    private final Property joinMsg = config.get("shs", "Join Message", "");
    private final Property firstJoin = config.get("hidden", "firstJoin", true);
    public static ArrayList<String> members = new ArrayList<String>();
    public static int inLobby;

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (firstJoin.getBoolean()) {
            updateChecker();
            firstJoin.set(false);
        }
    }

    @SubscribeEvent
    public void onClientTick(LivingEvent.LivingUpdateEvent e) {
        inLobby = 0;
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            if (members.contains(p.getName())) {
                inLobby++;
            }
        }
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
                if (msg.contains("]")) {
                        //sendMessage(msg.substring(msg.indexOf("]") + 2, msg.indexOf("joined") - 5));
                        members.add(msg.substring(msg.indexOf("]") + 2, msg.indexOf("joined the") - 5));
                        return;
                }
                //sendMessage(msg.substring(0, msg.indexOf("joined") - 5));
                members.add(msg.substring(2, msg.indexOf("joined the") - 5));
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
