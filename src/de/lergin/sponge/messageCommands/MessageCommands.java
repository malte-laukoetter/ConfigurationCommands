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

package de.lergin.sponge.messageCommands;

import com.google.inject.Inject;
import de.lergin.sponge.messageCommands.commands.AddCommand;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.Game;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.io.IOException;
import java.util.*;

/**
 * main class
 */

@Plugin(
        id = "msgCom",
        name = "MessageCommands",
        version = "1.0-SNAPSHOT"
)
public class MessageCommands {
    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    public Logger logger;

    @Inject
    public Game game;

    public ConfigurationNode rootNode;
    public ResourceBundle resourceBundle;
    public HashMap<String, CommandMapping> confCommands = new HashMap<>();
    public HashMap<String, ConfigurationNode> commandMap = new HashMap<>();
    public HashMap<String, CommandSetting> commandSettings = new HashMap<>();
    public CommandMapping editCommand;
    public CommandMapping deleteCommand;


    @Subscribe
    public void onServerStart(ServerStartedEvent event){
        util.setPlugin(this);

        //load translation
        resourceBundle = ResourceBundle.getBundle("resources/translation");

        //load config
        try {
            rootNode = configManager.load();
        } catch(IOException e) {
            rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());

            util.saveConfig();
        }

        commandSettings.put("message", CommandSetting.MESSAGE);
        commandSettings.put("description", CommandSetting.DESCRIPTION);
        commandSettings.put("extendedDescription", CommandSetting.EXTENDEDDESCRIPTION);
        commandSettings.put("permission", CommandSetting.PERMISSION);
        commandSettings.put("command", CommandSetting.COMMAND);

        for ( Map.Entry<Object, ? extends ConfigurationNode> entry :
                rootNode.getNode("commands").getChildrenMap().entrySet()) {
            commandMap.put(entry.getKey().toString(), entry.getValue());
        }


        CommandSpec addCmd = CommandSpec.builder()
                .permission("confCmd.add")
                .description(Texts.of("Add a new command"))
                .extendedDescription(Texts.of("Add a new command with confCmd"))
                .executor(new AddCommand(this))
                .arguments(
                        GenericArguments.string(Texts.of("name")),
                        GenericArguments.string(Texts.of("command")),
                        GenericArguments.remainingJoinedStrings(Texts.of("message"))
                )
                .build();

        game.getCommandDispatcher().register(this,
                addCmd,
                "addCmd"
        ).get();


        util.createDeleteCmd();


        util.createEditCmd();

        logger.info(resourceBundle.getString("plugin.initialized"));



        for ( Map.Entry<Object, ? extends ConfigurationNode> entry :
                rootNode.getNode("commands").getChildrenMap().entrySet())
        {
            util.registerCommand(entry.getValue());

            logger.info("Command \"" + entry.getKey() + "\" initialized");
        }
    }

    @Subscribe
    public void onServerStop(ServerStoppingEvent event){
        logger.info(resourceBundle.getString("plugin.stopped"));
    }
}
