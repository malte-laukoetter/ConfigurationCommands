package de.lergin.sponge.messageCommands.commands;

import de.lergin.sponge.messageCommands.CommandSetting;
import de.lergin.sponge.messageCommands.util;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Game;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * command for editing commands
 */
public class EditCommand implements CommandExecutor {

    Game game;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        ConfigurationNode node = (ConfigurationNode) args.getOne("name").get();

        switch ((CommandSetting) args.getOne("setting").get()){
            case COMMAND:
                node.getNode("commands").setValue(Arrays.asList(args.getOne("value").get().toString().split(" ")));
                break;

            case DESCRIPTION:
                node.getNode("description").setValue(args.getOne("value").get());
                break;

            case EXTENDEDDESCRIPTION:
                node.getNode("extendedDescription").setValue(args.getOne("value").get());
                break;

            case PERMISSION:
                node.getNode("permission").setValue(args.getOne("value").get());
                break;

            case MESSAGE:
                node.getNode("message").setValue(args.getOne("value").get());
                break;
        }

        util.saveConfig();
        util.reloadCommand(node);


        return CommandResult.success();
    }
}
