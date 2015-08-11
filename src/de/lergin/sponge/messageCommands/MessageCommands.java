/*
 * Copyright (c) 2015 Malte 'Lergin' Laukötter
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.lergin.sponge.messageCommands;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.Game;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    private Logger logger;


    private ConfigurationNode getConfig() {
        return rootNode;
    }

    private ConfigurationNode rootNode;
    private ResourceBundle resourceBundle;

    @Inject
    private static Game game;

    @Subscribe
    public void onServerStart(){
        //load translation
        resourceBundle = ResourceBundle.getBundle("resources/translation", Locale.GERMAN);

        //load config
        try {
            rootNode = configManager.load();
        } catch(IOException e) {
            rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());

            try {
                configManager.save(rootNode);
            } catch (IOException ex) {
                logger.error(resourceBundle.getString("failed.to.write.config"));
                logger.error(ex.getMessage());
            }
        }


        for ( Map.Entry<Object, ? extends ConfigurationNode> entry :
                rootNode.getNode("commands").getChildrenMap().entrySet())
        {
            ConfigurationNode node = entry.getValue();

            //create command
            CommandSpec commandSpec = CommandSpec.builder()
                    .description(
                            util.getTextFromJson(node.getNode("description").getString(""))
                    )
                    .extendedDescription(
                            util.getTextFromJson(node.getNode("extendedDescription").getString(""))
                    )
                    .permission(
                            node.getNode("permission").getString("")
                    )
                    .executor(
                            new messageCommandExecutor(
                                    util.getTextFromJson(node.getNode("message").getString(""))
                            )
                    )
                    .build();

            try {
                //register command
                game.getCommandDispatcher().register(this,
                        commandSpec,
                        node.getNode("aliase").getList(TypeToken.of(String.class))
                );
            } catch (ObjectMappingException e) {
                logger.error(e.getMessage());
            } catch (IllegalArgumentException e){
                logger.warn("Your using the same alias multiple times: ");
                logger.warn(e.getLocalizedMessage());
            }

            logger.info("Command \"" + entry.getKey() + "\" initialized");
        }

        logger.info(resourceBundle.getString("plugin.initialized"));
    }

    @Subscribe
    public void onServerStop(){
        try {
            configManager.save(rootNode);
        } catch (IOException e) {
            logger.error(resourceBundle.getString("failed.to.write.config"));
            logger.error(e.getMessage());
        }

        logger.info(resourceBundle.getString("plugin.stopped"));
    }
}
