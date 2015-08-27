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

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.messageCommands.CommandSetting;
import de.lergin.sponge.messageCommands.MessageCommands;
import de.lergin.sponge.messageCommands.util;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.ArrayList;

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
                util.getStringFromKey("command.param.name")
        ).get();

        CommandSetting commandSetting = (CommandSetting) args.getOne(
                util.getStringFromKey("command.param.setting")
        ).get();


        if(args.hasAny("c")){
            node.removeChild(commandSetting.getName());
        }

        if(commandSetting.isList()){

            try {
                ArrayList<String> valueList = new ArrayList<>();

                valueList.addAll(
                        node.getNode(commandSetting.getName()).getList(TypeToken.of(String.class))
                );

                valueList.add(
                        args.getOne(
                                util.getStringFromKey("command.param.value")
                        ).get().toString()
                );

                node.getNode(commandSetting.getName()).setValue(
                        valueList
                );
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }

        }else{

            node.getNode(commandSetting.getName()).setValue(args.getOne(
                    util.getStringFromKey("command.param.value")
            ).get());

        }

        util.saveConfig();
        util.reloadCommand(node);

        src.sendMessage(util.getTextFromJsonByKey("command.edit.success", node.getKey()));

        return CommandResult.success();
    }
}
