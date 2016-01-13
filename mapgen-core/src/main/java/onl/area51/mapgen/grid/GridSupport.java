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
package onl.area51.mapgen.grid;

import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Provides utilities to handle grid based data
 *
 * @author peter
 */
public class GridSupport
{

    /**
     * Returns a Stream of {@link GridPoint} of all values within a grid
     *
     * @param z  Zoom level, used only within the returned object
     * @param ys Start of y range
     * @param ye End (exclusive) of y range
     * @param xs start of x range
     * @param xe end (exclusive) of x range
     *
     * @return Stream
     */
    public static Stream<GridPoint> stream( int z, int ys, int ye, int xs, int xe )
    {
        return IntStream.range( ys, ye )
                .boxed()
                .flatMap( y -> IntStream.rangeClosed(xs, xe )
                        .mapToObj( x -> GridPoint.of( z, x, y ) )
                );
    }

    /**
     * Returns a Stream of {@link GridPoint} of all values within a grid.
     * Unlike {@link #stream(int, int, int, int, int) } this allows for filtering
     * against both axes.
     *
     * @param z  Zoom level, used only within the returned object
     * @param ys Start of y range
     * @param ye End (exclusive) of y range
     * @param yf IntPredicate to filter against Y axis
     * @param xs start of x range
     * @param xe end (exclusive) of x range
     * @param xf IntPredicate to filter against X axis
     *
     * @return Stream
     */
    public static Stream<GridPoint> stream( int z,
                                            int ys, int ye,
                                            IntPredicate yf,
                                            int xs, int xe,
                                            IntPredicate xf )
    {
        return IntStream.range( ys, ye )
                .filter( yf )
                .boxed()
                .flatMap( y -> IntStream.rangeClosed(xs, xe )
                        .filter( xf )
                        .mapToObj( x -> GridPoint.of( z, x, y ) )
                );
    }

    /**
     * Returns an IntPredicate to test value is between two points
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static IntPredicate between( int a, int b )
    {
        if( a > b ) {
            return between( b, a );
        }
        return v -> v >= a && v < b;
    }

    /**
     * Returns a DoublePredicate to test a value is between two points
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static DoublePredicate between( double a, double b )
    {
        if( a > b ) {
            return between( b, a );
        }
        return v -> v >= a && v < b;
    }

    /**
     * Returns an IntPredicate to test value is between two points
     *
     * @param a      lower bound
     * @param b      upper bound (exclusive)
     * @param mapper mapping function to map integer to double
     *
     * @return predicate
     */
    public static IntPredicate between( double a, double b, Function<Integer, Double> mapper )
    {
        // Don't put call in the lambda otherwise we'll create a new one every call
        final DoublePredicate p = between( a, b );
        return v -> p.test( mapper.apply( v ) );
    }

    /**
     * Similar to {@link #between(double, double)} but applies a correction to handle values that cross the Greenwich meridian.
     * <p>
     * For example, if a is negative then this is a composition which checks for values 0..b and (360-a)..360. Otherwise it's the same.
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static DoublePredicate betweenPosition( double a, double b )
    {
        DoublePredicate predicate;
        
        if( a > b ) {
            // Handle wrapping around the Greenwich meridian
            predicate = between( a, 360.0 ).or( between( 0.0, b ) );
        }
        else {
            predicate = between( a, b );
        }

        return v -> v < 0.0 ? predicate.test( v + 360.0 ) : predicate.test( v );
    }

    /**
     * A wrapper around {@link #betweenPosition(double, double)} but returns an {@link IntPredicate} and uses a mapping function to convert the value to a
     * Double first.
     *
     * @param a
     * @param b
     * @param mapper Mapping function to convert an integer to a double.
     *
     * @return
     */
    public static IntPredicate betweenPosition( double a, double b, Function<Integer, Double> mapper )
    {
        // Don't put call in the lambda otherwise we'll create a new one every call
        final DoublePredicate p = betweenPosition( a, b );
        return v -> p.test( mapper.apply( v ) );
    }

}
