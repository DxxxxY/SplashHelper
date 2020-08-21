package cc.dxxxxy.sh;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig {
    public GuiConfig(GuiScreen guiScreen) {
        super(guiScreen,
                new ConfigElement(SplashHelper.getConfig().getCategory("shs")).getChildElements(),
                Reference.ID,
                false,
                false,
                "Splash Helper Settings");
    }
}
