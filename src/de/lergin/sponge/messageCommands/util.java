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

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.messageCommands.commands.DeleteCommand;
import de.lergin.sponge.messageCommands.commands.EditCommand;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.io.IOException;

/**
 * helper methodes
 */
public class util {
    private static MessageCommands plugin;

    public static void setPlugin(MessageCommands plugin){
        util.plugin = plugin;
    }


    /**
     * returns a text object that is created from a json string
     * @param text the json string
     * @return the text object
     */
    public static Text getTextFromJson(String text){
        if(text.length() > 0)
            return Texts.json().fromUnchecked(text);

        return Texts.of(text);
    }

    public static Text getTextFromJsonByKey(String key, Object... arguments){
        return getTextFromJson(
                String.format(
                        plugin.resourceBundle.getString(key), arguments
                )
        );
    }

    public static Text getTextFromJsonByKey(String key){
        return getTextFromJson(
                plugin.resourceBundle.getString(key)
        );
    }


    /**
     * returns a text object that is created from a json string
     * @param text the json string
     * @param arguments replace arguments
     * @return the text object
     */
    public static Text getTextFromJson(String text, Object... arguments){
        return Texts.json().fromUnchecked(
                String.format(text, arguments)
        );
    }

    /**
     * saves the config
     */
    public static void saveConfig(){
        try {
            plugin.configManager.save(plugin.rootNode);
        } catch (IOException ex) {
            plugin.logger.error(plugin.resourceBundle.getString("error.config.write.failed"));
            plugin.logger.error(ex.getMessage());
        }
    }

    /**
     * reloads the command
     * @param node the configuration node of the command
     */
    public static void reloadCommand(ConfigurationNode node){
        deleteCommand(node.getKey().toString());
        registerCommand(node);
    }

    /**
     * deletes the command
     * @param key the key of the command
     */
    public static void deleteCommand(String key){
        plugin.game.getCommandDispatcher().removeMapping(
                plugin.confCommands.get(key)
        );
    }

    /**
     * registers a command from a configuration node
     * @param node the configuration node of the command
     */
    public static void registerCommand(ConfigurationNode node){
        //create command
        CommandSpec.Builder commandSpecBuilder;

        commandSpecBuilder = CommandSpec.builder()
                .executor(
                        new MessageCommandExecutor(
                                node
                        )
                );

        if(!node.getNode("permission").isVirtual()){
            commandSpecBuilder.permission(node.getNode("permission").getString());
        }

        if(!node.getNode("description").isVirtual()){
            commandSpecBuilder.description(
                    util.getTextFromJson(node.getNode("description").getString())
            );
        }

        if(!node.getNode("extendedDescription").isVirtual()){
            commandSpecBuilder.extendedDescription(
                    util.getTextFromJson(node.getNode("extendedDescription").getString())
            );
        }

        CommandSpec commandSpec = commandSpecBuilder.build();

        try {
            //register command
            plugin.confCommands.put(
                    node.getKey().toString(),
                    plugin.game.getCommandDispatcher().register(plugin,
                            commandSpec,
                            node.getNode("commands").getList(TypeToken.of(String.class))
                    ).get()
            );
        } catch (IllegalStateException e){
            plugin.logger.error(
                    plugin.resourceBundle.getString("error.commend.zero")
            );
        } catch (ObjectMappingException e) {
            plugin.logger.error(e.getMessage());
        } catch (IllegalArgumentException e){
            plugin.logger.warn(
                    plugin.resourceBundle.getString("error.command.multiple.times")
            );
            plugin.logger.warn(e.getLocalizedMessage());
        }
    }

    /**
     * updates the edit command (eg. to reload the autocomplete)
     */
    public static void updateEditCmd(){
        plugin.game.getCommandDispatcher().removeMapping(plugin.editCommand);

        createEditCmd();
    }

    /**
     * creates the edit command
     */
    public static void createEditCmd(){
        CommandSpec editCmd = CommandSpec.builder()
                .permission("confCmd.edit")
                .description(getTextFromJsonByKey("command.edit.description"))
                .extendedDescription(getTextFromJsonByKey("command.edit.extendedDescription"))
                .executor(new EditCommand(plugin))
                .arguments(
                        GenericArguments.choices(Texts.of(
                                plugin.resourceBundle.getString("command.param.setting")
                        ), plugin.commandSettings),
                        GenericArguments.choices(Texts.of(
                                plugin.resourceBundle.getString("command.param.name")
                        ), plugin.commandMap),
                        GenericArguments.flags()
                                .flag("c")
                                .buildWith(
                                        GenericArguments.remainingJoinedStrings(Texts.of(
                                                plugin.resourceBundle.getString("command.param.value")
                                        ))
                                )
                )
                .build();

        plugin.editCommand = plugin.game.getCommandDispatcher().register(plugin,
                editCmd,
                plugin.resourceBundle.getString("command.edit.command")
        ).get();
    }

    /**
     * updates the delete command (eg. to reload the autocomplete)
     */
    public static void updateDeleteCmd(){
        plugin.game.getCommandDispatcher().removeMapping(plugin.deleteCommand);

        createDeleteCmd();
    }

    /**
     * creates the delete command
     */
    public static void createDeleteCmd(){
        CommandSpec deleteCmd = CommandSpec.builder()
                .permission("confCmd.delete")
                .description(util.getTextFromJsonByKey("command.delete.description"))
                .extendedDescription(util.getTextFromJsonByKey("command.delete.extendedDescription"))
                .executor(new DeleteCommand(plugin))
                .arguments(
                        GenericArguments.choices(
                                Texts.of(
                                        plugin.resourceBundle.getString("command.param.name")
                                ),
                                plugin.commandMap
                        )
                )
                .build();

        plugin.deleteCommand = plugin.game.getCommandDispatcher().register(plugin,
                deleteCmd,
                plugin.resourceBundle.getString("command.delete.command")
        ).get();
    }

    public static MessageCommands getPlugin() {
        return plugin;
    }
}
