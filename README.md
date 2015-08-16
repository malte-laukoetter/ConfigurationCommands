# ConfigurationCommands
A sponge plugin for custom commands that can be createt via a configuration file or a command, so that it is easy to use!


## Usage
### Add a Command

You have two ways to add a new command: You can edit the configuration file of the plugin or add a command ingame with the help of the addCommand. 

#### ingame (addcmd)
To add a command ingame you need to use the addCommand (/addcmd <name> <command> <message...>).
The first parameter is the name / identifier for the command, that you later need to edit or delete the command.
The second parameter is a command that your users use to use the command, the last parameter is the message that is shown if the command is executed. To add a description, extended Description or a permission you need to edit the command with the editCmd.

Example: `/addcmd tsCommand ts ts: ts.domain.tld`

#### config
For adding a command with the help of the config (the ingame methode also writes into the config) you can easy copy the following example code into the config and replace the texts with yours. The example adds the same command as the command of the ingame example after using each of the ingame edit examples.

```
commands {
    tsCommand {
        commands=[
            ts
            ts3
        ]
        description="[{color:blue,text:\"shows the ts ip\"}]"
        extendedDescription="shows you the currnd ip of the teamspeak server"
        message="[{color:blue,text:\"TS3:\"},{color:green,text:\" ts3.domain.tld\"}]"
        permission="cmd.ts3"
    }
    
    #copy the code starting from tsCommand until here to this position to add a anouther command
}
```

### Edit a command

#### ingame (editcmd)
To edit a command ingame you need to use the command `/editcmd [key] [commandName] [value]`.

key                | value                              | example
:------------------|:-----------------------------------|:-------------------------------------------------------------------
command            | a list of commands seperatet by whitespace | `/editcmd command tsCommand ts ts3`
description        | a text (can be in the json format) | `/editcmd description tsCommand [{color:blue,text:\"shows the ts ip\"}]`
extendedDescription| a text (can be in the json format) | `/editcmd extendedDescription tsCommand shows you the currnd ip of the teamspeak server`
message            | a text (can be in the json format) | `/editcmd message tsCommand [{color:blue,text:\"TS3:\"},{color:green,text:\" ts3.domain.tld\"}]`
permission         | a permission node                  | `/editcmd permission tsCommand cmd.ts3`

#### config

You can easy edit the node of the configuration file.

### Delete a command

#### ingame (deletecmd)

You only need to use the command /deletecmd <commandName> to delete a command. 

**IMPORTANT: the command can not be restored and will be deletet immediately and permanent!**


#### config

You can easy delete the node from the configuration file


## What does each key do

key                | what it does 
:------------------|:-------------------------------------------------------------------------------
command            | the command that you can enter to see the message
description        | the short description, that is shown in the help view
extendedDescription| the extended description shown in the help view when hovering above the command
message            | the message that is shown to the player when they use the command
permission         | the permission that is needed to use the command


## Permissions

| Permission       | Command      |
|:---------------- |:------------ |
| `confCmd.add`    | `/addcmd`    |
| `confCmd.edit`   | `/editcmd`   |
| `confCmd.delete` | `/deletecmd` |


## FAQ

### How do I create a multi line message?
You can create a new line by adding \n to the position of the linebreak. In the JSON-format it must be in the text block.

### Can I change the messages of the plugin?
Of course you can! Everything you need to do is copying the `translation.properties` file from the resource folder of github to the configuration folder of the plugin and change the textes to whatever you want them to be. If you translate them it would be nice if you would create a pull request with the translatet file or send it via mail to lergin@gmx.de so everyone can use it.

### How can I help you?
Feel free to create a pull request if you want to add a feature. You can also translate the plugin into anouther language if you want to!

### I found an issue / bug where can I report it?
On the lefthand site you can finde the issue tracker of github, feel free to add one there. But make sure your using the latest version and that the bug isn't reportet already ;)

### Can you add feature X?
Maybe. Just create an issue on the issue tracker and we will see :)
