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
package onl.area51.mapgen.util;

import java.awt.Color;
import java.util.Map;
import uk.trainwatch.util.MapBuilder;

/**
 * Exposes the default colours defined in {@link Color} to name lookup.
 * <p>
 * This is primarily used by the Job extensions
 *
 * @author peter
 */
public enum ColourType
{

    WHITE( Color.WHITE ),
    LIGHT_GRAY( Color.LIGHT_GRAY ),
    LIGHT_GREY( Color.LIGHT_GRAY ),
    GRAY( Color.GRAY ),
    GREY( Color.GRAY ),
    DARK_GRAY( Color.DARK_GRAY ),
    DARK_GREY( Color.DARK_GRAY ),
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

    private static final Map<String, ColourType> TYPES = MapBuilder.<String, ColourType>builder()
            .readonly()
            .concurrent()
            .keyMapper( n -> n == null ? "" : n.toUpperCase().trim() )
            .key( Enum::name )
            .addAll( values() )
            .key( v -> v.name().replaceAll( "_", " " ) )
            .addAll( values() )
            .build();

    public static ColourType lookup( String n )
    {
        return TYPES.getOrDefault( n, BLACK );
    }

    public static ColourType lookupOrNull( String n )
    {
        return TYPES.getOrDefault( n, null );
    }

    /**
     * Decode a colour of the format RGB, #RGB, RRGGBB or #RRGGBB
     *
     * @param s
     *
     * @return
     */
    public static Color decodeHex( String s )
    {
        String c = s.startsWith( "#" ) ? s.substring( 1 ) : s;
        switch( c.length() ) {
            // #rgb
            case 3: {
                int r = Integer.parseInt( c.substring( 0, 1 ), 16 );
                int g = Integer.parseInt( c.substring( 1, 2 ), 16 );
                int b = Integer.parseInt( c.substring( 2, 3 ), 16 );
                return new Color( r << 8 + r, g << 8 + g, b << 8 + b );
            }

            // #rrggbb
            case 6:
                return new Color(
                        Integer.parseInt( c.substring( 0, 2 ), 16 ),
                        Integer.parseInt( c.substring( 2, 4 ), 16 ),
                        Integer.parseInt( c.substring( 4, 6 ), 16 )
                );

            default:
                throw new IllegalArgumentException( "Invalid colour " + c );
        }
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
