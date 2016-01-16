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

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntPredicate;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A Grid consisting of a set of rows and columns which can be traversed
 *
 * @author peter
 */
public interface Grid
{

    /**
     * The number of columns in this grid
     *
     * @return
     */
    int getWidth();

    /**
     * The number of rows in this grid
     *
     * @return
     */
    int getHeight();

    /**
     * The value at a specified point
     *
     * @param x
     * @param y
     *
     * @return
     */
    default double getValue( int x, int y )
    {
        throw new UnsupportedOperationException();
    }

    /**
     * The value at a specified point
     *
     * @param p
     *
     * @return
     */
    default double getValue( GridPoint p )
    {
        return Grid.this.getValue( p.getX(), p.getY() );
    }

    /**
     * A stream of GridPoints
     *
     * @return
     */
    default Stream<GridPoint> stream()
    {
        return Grid.this.stream( 0 );
    }

    /**
     * A stream of GridPoints
     *
     * @param z zoom
     *
     * @return
     */
    default Stream<GridPoint> stream( int z )
    {
        return GridSupport.stream( z, 0, getHeight(), 0, getWidth() );
    }

    /**
     * A stream of GridPoints
     *
     * @param x filter of x axis
     * @param y filter of x axis
     *
     * @return y filter of y axis
     */
    default Stream<GridPoint> stream( IntPredicate x, IntPredicate y )
    {
        return stream( 0, x, y );
    }

    /**
     * A stream of GridPoints
     *
     * @param z zoom
     * @param x filter of x axis
     * @param y filter of x axis
     *
     * @return y filter of y axis
     */
    default Stream<GridPoint> stream( int z, IntPredicate x, IntPredicate y )
    {
        return GridSupport.stream( z, 0, getHeight(), y, 0, getWidth(), x );
    }

    /**
     * A stream of GridDataPoints
     *
     * @return
     */
    default Stream<GridDataPoint> dataStream()
    {
        return Grid.this.dataStream( 0 );
    }

    /**
     * A stream of GridDataPoints
     *
     * @param z zoom
     *
     * @return
     */
    default Stream<GridDataPoint> dataStream( int z )
    {
        return Grid.this.stream( z ).map( p -> GridDataPoint.of( p, getValue( p ) ) );
    }

    /**
     * A stream of GridDataPoints
     *
     * @param x filter of x axis
     * @param y filter of x axis
     *
     * @return y filter of y axis
     */
    default Stream<GridDataPoint> dataStream( IntPredicate x, IntPredicate y )
    {
        return dataStream( 0, x, y );
    }

    /**
     * A stream of GridDataPoints
     *
     * @param z zoom
     * @param x filter of x axis
     * @param y filter of x axis
     *
     * @return y filter of y axis
     */
    default Stream<GridDataPoint> dataStream( int z, IntPredicate x, IntPredicate y )
    {
        return GridSupport.stream( z, 0, getHeight(), y, 0, getWidth(), x ).map( p -> GridDataPoint.of( p, getValue( p ) ) );
    }

    /**
     * Iterate through each location within this grid
     *
     * @param action
     */
    default void forEach( BiConsumer<Integer, Integer> action )
    {
        GridSupport.forEach( 0, 0, getWidth(), getHeight(), action );
    }

    /**
     * Iterate through each location within this grid
     *
     * @param yf     IntPredicate to filter against Y axis
     * @param xf     IntPredicate to filter against X axis
     * @param action
     *
     */
    default void forEach( IntPredicate yf, IntPredicate xf, BiConsumer<Integer, Integer> action )
    {
        GridSupport.forEach( 0, 0, getWidth(), getHeight(), yf, xf, action );
    }

    /**
     * Return a subset of this grid
     *
     * @param l left of subset
     * @param t top of subset
     * @param w width
     * @param h height
     *
     * @return subset
     *
     * @throws IllegalArgumentException if the subset is not fully contained within this grid
     */
    default Grid subset( int l, int t, int w, int h )
    {
        if( w < 1 || h < 1 ) {
            throw new IllegalArgumentException( "Grid dimensions must be >=1" );
        }
        if( l < 0 || t < 0 || (l + w) > getWidth() || (t + h) > getHeight() ) {
            throw new IllegalArgumentException( "Grid subset is not contained within this grid" );
        }
        Grid parent = this;
        return new Grid()
        {
            @Override
            public int getWidth()
            {
                return w;
            }

            @Override
            public int getHeight()
            {
                return h;
            }

            @Override
            public double getValue( int x, int y )
            {
                return parent.getValue( l + x, t + y );
            }

        };
    }

    /**
     * Create a Grid with no data
     *
     * @param w width
     * @param h height
     *
     * @return Grid
     *
     * @throws IllegalArgumentException if width or height is less than 1
     */
    static Grid of( int w, int h )
    {
        if( w < 1 || h < 1 ) {
            throw new IllegalArgumentException( "Grid dimensions must be >=1" );
        }
        return new Grid()
        {
            @Override
            public int getWidth()
            {
                return w;
            }

            @Override
            public int getHeight()
            {
                return h;
            }

        };
    }

