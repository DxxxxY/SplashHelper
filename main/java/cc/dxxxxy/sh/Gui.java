package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;

public class Gui extends net.minecraft.client.gui.Gui {
    Configuration config = SplashHelper.getConfig();

    Property showInHub = config.get("shs", "Show Members in Hub", false);
    Property showPartySize = config.get("shs", "Show Party Size", false);

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent e) {
        if (showInHub.getBoolean()) {
            if (e.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
                drawString(Minecraft.getMinecraft().fontRendererObj, Events.inLobby + " party members in your lobby", 5, 5, Color.cyan.getRGB());
            }
        }
        if (showPartySize.getBoolean()) {
            if (e.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
                drawString(Minecraft.getMinecraft().fontRendererObj, Integer.toString(Events.members.size()) + " people in your party", 5, 15, Color.cyan.getRGB());
            }
        }
    }
}
