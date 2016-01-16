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
import java.util.DoubleSummaryStatistics;
import java.util.Objects;

/**
 * A Colour map which will map values to colours.
 *
 * @author peter
 */
public interface RangedColorMap
        extends ColorMap,
                RangedValue
{

    /**
     * The colour assigned to this value
     *
     * @param val value
     *
     * @return Color
     */
    default Color getColor( double val )
    {
        return getColor( getIndex( val ) );
    }

    /**
     * Return a RangedColorMap with the specified ColorMap and based on the supplied stats
     *
     * @param map   ColorMap map
     * @param stats Statistics
     *
     * @return
     */
    static RangedColorMap create( ColorMap map, DoubleSummaryStatistics stats )
    {
        Objects.requireNonNull( stats );
        return create( map, stats.getMin(), stats.getMax() );
    }

    /**
     * Create a RangedColorMap for the specified values
     *
     * @param map ColorMap
     * @param min min value
     * @param max max value
     *
     * @return
     */
    static RangedColorMap create( ColorMap map, double min, double max )
    {
        Objects.requireNonNull( map );
        if( min == max ) {
            throw new IllegalArgumentException( "Min must not equal max" );
        }
        if( min > max ) {
            return create( map, max, min );
        }

        return create( map, RangedValue.create( min, max, map.size() ) );
    }

    /**
     * Creates a RangedColorMap based on a ColorMap and a RangedValue
     *
     * @param map   ColorMap
     * @param range RangedValue
     *
     * @return RangedColorMap
     *
     * @throws IllegalArgumentException if the sizes of the ColorMap and RangedValue do not match
     */
    static RangedColorMap create( ColorMap map, RangedValue range )
    {
        Objects.requireNonNull( map );
        Objects.requireNonNull( range );
        if( map.size() != range.size() ) {
            throw new IllegalArgumentException( "ColorMap and RangedValue must be the same size" );
        }

        return new RangedColorMap()
        {
            @Override
            public double getMinValue()
            {
                return range.getMinValue();
            }

            @Override
            public double getMaxValue()
            {
                return range.getMaxValue();
            }

            @Override
            public int getIndex( double val )
            {
                return range.getIndex( val );
            }

            @Override
            public double getValue( int index )
            {
                return range.getValue( index );
            }

            @Override
            public Color getColor( int index )
            {
                return map.getColor( index );
            }

            @Override
            public int size()
            {
                return map.size();
            }
        };
    }

    /**
     * Create a RangedColorMap as a gradient between two colours
     *
     * @param a     Colour for minimum
     * @param b     Colour for maximum
     * @param size  Number of Colour's
     * @param stats Statistics
     *
     * @return map
     */
    static RangedColorMap gradient( Color a, Color b, int size, DoubleSummaryStatistics stats )
    {
        return create( ColorMap.gradient( a, b, size ), stats );
    }

    /**
     * Create a RangedColorMap as a gradient between two colours
     *
     * @param a    Colour for minimum
     * @param b    Colour for maximum
     * @param size Number of Colour's
     * @param min  minimum value
     * @param max  maximum value
     *
     * @return map
     */
    static RangedColorMap gradient( Color a, Color b, int size, double min, double max )
    {
        return create( ColorMap.gradient( a, b, size ), min, max );
    }

    /**
     * A 27 step ColorMap used for temperatures ranging from -18C to 34C
     *
     * @return
     */
    static RangedColorMap temp()
    {
        return create( ColorMap.temp(), -18, 34 );
    }

    /**
     * Creates a grey scaled RangedColorMap
     *
     * @param size number of entries, min 3 max 256
     *
     * @return RangedColorMap
     */
    static RangedColorMap greyScale( int size )
    {
        if( size < 3 || size > 256 ) {
            throw new IllegalArgumentException( "Size must be 3<=size<=256" );
        }

        return create( ColorMap.greyScale( size ), RangedValue.create( 0, 255, size ) );
    }
}