    /**
     * Create a Grid with data supplied by a function
     *
     * @param f function to supply value at a location
     * @param w width
     * @param h height
     *
     * @return Grid
     *
     * @throws IllegalArgumentException if width or height is less than 1
     */
    static Grid of( ToDoubleBiFunction<Integer, Integer> f, int w, int h )
    {
        Objects.requireNonNull( f );
        if( w < 1 || h < 1 ) {
            throw new IllegalArgumentException( "Grid dimensions must be >=1" );
        }
        return new Grid()
        {

            @Override
            public int getWidth()
            {
                return w;
            }

            @Override
            public int getHeight()
            {
                return h;
            }

            @Override
            public double getValue( int x, int y )
            {
                return f.applyAsDouble( x, y );
            }

        };
    }

    /**
     * Creates a Grid with data from a single dimensioned array.
     *
     * @param a array
     * @param w width
     * @param h height
     *
     * @return Grid
     *
     * @throws IllegalArgumentException if (w*h) is greater than the length of the array or if w or h is less than 1
     */
    static Grid of( double[] a, int w, int h )
    {
        Objects.requireNonNull( a );
        if( w < 1 || h < 1 ) {
            throw new IllegalArgumentException( "Grid dimensions must be >=1" );
        }
        if( (w * h) > a.length ) {
            throw new IllegalArgumentException( "Grid is larger than array size" );
        }
        return new Grid()
        {

            @Override
            public int getWidth()
            {
                return w;
            }

            @Override
            public int getHeight()
            {
                return h;
            }

            @Override
            public double getValue( int x, int y )
            {
                int i = (y * w) + x;
                if( i < 0 || i >= a.length ) {
                    throw new ArrayIndexOutOfBoundsException( "Index " + i + " out of bounds 0<=i<" + a.length );
                }
                return a[i];
            }

        };
    }

    /**
     * Creates a Grid based on a double dimension array.
     * <p>
     * The height is the length of the first dimension whilst the width is the maximum length of all rows.
     * <p>
     * When retrieving a value then if the point is out of bounds (row is null or shorter than x) then NaN is returned.
     *
     * @param a Array
     *
     * @return Grid
     *
     * @throws IllegalArgumentException if the array is empty or if either of the width or height would be less than 1
     */
    static Grid of( double[][] a )
    {
        Objects.requireNonNull( a );
        if( a.length == 0 ) {
            throw new IllegalArgumentException( "Cannot create a Grid of zero size" );
        }
        int h = a.length;
        int w = IntStream.range( 0, a.length ).map( i -> a[i].length ).max().getAsInt();
        if( w < 1 || h < 1 ) {
            throw new IllegalArgumentException( "Grid dimensions must be >=1" );
        }

        return new Grid()
        {

            @Override
            public int getWidth()
            {
                return w;
            }

            @Override
            public int getHeight()
            {
                return h;
            }

            @Override
            public double getValue( int x, int y )
            {
                if( y < 0 || y >= a.length ) {
                    throw new ArrayIndexOutOfBoundsException( "Index " + y + " out of bounds 0<=y<" + a.length );
                }
                double r[] = a[y];
                if( r == null || r.length <= x ) {
                    return Double.NaN;
                }
                return r[x];
            }

        };
    }

    /**
     * Creates a Grid with data from a single dimensioned array.
     *
     * @param a array
     * @param w width
     * @param h height
     *
     * @return Grid
     *
     * @throws IllegalArgumentException if (w*h) is greater than the length of the array or if w or h is less than 1
     */
    static Grid of( float[] a, int w, int h )
    {
        Objects.requireNonNull( a );
        if( w < 1 || h < 1 ) {
            throw new IllegalArgumentException( "Grid dimensions must be >=1" );
        }
        if( (w * h) > a.length ) {
            throw new IllegalArgumentException( "Grid is larger than array size" );
        }
        return new Grid()
        {

            @Override
            public int getWidth()
            {
                return w;
            }

            @Override
            public int getHeight()
            {
                return h;
            }

            @Override
            public double getValue( int x, int y )
            {
                int i = (y * w) + x;
                if( i < 0 || i >= a.length ) {
                    throw new ArrayIndexOutOfBoundsException( "Index " + i + " out of bounds 0<=i<" + a.length );
                }
                return a[i];
            }

        };
    }

    /**
     * Creates a Grid based on a double dimension array.
     * <p>
     * The height is the length of the first dimension whilst the width is the maximum length of all rows.
     * <p>
     * When retrieving a value then if the point is out of bounds (row is null or shorter than x) then NaN is returned.
     *
     * @param a Array
     *
     * @return Grid
     *
     * @throws IllegalArgumentException if the array is empty or if either of the width or height would be less than 1
     */
    static Grid of( float[][] a )
    {
        Objects.requireNonNull( a );
        if( a.length == 0 ) {
            throw new IllegalArgumentException( "Cannot create a Grid of zero size" );
        }
        int h = a.length;
        int w = IntStream.range( 0, a.length ).map( i -> a[i].length ).max().getAsInt();
        if( w < 1 || h < 1 ) {
            throw new IllegalArgumentException( "Grid dimensions must be >=1" );
        }

        return new Grid()
        {

            @Override
            public int getWidth()
            {
                return w;
            }

            @Override
            public int getHeight()
            {
                return h;
            }

            @Override
            public double getValue( int x, int y )
            {
                if( y < 0 || y >= a.length ) {
                    throw new ArrayIndexOutOfBoundsException( "Index " + y + " out of bounds 0<=y<" + a.length );
                }
                float r[] = a[y];
                if( r == null || r.length <= x ) {
                    return Float.NaN;
                }
                return r[x];
            }

        };
    }
}
