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

import com.google.common.base.Optional;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;


/**
 *
 */
public enum PlayerDataKey implements DataKey{
    HEALTH(Keys.HEALTH, DataTypes.DOUBLE),
    //GAME_MODE(Keys.GAME_MODE, DataTypes.GAME_MODE),
    DISPLAY_NAME(Keys.DISPLAY_NAME, DataTypes.TEXT),
    FOOD_LEVEL(Keys.FOOD_LEVEL, DataTypes.INTEGER),
    //TOTAL_EXPERIENCE(Keys.TOTAL_EXPERIENCE, DataTypes.INTEGER),
    //SKIN(Keys.SKIN, DataTypes.UUID),
    VELOCITY(Keys.VELOCITY, DataTypes.VECTOR3D),
    EYE_LOCATION(Keys.EYE_LOCATION, DataTypes.VECTOR3D_COORDINATES);

    Key key;
    DataTypes dataType;

    PlayerDataKey(Key key, DataTypes dataType){
        this.key = key;
        this.dataType = dataType;
    }

    public String getData(Object player){

        Optional data = ((Player) player).get(key);

        if(data.isPresent()){
            return dataType.getString(data.get());
        } else {
            return String.format("%s data missing.", name());
        }
    }
}
