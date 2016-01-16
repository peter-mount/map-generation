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

import java.util.DoubleSummaryStatistics;

/**
 * A range of itemised values.
 *
 * @author peter
 */
public interface RangedValue
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
     * The value at the specified index. This is usually used when generating legends
     *
     * @param index
     *
     * @return
     */
    double getValue( int index );

    /**
     * The number of entries in this range
     *
     * @return
     */
    int size();

    /**
     * Create a RangedValue based on statistics
     *
     * @param stats statistics
     * @param size  number of entries
     *
     * @return RangedValue
     */
    static RangedValue create( DoubleSummaryStatistics stats, int size )
    {
        return create( stats.getMin(), stats.getMax(), size );
    }

    /**
     * Create a RangedValue based on a range
     *
     * @param min  min value
     * @param max  max value
     * @param size number of entries
     *
     * @return RangedValue
     */
    static RangedValue create( double min, double max, int size )
    {
        if( min == max ) {
            throw new IllegalArgumentException( "Min must not equal max" );
        }
        if( min > max ) {
            return create( max, min, size );
        }

        int maxIndex = size - 1;
        
        // Note maxIndex not size here as we don't include index 0 in Δv
        double Δv = (max - min) / maxIndex;

        return new RangedValue()
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
                // FIXME this needs a better algorithm if we support large values
                double v = min;
                for( int i = 0; i < size; i++ ) {
                    v += Δv;
                    if( val < v ) {
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
                    return min;
                }
                else if( index > maxIndex ) {
                    return max;
                }
                return min + (index * Δv);
            }

            @Override
            public int size()
            {
                return size;
            }
        };
    }
}
