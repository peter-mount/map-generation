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
package onl.area51.mapgen.gis;

import java.awt.geom.Rectangle2D;
import uk.trainwatch.gis.Bounds;
import uk.trainwatch.gis.Coordinate;

/**
 * A Tile reference
 * <p>
 * @author peter
 */
public interface TileReference
{

    int getZ();

    int getX();

    int getY();

    default double getDecimalX()
    {
        return (double) getX();
    }

    default double getDecimalY()
    {
        return (double) getY();
    }

    static TileReference of( int z, int x, int y )
    {
        return new BasicTileReference( z, x, y );
    }

    static TileReference of( int z, double x, double y )
    {
        return new BasicTileReference( z, x, y );
    }

    static TileReference fromCoordinate( int z, Coordinate point )
    {
        double φ = Math.toRadians( point.getLatitude() );
        double m = 1 << z;
        return new BasicTileReference(
                z,
                Math.max( 0, Math.min( m - 1, (point.getLongitude() + 180.0) / 360.0 * m ) ),
                Math.max( 0, Math.min( m - 1, (1.0 - Math.log( Math.tan( φ ) + 1.0 / Math.cos( φ ) ) / Math.PI) / 2.0 * m ) )
        );
    }

    default Coordinate toCoordinate()
    {
        return toCoordinate( getZ(), getX(), getY() );
    }

    static Coordinate toCoordinate( int z, int x, int y )
    {
        return toCoordinate( z, (double) x, (double) y );
    }

    static Coordinate toCoordinate( int z, double x, double y )
    {
        return Coordinate.of( x / Math.pow( 2.0, z ) * 360.0 - 180.0,
                              Math.toDegrees( Math.atan( Math.sinh( Math.PI - (2.0 * Math.PI * y) / Math.pow( 2.0, z ) ) ) ) );
    }

    default Coordinate getTopLeftCoordinate()
    {
        return toCoordinate( getZ(), getX(), getY() );
    }

    default Coordinate getTopRightCoordinate()
    {
        return toCoordinate( getZ(), getX() + 1, getY() );
    }

    default Coordinate getBottomLeftCoordinate()
    {
        return toCoordinate( getZ(), getX(), getY() + 1 );
    }

    default Coordinate getBottomRightCoordinate()
    {
        return toCoordinate( getZ(), getX() + 1, getY() + 1 );
    }

    default Bounds<Coordinate> getCoordinateBounds()
    {
        return Bounds.of( getTopLeftCoordinate(),
                          getTopRightCoordinate(),
                          getBottomLeftCoordinate(),
                          getBottomRightCoordinate() );
    }

    /**
     * Is this TileReference contained by another one
     * <p>
     * @param t
     *          <p>
     * @return
     */
    default boolean contains( TileReference t )
    {
        if( getZ() != t.getZ() ) {
            return false;
        }
        double x0 = t.getDecimalX(), y0 = t.getDecimalY();
        double x1 = getDecimalX(), y1 = getDecimalY();
        return x1 <= x0 && x0 < (x1 + 1.0) && y1 <= y0 && y0 < (y1 + 1.0);
    }

    default int getPx( double left )
    {
        return (int) ((getDecimalX() - left) * 256);
    }

    default int getPy( double top )
    {
        return (int) ((getDecimalY() - top) * 256);
    }

}
