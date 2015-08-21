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
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.Game;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * main class
 */

@Plugin(
        id = "confCmd",
        name = "ConfigurationCommands",
        version = "1.0"
)
public class MessageCommands {
    @Inject
    @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    public Logger logger;

    @Inject
    public Game game;

    @Inject
    public PluginContainer container;

    public ConfigurationNode rootNode;
    public ResourceBundle resourceBundle;
    public HashMap<String, CommandMapping> confCommands = new HashMap<>();
    public HashMap<String, ConfigurationNode> commandMap = new HashMap<>();
    public HashMap<String, CommandSetting> commandSettings = new HashMap<>();
    public HashMap<String, Key> playerDataKeys = new HashMap<>();
    public CommandMapping editCommand;
    public CommandMapping deleteCommand;


    @Subscribe
    public void onServerStart(ServerStartedEvent event){
        util.setPlugin(this);

        //load translation
        try {
            File file = new File("config" + File.separator + "confCmd");
            URL[] urls = {file.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            resourceBundle = ResourceBundle.getBundle("translation", Locale.getDefault(), loader);
        }catch(Exception ex) {
            resourceBundle = ResourceBundle.getBundle("resources/translation");

            logger.info(resourceBundle.getString("error.no.custom.translation"));
        }

        //load config
        try {
            rootNode = configManager.load();
        } catch(IOException e) {
            rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());

            util.saveConfig();
        }



        try {
            Metrics metrics = new Metrics(game, container);
            metrics.start();
        } catch (IOException e) {
            logger.info(resourceBundle.getString("error.no.connection.mcStats"));
        }


        for(CommandSetting commandSetting : CommandSetting.values()){
            commandSettings.put(
                    commandSetting.toString(),
                    commandSetting);
        }

/*
        playerDataKeys.put("DISPLAY_NAME", Keys.DISPLAY_NAME);
        playerDataKeys.put("BODY_ROTATIONS", Keys.BODY_ROTATIONS);
        playerDataKeys.put("EXPERIENCE_FROM_START_OF_LEVEL", Keys.EXPERIENCE_FROM_START_OF_LEVEL);
        playerDataKeys.put("EXPERIENCE_LEVEL", Keys.EXPERIENCE_LEVEL);
        playerDataKeys.put("EXPERIENCE_SINCE_LEVEL", Keys.EXPERIENCE_SINCE_LEVEL);
        playerDataKeys.put("EYE_HEIGHT", Keys.EYE_HEIGHT);
        playerDataKeys.put("EYE_LOCATION", Keys.EYE_LOCATION);
        playerDataKeys.put("FALL_DAMAGE_PER_BLOCK", Keys.FALL_DAMAGE_PER_BLOCK);
        playerDataKeys.put("FIRST_DATE_PLAYED", Keys.FIRST_DATE_PLAYED);
        playerDataKeys.put("FOOD_LEVEL", Keys.FOOD_LEVEL);
        playerDataKeys.put("GAME_MODE", Keys.GAME_MODE);
        playerDataKeys.put("HEAD_ROTATION", Keys.HEAD_ROTATION);
        playerDataKeys.put("HEALTH", Keys.HEALTH);
        playerDataKeys.put("IN_WALL", Keys.IN_WALL);
        playerDataKeys.put("INVISIBLE", Keys.INVISIBLE);
        playerDataKeys.put("INVULNERABILITY", Keys.INVULNERABILITY);
        playerDataKeys.put("INVULNERABILITY_TICKS", Keys.INVULNERABILITY_TICKS);
        playerDataKeys.put("IS_AFLAME", Keys.IS_AFLAME);
        playerDataKeys.put("IS_FLYING", Keys.IS_FLYING);
        playerDataKeys.put("IS_PLAYING", Keys.IS_PLAYING);
        playerDataKeys.put("IS_SITTING", Keys.IS_SITTING);
        playerDataKeys.put("IS_SLEEPING", Keys.IS_SLEEPING);
        playerDataKeys.put("IS_SNEAKING", Keys.IS_SNEAKING);
        playerDataKeys.put("IS_SPRINTING", Keys.IS_SPRINTING);
        playerDataKeys.put("IS_WHITELISTED", Keys.IS_WHITELISTED);
        playerDataKeys.put("LAST_ATTACKER", Keys.LAST_ATTACKER);
        playerDataKeys.put("LAST_DAMAGE", Keys.LAST_DAMAGE);
        playerDataKeys.put("LAST_DATE_PLAYED", Keys.LAST_DATE_PLAYED);
        playerDataKeys.put("LEFT_ARM_ROTATION", Keys.LEFT_ARM_ROTATION);
        playerDataKeys.put("LEFT_LEG_ROTATION", Keys.LEFT_LEG_ROTATION);
        playerDataKeys.put("MAX_FALL_DAMAGE", Keys.MAX_FALL_DAMAGE);
        playerDataKeys.put("MAX_HEALTH", Keys.MAX_HEALTH);
        playerDataKeys.put("PASSENGER", Keys.PASSENGER);
        playerDataKeys.put("POTION_EFFECTS", Keys.POTION_EFFECTS);
        playerDataKeys.put("RESPAWN_LOCATIONS", Keys.RESPAWN_LOCATIONS);
        playerDataKeys.put("RIGHT_ARM_ROTATION", Keys.RIGHT_ARM_ROTATION);
        playerDataKeys.put("RIGHT_LEG_ROTATION", Keys.RIGHT_LEG_ROTATION);
        playerDataKeys.put("ROTATION", Keys.ROTATION);
        playerDataKeys.put("SATURATION", Keys.SATURATION);
        playerDataKeys.put("SHOWS_DISPLAY_NAME", Keys.SHOWS_DISPLAY_NAME);
        playerDataKeys.put("SKIN", Keys.SKIN);
        playerDataKeys.put("SKIN_UUID", Keys.SKIN_UUID);
        playerDataKeys.put("TOTAL_EXPERIENCE", Keys.TOTAL_EXPERIENCE);
*/




        for ( Map.Entry<Object, ? extends ConfigurationNode> entry :
                rootNode.getNode("commands").getChildrenMap().entrySet()) {
            commandMap.put(entry.getKey().toString(), entry.getValue());
        }


        CommandSpec addCmd = CommandSpec.builder()
                .permission("confCmd.add")
                .description(util.getTextFromJsonByKey("command.add.description"))
                .extendedDescription(util.getTextFromJsonByKey("command.add.extendedDescription"))
                .executor(new AddCommand(this))
                .arguments(
                        GenericArguments.string(Texts.of(
                                resourceBundle.getString("command.param.name")
                        )),
                        GenericArguments.string(Texts.of(
                                resourceBundle.getString("command.param.command")
                        )),
                        GenericArguments.remainingJoinedStrings(Texts.of(
                                resourceBundle.getString("command.param.message")
                        ))
                )
                .build();

        game.getCommandDispatcher().register(this,
                addCmd,
                resourceBundle.getString("command.add.command")
        ).get();


        util.createDeleteCmd();


        util.createEditCmd();

        logger.info(resourceBundle.getString("plugin.initialized"));



        for ( Map.Entry<Object, ? extends ConfigurationNode> entry :
                rootNode.getNode("commands").getChildrenMap().entrySet())
        {
            util.registerCommand(entry.getValue());

            logger.info(
                    String.format(resourceBundle.getString("command.initialized"), entry.getKey())
            );
        }
    }

    @Subscribe
    public void onServerStop(ServerStoppingEvent event){
        logger.info(resourceBundle.getString("plugin.stopped"));
    }
}
