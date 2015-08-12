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
            plugin.logger.error(plugin.resourceBundle.getString("failed.to.write.config"));
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
     * reloads the command
     * @param node the configuration node of the command
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
        CommandSpec.Builder commandSpecBuilder = CommandSpec.builder()
                .executor(
                        new MessageCommandExecutor(
                                util.getTextFromJson(node.getNode("message").getString(""))
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
            plugin.logger.error("every command need at least one command");
        } catch (ObjectMappingException e) {
            plugin.logger.error(e.getMessage());
        } catch (IllegalArgumentException e){
            plugin.logger.warn("Your using the same command multiple times: ");
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
                .description(Texts.of("Edit a command"))
                .extendedDescription(Texts.of("edit a command created with confCmd"))
                .executor(new EditCommand())
                .arguments(
                        GenericArguments.choices(Texts.of("setting"), plugin.commandSettings),
                        GenericArguments.choices(Texts.of("name"), plugin.commandMap),
                        GenericArguments.remainingJoinedStrings(Texts.of("value"))
                )
                .build();

        plugin.editCommand = plugin.game.getCommandDispatcher().register(plugin,
                editCmd,
                "editCmd"
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
                .description(Texts.of("deletes a command"))
                .extendedDescription(Texts.of("Deletes the command from confCmd"))
                .executor(new DeleteCommand(plugin))
                .arguments(
                        GenericArguments.choices(Texts.of("name"), plugin.commandMap)
                )
                .build();

        plugin.deleteCommand = plugin.game.getCommandDispatcher().register(plugin,
                deleteCmd,
                "deleteCmd"
        ).get();
    }
}
