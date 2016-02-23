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
package onl.area51.mapgen.linear;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A representation of linear data
 *
 * @see onl.area51.mapgen.grid.Grid
 * @author peter
 * @param <X> type of axis
 */
public interface Linear<X>
{

    /**
     * The minimum value of the axis
     *
     * @return
     */
    X getMinimumX();

    /**
     * The maximum value of the axis
     *
     * @return
     */
    X getMaximumX();

    /**
     * The value at a position on the axis
     *
     * @param x position on axis
     * @return value or NaN if none present
     */
    default double getValue( X x )
    {
        return Double.NaN;
    }

    /**
     * Run through all possible values of the axis
     *
     * @param action
     */
    void forEach( BiConsumer<X, Double> action );

    /**
     * Return a stream of values on this axis
     *
     * @return
     */
    Stream<LinearPoint<X>> stream();

    /**
     * return a subset of this linear
     *
     * @param min
     * @param max
     * @return
     */
    Linear<X> subset( X min, X max );

    static Linear<Integer> createLinearArray( double... data )
    {
        return createLinear( 0, data.length, x -> data[x] );
    }

    static Linear<Integer> createScaledLinearArray( double scale, double... data )
    {
        return createLinear( 0, data.length, x -> data[x] );
    }

//    static Linear<Double> createScaledLinear( double min, double max, double... data )
//    {
//        return createLinear( min, max, (max - min) / data.length, x -> data[x] );
//    }

    static Linear<Integer> createLinear( int min, int max )
    {
        if( min > max )
        {
            return createLinear( max, min );
        }
        return new Linear<Integer>()
        {
            @Override
            public Integer getMinimumX()
            {
                return min;
            }

            @Override
            public Integer getMaximumX()
            {
                return max;
            }

            @Override
            public void forEach( BiConsumer<Integer, Double> action )
            {
                LinearSupport.forEach( min, max, x -> action.accept( x, Double.NaN ) );
            }

            @Override
            public Stream<LinearPoint<Integer>> stream()
            {
                return IntStream.rangeClosed( min, max )
                        .mapToObj( x -> LinearPoint.create( x, this ) );
            }

            @Override
            public Linear<Integer> subset( Integer min, Integer max )
            {
                return createLinear( min, max );
            }
        };
    }

    static Linear<Integer> createLinear( int min, int max, Function<Integer, Double> f )
    {
        if( min > max )
        {
            return createLinear( max, min, f );
        }
        return new Linear<Integer>()
        {
            @Override
            public Integer getMinimumX()
            {
                return min;
            }

            @Override
            public Integer getMaximumX()
            {
                return max;
            }

            @Override
            public double getValue( Integer x )
            {
                if( x < min || x > max )
                {
                    throw new IndexOutOfBoundsException();
                }
                return f.apply( x );
            }

            @Override
            public void forEach( BiConsumer<Integer, Double> action )
            {
                LinearSupport.forEach( min, max, x -> action.accept( x, f.apply( x ) ) );
            }

            @Override
            public Stream<LinearPoint<Integer>> stream()
            {
                return IntStream.rangeClosed( min, max )
                        .mapToObj( x -> LinearPoint.create( x, f, this ) );
            }

            @Override
            public Linear<Integer> subset( Integer subMin, Integer subMax )
            {
                if( subMin > subMax )
                {
                    return subset( subMax, subMin );
                }
                if( subMin < min || subMax > max )
                {
                    throw new IndexOutOfBoundsException();
                }
                return createLinear( subMin, subMax, x -> f.apply( x + subMin ) );
            }
        };
    }

    static Linear<Double> createLinear( double min, double max, double s, Function<Double, Double> f )
    {
        if( min > max )
        {
            return createLinear( max, min, s, f );
        }

        int c = (int) ((max - min) / s);

        return new Linear<Double>()
        {
            @Override
            public Double getMinimumX()
            {
                return min;
            }

            @Override
            public Double getMaximumX()
            {
                return max;
            }

            @Override
            public double getValue( Double x )
            {
                if( x < min || x > max )
                {
                    throw new IndexOutOfBoundsException();
                }

                return f.apply( x );
            }

            @Override
            public void forEach( BiConsumer<Double, Double> action )
            {
                LinearSupport.forEach( min, max, s, x -> action.accept( x, getValue( x ) ) );
            }

            @Override
            public Stream<LinearPoint<Double>> stream()
            {
                return IntStream.rangeClosed( 0, c )
                        .mapToDouble( x -> min + (x * s) )
                        .mapToObj( x -> LinearPoint.create( x, f, this ) );
            }

            @Override
            public Linear<Double> subset( Double subMin, Double subMax )
            {
                if( subMin > subMax )
                {
                    return subset( subMax, subMin );
                }
                return createLinear( subMin, subMax, s, x -> getValue( x + subMin ) );
            }
        };
    }

}
