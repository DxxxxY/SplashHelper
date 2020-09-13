package cc.dxxxxy.sh;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.config.Configuration;

import static cc.dxxxxy.sh.SplashHelper.*;

public class SetItem extends CommandBase {
    Configuration config = getConfig();

    @Override
    public String getCommandName() { return "shset"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return null; }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        config.get("hidden", "Auction Item", "").set(Minecraft.getMinecraft().thePlayer.getHeldItem().getDisplayName().substring(2));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
}
