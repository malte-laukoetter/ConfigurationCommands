/*
 * Copyright (c) 2015. Malte 'Lergin' Laukötter
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

package de.lergin.sponge.messageCommands.data;

import de.lergin.sponge.messageCommands.util;

import java.util.Date;

/**
 *
 */
public enum ServerDataKeys implements DataKey {
    PLAYER_ONLINE(DataTypes.INTEGER)
            {
                public Object getDataValue() {
                    return util.getGame().getServer().getOnlinePlayers().size();
                }
            },
    SLOTS(DataTypes.INTEGER)
            {
                public Object getDataValue() {
                    return util.getGame().getServer().getMaxPlayers();
                }
            },
    MOTD(DataTypes.TEXT)
            {
                public Object getDataValue() {
                    return util.getGame().getServer().getMotd();
                }
            },
    HAS_WHITELIST(DataTypes.BOOLEAN)
            {
                public Object getDataValue() {
                    return util.getGame().getServer().hasWhitelist();
                }
            },
    ONLINE_MODE(DataTypes.BOOLEAN)
            {
                @Override
                public Object getDataValue(){
                    return util.getGame().getServer().getOnlineMode();
                }
            },
    SPONGE_API_VERSION(DataTypes.STRING)
            {
                @Override
                public Object getDataValue() {
                    return  util.getGame().getPlatform().getApiVersion();
                }
            },
    MINECRAFT_VERSION(DataTypes.STRING)
            {
                @Override
                public Object getDataValue() {
                    return  util.getGame().getPlatform().getMinecraftVersion().getName();
                }
            },
    VERSION(DataTypes.STRING)
            {
                @Override
                public Object getDataValue() {
                    return  util.getGame().getPlatform().getVersion();
                }
            },
    DATE(DataTypes.DATE)
            {
                @Override
                public Object getDataValue() {
                    return new Date();
                }
            },
    TIME(DataTypes.TIME)
            {
                @Override
                public Object getDataValue() {
                    return new Date();
                }
            },
    TYPE(DataTypes.STRING)
            {
                @Override
                public Object getDataValue() {
                    return util.getGame().getPlatform().getExecutionType().toString();
                }
            }
    ;

    private final DataType dataType;

    ServerDataKeys(DataType dataType) {
        this.dataType = dataType;
    }

    public Object getDataValue(){
        return null;
    }


    public String getData() {
        return dataType.getString(getDataValue());
    }
}
