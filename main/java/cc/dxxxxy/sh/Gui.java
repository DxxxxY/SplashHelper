package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;

public class Gui extends net.minecraft.client.gui.Gui {
    Configuration config = SplashHelper.getConfig();

    Property counters = config.get("shs", "Counters", false);

    Property autoTrade = config.get("shs", "Auto-Party Trade", false);
    Property feeTrade = config.get("shs", "Fee Trade", 0);

    Property autoAuction = config.get("shs", "Auto-Party Auction", false);
    Property feeAuction = config.get("shs", "Fee Auction", 0);
    Property feeItem = config.get("hidden", "Auction Item", "");

    Property joinMsg = config.get("shs", "Join Message", "");

    public String boolToString(boolean bool) {
        if (bool) {
            return EnumChatFormatting.GREEN + "Enabled";
        } else {
            return EnumChatFormatting.RED + "Disabled";
        }
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent e) {
        if (e.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            drawString(Minecraft.getMinecraft().fontRendererObj, "Splash Helper " + Reference.VERSION, 5, 5, Color.green.getRGB());
            drawString(Minecraft.getMinecraft().fontRendererObj, "[Counters] " + boolToString(counters.getBoolean()), 10, 15, Color.cyan.getRGB());
            if (counters.getBoolean()) {
                drawString(Minecraft.getMinecraft().fontRendererObj, "[Party In Lobby] " + EnumChatFormatting.YELLOW + Events.inLobby, 15, 25, Color.cyan.getRGB());
                drawString(Minecraft.getMinecraft().fontRendererObj, "[Party Size] " + EnumChatFormatting.YELLOW + Integer.toString(Events.members.size()), 15, 35, Color.cyan.getRGB());
            }
            drawString(Minecraft.getMinecraft().fontRendererObj, "[Auto-Party Trade] " + boolToString(autoTrade.getBoolean()), 10, 45, Color.cyan.getRGB());
            if (autoTrade.getBoolean()) {
                drawString(Minecraft.getMinecraft().fontRendererObj, "[Fee] " + EnumChatFormatting.YELLOW + feeTrade.getInt() + "k", 15, 55, Color.cyan.getRGB());
            }
            drawString(Minecraft.getMinecraft().fontRendererObj, "[Auto-Party Auction] " + boolToString(autoAuction.getBoolean()), 10, 65, Color.cyan.getRGB());
            if (autoAuction.getBoolean()) {
                drawString(Minecraft.getMinecraft().fontRendererObj, "[Fee] " + EnumChatFormatting.YELLOW + feeAuction.getInt() + "k", 15, 75, Color.cyan.getRGB());
                drawString(Minecraft.getMinecraft().fontRendererObj, "[Item] " + EnumChatFormatting.YELLOW + feeItem.getString(), 15, 85, Color.cyan.getRGB());
            }
            drawString(Minecraft.getMinecraft().fontRendererObj, "[Auto-Message] " + boolToString(!joinMsg.getString().equals("")), 10, 95, Color.cyan.getRGB());
            if (!joinMsg.getString().equals("")) {
                drawString(Minecraft.getMinecraft().fontRendererObj, "[Message] " + EnumChatFormatting.YELLOW + joinMsg.getString(), 15, 105, Color.cyan.getRGB());
            }
        }
    }
}
