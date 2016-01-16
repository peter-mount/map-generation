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
        extends ColorMap
{

    /**
     * The minimum value
     *
     * @return
     */
    double getMinValue();

    /**
     * The maximum value
     *
     * @return
     */
    double getMaxValue();

    /**
     * The index in the map associated with a value
     *
     * @param val value
     *
     * @return index
     */
    int getIndex( double val );

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
     * The value at the specified index. This is usually used when generating legends
     *
     * @param index
     *
     * @return
     */
    double getValue( int index );

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

        int size = map.size(), maxIndex = size - 1;
        double Δv = (max - min) / size;
        double v[] = new double[size];
        v[0] = min;
        for( int i = 1; i < size; i++ ) {
            v[i] = v[i - 1] + Δv;
        }

        return new RangedColorMap()
        {
            @Override
            public double getMinValue()
            {
                return min;
            }

            @Override
            public double getMaxValue()
            {
                return max;
            }

            @Override
            public int getIndex( double val )
            {
                if( val <= min ) {
                    return 0;
                }
                if( val >= max ) {
                    return maxIndex;
                }
                for( int i = 1; i < size; i++ ) {
                    if( val < v[i] ) {
                        return i;
                    }
                }
                // Should never be reached
                return maxIndex;
            }

            @Override
            public double getValue( int index )
            {
                if( index < 0 ) {
                    return v[0];
                }
                else if( index > maxIndex ) {
                    return v[maxIndex];
                }
                return v[index];
            }

            @Override
            public Color getColor( int index )
            {
                return map.getColor( index );
            }

            @Override
            public int size()
            {
                return size;
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
}
