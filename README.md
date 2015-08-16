# ConfigurationCommands
a sponge plugin for custom commands that can be createt via a configuration file or a command


## Usage
### Add a Command

You have two ways to add a new command: You can edit the configuration file of the plugin or add a command ingame with the help of the addCommand. 

#### ingame (addcmd)
To add a command ingame you need to use the addCommand (/addcmd <name> <command> <message...>).
The first parameter is the name / identifier for the command, that you later need to edit or delete the command.
The second parameter is a command that your users use to use the command, the last parameter is the message that is shown if the command is executed. To add a description, extended Description or a permission you need to edit the command with the editCmd.

#### config


### Edit a command

#### ingame (editcmd)
To edit a command ingame you need to use the command /editcmd [key] [commandName] [value].

| Key | value | example|
|----|:----:|---|
|command| a list of commands | /editcmd command [commandName] command1 command2 | 
|description| a text (can be in the json format) | /editcmd description [commandName] [{color:blue,text:\"A command\"}] | 
|extendedDescription| a text (can be in the json format) | /editcmd extendedDescription [commandName] a realy long description of the command | 
|message| a text (can be in the json format) | /editcmd message [commandName] [{color:blue,text:\"TS3:\"},{color:green,text:\" ts3.domain.tld\"}] | 
|permission| a permission node | /editcmd permission [commandName] cmd.ts3 | 

#### config

### Delete a command

## Permissions

| Permission     | Command    |
| -------------- |:----------:|
| confCmd.add    | /addcmd    |
| confCmd.edit   | /editcmd   |
| confCmd.delete | /deletecmd |
