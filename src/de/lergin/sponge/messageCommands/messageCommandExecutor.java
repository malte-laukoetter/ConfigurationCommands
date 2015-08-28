/*
 * Copyright (c) 2015. Malte 'Lergin' Lauk�tter
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
import de.lergin.sponge.messageCommands.data.PlayerDataKey;
import de.lergin.sponge.messageCommands.data.ServerDataKeys;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * the commandExecutor for custom commands
 */
public class messageCommandExecutor implements CommandExecutor {
    private final String message;
    private List<String> consoleCommandList = new ArrayList<>();
    private List<String> playerCommandList = new ArrayList<>();
    private Boolean hasPlayerKey = false;


    /**
     * creates a new custom command
     * @param node the configurationNode of the command
     */
    public messageCommandExecutor(ConfigurationNode node) {
        this.message = node.getNode(CommandSetting.MESSAGE.getName()).getString("");

        try {
            this.playerCommandList = node.getNode(CommandSetting.COMMANDS_PLAYER.getName()).getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        try {
            this.consoleCommandList = node.getNode("consoleCommands").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        for(PlayerDataKey playerDataKey : PlayerDataKey.values()){
            if(message.contains("PLAYER." + playerDataKey.name())) {
                hasPlayerKey = true;
                break;
            }
        }
    }



    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String sendMessage = message;

        if(hasPlayerKey) {
            if (!args.hasAny(util.getPlugin().resourceBundle.getString("command.param.player"))
                    && !(src instanceof Player)) {
                src.sendMessage(util.getTextFromJsonByKey("error.need.player"));
                return CommandResult.success();
            } else if (args.hasAny(
                    util.getStringFromKey("command.param.player")
            )) {
                sendMessage = replacePlayerKeys(
                        sendMessage,
                        (Player) args.getOne(util.getStringFromKey("command.param.player")).get()
                );
            } else {
                sendMessage = replacePlayerKeys(sendMessage, (Player) src);
            }
        }


        src.sendMessage(util.getTextFromJson(replaceServerKeys(sendMessage)));

        CommandService commandService = util.getPlugin().game.getCommandDispatcher();

        for(String command : playerCommandList){
            commandService.process(src, command);
        }

        for(String command : consoleCommandList){
            commandService.process(util.getPlugin().game.getServer().getConsole(), command);
        }

        return CommandResult.success();
    }


    public String replacePlayerKeys(String text, Player player){
        for(PlayerDataKey playerDataKey : PlayerDataKey.values()){
            if(text.contains("PLAYER." + playerDataKey.name())) {
                text = text.replace("PLAYER." + playerDataKey.name(), playerDataKey.getData(player));
            }
        }

        return text;
    }

    public String replaceServerKeys(String text){
        for(ServerDataKeys serverDataKey : ServerDataKeys.values()){
            if(text.contains("SERVER." + serverDataKey.name())) {
                text = text.replace("SERVER." + serverDataKey.name(), serverDataKey.getData());
            }
        }

        return text;
    }
}