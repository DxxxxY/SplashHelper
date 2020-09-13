package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static cc.dxxxxy.sh.SplashHelper.*;

public class Events {
    private final Configuration config = getConfig();
    private final Minecraft mc = Minecraft.getMinecraft();
    private String name = null;

    private final Property autoTrade = config.get("shs", "Auto-Party Trade", false);
    private final Property feeTrade = config.get("shs", "Fee Trade", 0);

    private final Property autoAuction = config.get("shs", "Auto-Party Auction", false);
    private final Property feeAuction = config.get("shs", "Fee Auction", 0);
    private final Property feeItem = config.get("hidden", "Auction Item", "");

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
        String msg = e.message.getUnformattedText();
        if (mc.isSingleplayer()) return;
        if (!mc.getCurrentServerData().serverIP.contains(".hypixel.net")) return;
        if (msg.contains("   ")) return;
        if (!getBoardTitle(mc.theWorld.getScoreboard()).equals("SBScoreboard")) return;
        if (msg.contains(":")) return;
        if (autoTrade.getBoolean()) {
            if (msg.contains("Trade completed with")) {
                if (msg.contains("]")) {
                    name = msg.substring(msg.indexOf("]") + 2, msg.indexOf("!"));
                    return;
                }
                name = msg.substring(msg.indexOf("h") + 2, msg.indexOf("!"));
            }
            if (msg.contains("+") && msg.contains("coins") && name != null) {
                int fee = Integer.parseInt(msg.substring(3, msg.indexOf("k")));
                if (fee >= feeTrade.getInt()) {
                    mc.thePlayer.sendChatMessage("/p " + name);
                    name = null;
                }
            }
        }
        if (autoAuction.getBoolean()) {
            if (msg.contains("[Auction]") && msg.contains("bought")) {
                if (msg.contains(feeItem.getString())) {
                    name = msg.substring(msg.indexOf("]") + 4, msg.indexOf("bought") - 2);
                    int fee = Integer.parseInt((msg.substring(msg.indexOf("for") + 6, msg.indexOf("coins") - 1)).replaceAll(",", ""));
                    if (fee >= feeAuction.getInt()) {
                        mc.thePlayer.sendChatMessage("/p " + name);
                        name = null;
                    }
                }
            }
        }
        if (!joinMsg.getString().equals("")) {
            if (msg.contains("joined the party.")) {
                mc.thePlayer.sendChatMessage("/pc " + joinMsg.getString());
            }
        }
        if (msg.contains("joined the party.")) {
            if (msg.contains("]")) {
                members.add(msg.substring(msg.indexOf("]") + 2, msg.indexOf("joined the") - 1));
                return;
            }
            members.add(msg.substring(2, msg.indexOf("joined the") - 1));
        }
        if (msg.contains("left the party.")) {
            if (msg.contains("You")) return;
            if (msg.contains("]")) {
                members.remove(members.indexOf(msg.substring(msg.indexOf("]") + 2, msg.indexOf("has left") - 1)));
                return;
            }
            members.remove(members.indexOf(msg.substring(2, msg.indexOf("has left") - 1)));
        }
        if (msg.contains("has been removed")) {
            if (msg.contains("]")) {
                members.remove(members.indexOf(msg.substring(msg.indexOf("]") + 2, msg.indexOf("has been removed") - 1)));
                return;
            }
            members.remove(members.indexOf(msg.substring(2, msg.indexOf("has left") - 1)));
        }
        if (msg.contains("You have joined")) {
            if (!joinMsg.getString().equals("")) {
                joinMsg.set("");
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
