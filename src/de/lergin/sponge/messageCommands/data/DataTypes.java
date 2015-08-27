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

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.rotation.Rotation;

import java.util.Date;

/**
 *
 */
public enum DataTypes implements DataType {
    STRING,
    INTEGER,
    DOUBLE,
    UUID,
    TEXT {
        @Override
        public String getString(Object text) {
            return Texts.json().to((Text) text);
        }

        @Override
        public Text getText(Object text) {
            return (Text) text;
        }
    },
    BOOLEAN {
        @Override
        public String getString(Object bool) {
            if((Boolean) bool){
                return "ja";
            }else{
                return "nein";
            }
        }
    },
    GAME_MODE {
        @Override
        public String getString(Object gm) {
            return ((GameMode) gm).getName();
        }
    },
    VECTOR3D_COORDINATES {
        @Override
        public String getString(Object vector) {
            Vector3d vector3d = (Vector3d) vector;
            return String.format("x:%d y:%d z:%d", vector3d.getFloorX(), vector3d.getFloorY(), vector3d.getFloorZ());
        }
    },
    VECTOR3D {
        @Override
        public String getString(Object vector) {
            Vector3d vector3d = (Vector3d) vector;
            return String.format("x:%f y:%f z:%f", vector3d.getX(), vector3d.getY(), vector3d.getZ());
        }
    },
    DATE {
        @Override
        public String getString(Object obj) {
            Date date = (Date) obj;

            return date.toString();
        }
    },
    LIVING {
        @Override
        public String getString(Object obj) {
            Living living = (Living) obj;

            return living.getType().getName();
        }
    },
    ENTITY {
        @Override
        public String getString(Object obj) {
            Entity entity = (Entity) obj;

            return entity.getType().getName();
        }
    },
    ROTATION {
        @Override
    public String getString(Object obj) {
            Rotation rotation = (Rotation) obj;

            return rotation.getName();
        }
    }
}
