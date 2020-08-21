package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;

public class ReinviteCommand extends CommandBase {
    private final Logger logger = SplashHelper.getLogger();
    public static boolean reInv;
    public String rem;
    public int tickcount;
    public int counter;
    @Override
    public String getCommandName() {
        return "sh-reinv";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        reInv = true;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ClientTickEvent e) {
        if (e.phase.equals(TickEvent.Phase.END)) {
            if (reInv) {
                tickcount++;
                if (counter > 0) {
                    Events.members.remove(rem);
                }
                for (String name : Events.members) {
                    if (tickcount == 2 * 20) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/p " + name);
                        rem = name;
                        tickcount = 0;
                        counter++;
                    }
                }
                if (Events.members.isEmpty()) {
                    reInv = false;
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
}
