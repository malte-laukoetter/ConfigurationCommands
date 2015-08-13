/*
 * Copyright (c) 2015. Malte 'Lergin' Laukötter
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package de.lergin.sponge.messageCommands.commands;

import de.lergin.sponge.messageCommands.CommandSetting;
import de.lergin.sponge.messageCommands.MessageCommands;
import de.lergin.sponge.messageCommands.util;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import javax.annotation.RegEx;
import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * command for editing commands
 */
public class EditCommand implements CommandExecutor {
    private final MessageCommands plugin;

    public EditCommand(MessageCommands plugin){
        this.plugin = plugin;
    }


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ConfigurationNode node = (ConfigurationNode) args.getOne(
                plugin.resourceBundle.getString("command.param.name")
        ).get();

        switch ((CommandSetting) args.getOne(
                    plugin.resourceBundle.getString("command.param.setting")
        ).get()){
            case COMMAND:
                node.getNode("commands").setValue(Arrays.asList(args.getOne(
                        plugin.resourceBundle.getString("command.param.value")
                ).get().toString().split("\\s+")));
                break;

            case DESCRIPTION:
                node.getNode("description").setValue(args.getOne(
                        plugin.resourceBundle.getString("command.param.value")
                ).get());
                break;

            case EXTENDEDDESCRIPTION:
                node.getNode("extendedDescription").setValue(args.getOne(
                        plugin.resourceBundle.getString("command.param.value")
                ).get());
                break;

            case PERMISSION:
                node.getNode("permission").setValue(args.getOne(
                        plugin.resourceBundle.getString("command.param.value")
                ).get());
                break;

            case MESSAGE:
                node.getNode("message").setValue(args.getOne(
                        plugin.resourceBundle.getString("command.param.value")
                ).get());
                break;
        }

        util.saveConfig();
        util.reloadCommand(node);

        src.sendMessage(util.getTextFromJsonByKey("command.edit.success", node.getKey()));

        return CommandResult.success();
    }
}
