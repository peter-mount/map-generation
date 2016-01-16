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

import static onl.area51.mapgen.util.RangedColorMap.create;

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

    static RangedValue create( double min, double max, int size )
    {
        if( min == max ) {
            throw new IllegalArgumentException( "Min must not equal max" );
        }
        if( min > max ) {
            return create( max, min, size );
        }
        int maxIndex = size - 1;
        double Δv = (max - min) / size;
        double v[] = new double[size];
        v[0] = min;
        for( int i = 1; i < size; i++ ) {
            v[i] = v[i - 1] + Δv;
        }

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
            public int size()
            {
                return size;
            }
        };
    }
}
