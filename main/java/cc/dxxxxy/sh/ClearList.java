package cc.dxxxxy.sh;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class ClearList extends CommandBase {
    @Override
    public String getCommandName() { return "shclear"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return null; }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException { Events.members.clear(); }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
}
