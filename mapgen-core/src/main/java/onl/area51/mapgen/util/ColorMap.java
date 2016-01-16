/*
 * Copyright 2016 peter.
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
import java.util.Arrays;
import java.util.Objects;

/**
 * An indexed Colour map which can be used to represent values as colours
 *
 * @author peter
 */
public interface ColorMap
{

    /**
     * The colour at a specific index
     *
     * @param index
     *
     * @return
     */
    Color getColor( int index );

    /**
     * The number of entries in this map
     *
     * @return
     */
    int size();

    /**
     * Returns a ColorMap containing the supplied colours
     *
     * @param c
     *
     * @return
     */
    static ColorMap create( Color... c )
    {
        Objects.requireNonNull( c );
        if( c.length < 1 ) {
            throw new IllegalArgumentException( "Invalid ColorMap size" );
        }
        // Copy of the array so we know it's immutable
        Color map[] = Arrays.copyOf( c, c.length );
        return new ColorMap()
        {
            @Override
            public Color getColor( int index )
            {
                return map[index];
            }

            @Override
            public int size()
            {
                return map.length;
            }
        };
    }

    /**
     * Create a gradient between two colours
     *
     * @param a    Starting colour at index 0
     * @param b    Ending colour at index size-1
     * @param size Number of colours in the map
     *
     * @return
     */
    static ColorMap gradient( Color a, Color b, int size )
    {
        Objects.requireNonNull( a );
        Objects.requireNonNull( b );
        if( size < 3 ) {
            throw new IllegalArgumentException( "Invalid ColorMap size" );
        }

        final float α = a.getAlpha() / 255f;

        // The colour components and the delta between each element
        float cc[] = a.getColorComponents( null );
        float Δc[] = b.getColorComponents( a.getColorSpace(), null );
        for( int i = 0; i < cc.length; i++ ) {
            Δc[i] = (Δc[i] - cc[i]) / size;
        }

        // Generate the map
        Color m[] = new Color[size];
        for( int i = 0; i < size; i++ ) {
            m[i] = new Color( a.getColorSpace(), cc, α );
            for( int j = 0; j < cc.length; j++ ) {
                cc[j] += Δc[j];
            }
        }

        // Own implementation rather than create() as no need to copy the array for security reasons
        return new ColorMap()
        {
            @Override
            public Color getColor( int index )
            {
                return m[index];
            }

            @Override
            public int size()
            {
                return size;
            }
        };
    }

    /**
     * A ColorMap consisting of a single colour.
     *
     * @param c Colour to return
     *
     * @return
     */
    static ColorMap single( Color c )
    {
        return single( c, 1 );
    }

    /**
     * A ColorMap consisting of a single colour.
     *
     * @param c    Colour to return
     * @param size the "size" of the map
     *
     * @return
     */
    static ColorMap single( Color c, int size )
    {
        Objects.requireNonNull( c );
        if( size < 1 ) {
            throw new IllegalArgumentException( "Invalid ColorMap size" );
        }
        return new ColorMap()
        {
            @Override
            public Color getColor( int index )
            {
                return c;
            }

            @Override
            public int size()
            {
                return size;
            }
        };
    }

    /**
     * A 11 step ColorMap used for rain/snow fall. Lowest is dark Green, middle brown then red to highest white
     *
     * @return
     */
    static ColorMap rainfall()
    {
        return create( ColorMaps.RAINFALL );
    }

    /**
     * A 27 step ColorMap used for temperatures with blue cold and red hot
     *
     * @return
     */
    static ColorMap temp()
    {
        return create( ColorMaps.TEMP );
    }
}
