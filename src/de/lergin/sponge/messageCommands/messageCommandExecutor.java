package de.lergin.sponge.messageCommands;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

/**
 *
 */
public class MessageCommandExecutor implements CommandExecutor {
    private final Text message;

    public MessageCommandExecutor(Text message) {
        this.message = message;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(message);
        return CommandResult.success();
    }
}