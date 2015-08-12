package de.lergin.sponge.messageCommands.commands;

import de.lergin.sponge.messageCommands.util;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.Arrays;

/**
 *
 */
public class AddCommand implements CommandExecutor {
    private ConfigurationNode rootNode;

    public AddCommand(ConfigurationNode rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ConfigurationNode node = rootNode.getNode("commands", args.getOne("name").get().toString());


        node.getNode("message").setValue(
                args.getOne("message").get().toString()
        );

        node.getNode("commands").setValue(
                Arrays.asList(args.getOne("command").get().toString().split(" "))
        );

        util.saveConfig();
        util.registerCommand(node);


        return CommandResult.success();
    }
}
