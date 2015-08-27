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
import de.lergin.sponge.messageCommands.util;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;


/**
 *
 */
public enum PlayerDataKey implements DataKey{
    BODY_ROTATIONS(Keys.BODY_ROTATIONS, DataTypes.VECTOR3D),
    DISPLAY_NAME(Keys.DISPLAY_NAME, DataTypes.TEXT),
    EXHAUSTION(Keys.EXHAUSTION,DataTypes.DOUBLE),
    EXPERIENCE_FROM_START_OF_LEVEL(Keys.EXPERIENCE_FROM_START_OF_LEVEL, DataTypes.INTEGER),
    EXPERIENCE_LEVEL(Keys.EXPERIENCE_LEVEL, DataTypes.INTEGER),
    EXPERIENCE_SINCE_LEVEL(Keys.EXPERIENCE_SINCE_LEVEL, DataTypes.INTEGER),
    EYE_HEIGHT(Keys.EYE_HEIGHT, DataTypes.DOUBLE),
    FIRE_DAMAGE_DELAY(Keys.FIRE_DAMAGE_DELAY, DataTypes.INTEGER),
    FIRE_TICKS(Keys.FIRE_TICKS, DataTypes.INTEGER),
    FIRST_DATE_PLAYED(Keys.FIRST_DATE_PLAYED, DataTypes.DATE),
    FOOD_LEVEL(Keys.FOOD_LEVEL, DataTypes.INTEGER),
    GAME_MODE(Keys.GAME_MODE, DataTypes.GAME_MODE),
    HEAD_ROTATION(Keys.HEAD_ROTATION,DataTypes.VECTOR3D),
    HEALTH(Keys.HEALTH, DataTypes.DOUBLE),
    HELD_EXPERIENCE(Keys.HELD_EXPERIENCE,DataTypes.INTEGER),
    IN_WALL(Keys.IN_WALL, DataTypes.BOOLEAN),
    INVISIBLE(Keys.INVISIBLE, DataTypes.BOOLEAN),
    INVULNERABILITY(Keys.INVULNERABILITY, DataTypes.INTEGER),
    INVULNERABILITY_TICKS(Keys.INVULNERABILITY_TICKS, DataTypes.INTEGER),
    IS_AFLAME(Keys.IS_AFLAME, DataTypes.BOOLEAN),
    IS_FLYING(Keys.IS_FLYING, DataTypes.BOOLEAN),
    IS_PLAYING(Keys.IS_PLAYING, DataTypes.BOOLEAN),
    IS_SITTING(Keys.IS_SITTING, DataTypes.BOOLEAN),
    IS_SLEEPING(Keys.IS_SLEEPING, DataTypes.BOOLEAN),
    IS_SNEAKING(Keys.IS_SNEAKING, DataTypes.BOOLEAN),
    IS_SPRINTING(Keys.IS_SPRINTING, DataTypes.BOOLEAN),
    IS_WHITELISTED(Keys.IS_WHITELISTED, DataTypes.BOOLEAN),
    LAST_ATTACKER(Keys.LAST_ATTACKER, DataTypes.LIVING),
    LAST_DAMAGE(Keys.LAST_DAMAGE, DataTypes.DOUBLE),
    LAST_DATE_PLAYED(Keys.LAST_DATE_PLAYED, DataTypes.DATE),
    LEFT_ARM_ROTATION(Keys.LEFT_ARM_ROTATION, DataTypes.VECTOR3D),
    LEFT_LEG_ROTATION(Keys.LEFT_LEG_ROTATION, DataTypes.VECTOR3D),
    MAX_HEALTH(Keys.MAX_HEALTH, DataTypes.DOUBLE),
    PASSENGER(Keys.PASSENGER, DataTypes.ENTITY),
    RIGHT_ARM_ROTATION(Keys.RIGHT_ARM_ROTATION, DataTypes.VECTOR3D),
    RIGHT_LEG_ROTATION(Keys.RIGHT_LEG_ROTATION, DataTypes.VECTOR3D),
    ROTATION(Keys.ROTATION, DataTypes.ROTATION),
    CHEST_ROTATION(Keys.CHEST_ROTATION, DataTypes.VECTOR3D),
    SATURATION(Keys.SATURATION, DataTypes.DOUBLE),
    SHOWS_DISPLAY_NAME(Keys.SHOWS_DISPLAY_NAME, DataTypes.BOOLEAN),
    SKIN(Keys.SKIN, DataTypes.UUID),
    SKIN_UUID(Keys.SKIN_UUID, DataTypes.UUID),
    TOTAL_EXPERIENCE(Keys.TOTAL_EXPERIENCE, DataTypes.INTEGER),
    VELOCITY(Keys.VELOCITY, DataTypes.VECTOR3D);

    Key key;
    DataTypes dataType;

    PlayerDataKey(Key key, DataTypes dataType) {
        this.key = key;
        this.dataType = dataType;
    }

    public String getData(Object player){
        try {
            Optional data = ((Player) player).get(key);

            if (data.isPresent()) {
                return dataType.getString(data.get());
            } else {
                return String.format(
                        util.getStringFromKey("error.data.missing"),
                        name()
                );
            }
        }
        catch(NullPointerException ex){
            return String.format(
                    util.getStringFromKey("error.data.missing"),
                    name()
            );
        }
    }
}
