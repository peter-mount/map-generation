/*
 * Copyright 2015 peter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package onl.area51.mapgen.renderer.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static onl.area51.mapgen.renderer.util.ImageType.INT_RGB;

/**
 *
 * @author peter
 */
public enum ColourType
{

    WHITE( Color.WHITE ),
    LIGHT_GRAY( Color.LIGHT_GRAY ),
    GRAY( Color.GRAY ),
    DARK_GRAY( Color.DARK_GRAY ),
    BLACK( Color.BLACK ),
    RED( Color.RED ),
    PINK( Color.PINK ),
    ORANGE( Color.ORANGE ),
    YELLOW( Color.YELLOW ),
    GREEN( Color.GREEN ),
    MAGENTA( Color.MAGENTA ),
    CYAN( Color.CYAN ),
    BLUE( Color.BLUE );
    private final Color colour;
    private static final Map<String, ColourType> TYPES = new ConcurrentHashMap<>();

    static {
        for( ColourType t: values() ) {
            String n = t.name();
            TYPES.put( n, t );
            if( n.indexOf( '_' ) > -1 ) {
                TYPES.put( n.replace( '_', ' ' ), t );
            }
        }
    }

    public static ColourType lookup( String n )
    {
        return TYPES.getOrDefault( n == null ? "" : n.toUpperCase().trim(), BLACK );
    }

    private ColourType( Color colour )
    {
        this.colour = colour;
    }

    public Color getColor()
    {
        return colour;
    }
}
