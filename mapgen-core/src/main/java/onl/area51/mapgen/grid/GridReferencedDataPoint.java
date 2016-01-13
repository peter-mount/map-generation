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

import java.util.function.Function;
import onl.area51.mapgen.gis.TileReference;
import uk.trainwatch.gis.Coordinate;

/**
 * A point within a grid with both a value and the ability to get the original geographical position
 *
 * @author peter
 */
public interface GridReferencedDataPoint
        extends GridDataPoint
{

    /**
     * The longitude
     *
     * @return longitude
     */
    default double getΛ()
    {
        return getCoordinate().getLongitude();
    }

    /**
     * The latitude
     *
     * @return latitude
     */
    default double getΦ()
    {
        return getCoordinate().getLatitude();
    }

    /**
     * The coordinate
     *
     * @return coordinate
     */
    Coordinate getCoordinate();

    /**
     * The tile reference
     *
     * @return
     */
    TileReference getTileReference();

    /**
     *
     * @param p GridPoint
     * @param v value at this point
     * @param c mapping function to map GridPoint to Coordinate
     *
     * @return
     */
    static GridReferencedDataPoint of( GridPoint p, double v, Function<GridPoint, Coordinate> c )
    {
        return of( GridDataPoint.of( p, v ), c );
    }

    /**
     *
     * @param z zoom
     * @param x x
     * @param y y
     * @param v value at this point
     * @param c mapping function to map GridPoint to Coordinate
     *
     * @return
     */
    static GridPoint of( int z, int x, int y, double v, Function<GridPoint, Coordinate> c )
    {
        return of( GridDataPoint.of( z, x, y, v ), c );
    }

    /**
     *
     * @param p GridPoint
     * @param c mapping function to map GridPoint to Coordinate
     *
     * @return
     */
    static GridReferencedDataPoint of( GridDataPoint p, Function<GridPoint, Coordinate> c )
    {
        return new GridReferencedDataPoint()
        {
            private volatile Coordinate coord;
            private volatile TileReference tr;

            @Override
            public double getValue()
            {
                return p.getValue();
            }

            @Override
            public int getZ()
            {
                return p.getZ();
            }

            @Override
            public int getX()
            {
                return p.getX();
            }

            @Override
            public int getY()
            {
                return p.getY();
            }

            @Override
            public Coordinate getCoordinate()
            {
                if( coord == null ) {
                    coord = c.apply( this );
                }
                return coord;
            }

            /**
             * The tile reference
             *
             * @return
             */
            @Override
            public TileReference getTileReference()
            {
                if( tr == null ) {
                    tr = TileReference.fromCoordinate( getZ(), getCoordinate() );
                }
                return tr;
            }
        };
    }

    /**
     * Returns an instance using a mapping function to get the value.
     * <p>
     * Note: The value is retrieved when you call this method. This is usually more efficient, but if you want it to perform the lookup every time
     * {@link #getValue() } is called then use the
     * {@link #ofDynamic(onl.area51.mapgen.grid.GridPoint, java.util.function.Function, java.util.function.Function)} method instead.
     *
     * @param p GridPoint
     * @param v mapping function to map GridPoint to value
     * @param c mapping function to map GridPoint to Coordinate
     *
     * @return
     *
     * @see #ofDynamic(onl.area51.mapgen.grid.GridPoint, java.util.function.Function, java.util.function.Function)
     */
    static GridReferencedDataPoint of( GridPoint p, Function<GridPoint, Double> v, Function<GridPoint, Coordinate> c )
    {
        return of( p, v.apply( p ), c );
    }

    /**
     * Returns an instance where each call to {@link #getValue()} will call the value function every time.
     *
     * @param p GridPoint
     * @param v mapping function to map GridPoint to value
     * @param c mapping function to map GridPoint to Coordinate
     *
     * @return
     *
     * @see #of(onl.area51.mapgen.grid.GridPoint, java.util.function.Function, java.util.function.Function)
     */
    static GridReferencedDataPoint ofDynamic( GridPoint p, Function<GridPoint, Double> v, Function<GridPoint, Coordinate> c )
    {
        return new GridReferencedDataPoint()
        {
            private volatile Coordinate coord;
            private volatile TileReference tr;

            @Override
            public double getValue()
            {
                return v.apply( this );
            }

            @Override
            public int getZ()
            {
                return p.getZ();
            }

            @Override
            public int getX()
            {
                return p.getX();
            }

            @Override
            public int getY()
            {
                return p.getY();
            }

            @Override
            public Coordinate getCoordinate()
            {
                if( coord == null ) {
                    coord = c.apply( this );
                }
                return coord;
            }

            /**
             * The tile reference
             *
             * @return
             */
            @Override
            public TileReference getTileReference()
            {
                if( tr == null ) {
                    tr = TileReference.fromCoordinate( getZ(), getCoordinate() );
                }
                return tr;
            }
        };
    }

}
