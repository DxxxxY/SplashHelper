package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Gui extends net.minecraft.client.gui.Gui {
    Configuration config = SplashHelper.getConfig();

    Property isOn = config.get("shs", "Enabled", false);
    Property fee = config.get("shs", "Fee Amount", 0);
    Property joinMsg = config.get("shs", "Join Message", "");
    Property showInHub = config.get("shs", "Show Members in Hub", false);
    FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent e) {
        if (showInHub.getBoolean()) {
            if (e.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
                drawString(fr,"A", 9, 9, Color.cyan.getRGB());
            }
        }
    }
}
