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
package onl.area51.mapgen.util.tile;

import java.awt.geom.Rectangle2D;
import uk.trainwatch.gis.Coordinate;

/**
 *
 * @author peter
 */
public interface RectangleTileReference
        extends TileReference
{

    int getX2();

    int getY2();

    double getDecimalX2();

    double getDecimalY2();

    @Override
    default boolean contains( TileReference t )
    {
        if( getZ() != t.getZ() ) {
            return false;
        }
        double x0 = t.getDecimalX(), y0 = t.getDecimalY();
        double x1 = getDecimalX(), y1 = getDecimalY();
        double x2 = getDecimalX2(), y2 = getDecimalY2();

        return x1 <= x0 && x0 < x2 && y1 <= y0 && y0 < y2;
    }

    static double fix( double v )
    {
        double λ = v;
        while( λ < -180.0 ) {
            λ += 360.0;
        }
        while( λ >= 180.0 ) {
            λ -= 360.0;
        }
        return λ;
    }

    /**
     * Return a RectangleTileReference for the specified zoom and Rectangle2D.
     * <p>
     * Note: With the rectangle origin O = (r.x,r.y) then:
     * <ul>
     * <li>If O is bottom left then height & width must be positive</ul>
     * <li>If O is bottom right then height is positive & width negative</ul>
     * <li>If O is top left then height is negative & width positive</ul>
     * <li>If O is top right then height & width must be negative</ul>
     * </ul>
     *
     * @param z
     * @param r
     *
     * @return
     */
    static RectangleTileReference fromRectangle( int z, Rectangle2D r )
    {
        double φ1 = Math.max( r.getY(), r.getY() + r.getHeight() );
        double φ2 = Math.min( r.getY(), r.getY() + r.getHeight() );
        return fromRectangle( z, Coordinate.of( r.getX(), φ1 ), Coordinate.of( r.getX() + r.getWidth(), φ2 ) );
    }

    /**
     * Return a RectangleTileReference for the specified zoom, Coordinate and dimensions.
     * <p>
     * Note: With the rectangle origin O = (r.x,r.y) then:
     * <ul>
     * <li>If O is bottom left then height & width must be positive</ul>
     * <li>If O is bottom right then height is positive & width negative</ul>
     * <li>If O is top left then height is negative & width positive</ul>
     * <li>If O is top right then height & width must be negative</ul>
     * </ul>
     *
     * @param z
     * @param tl Coordinate of origin
     * @param w  width
     * @param h  height
     *
     * @return
     */
    static RectangleTileReference fromRectangle( int z, Coordinate tl, double w, double h )
    {
        return fromRectangle( z, tl, Coordinate.of( fix( tl.getLongitude() + w ), tl.getLatitude() - h ) );
    }

    /**
     * Return a RectangleTileReference for the specified zoom and two Coordinates forming opposing corners
     *
     * @param z
     * @param tl Coordinate of origin
     * @param br Coordinate of opposing corner
     *
     * @return
     */
    static RectangleTileReference fromRectangle( int z, Coordinate tl, Coordinate br )
    {
        double φ1 = Math.toRadians( tl.getLatitude() );
        double φ2 = Math.toRadians( br.getLatitude() );
        double λ1 = tl.getLongitude() + 180.0, λ2 = br.getLongitude() + 180.0;
        double m = 1 << z;
        return new BasicRectangleTileReference(
                z,
                Math.max( 0, Math.min( m - 1, λ1 / 360.0 * m ) ),
                Math.max( 0, Math.min( m - 1, (1.0 - Math.log( Math.tan( φ1 ) + 1.0 / Math.cos( φ1 ) ) / Math.PI) / 2.0 * m ) ),
                Math.max( 0, Math.min( m - 1, λ2 / 360.0 * m ) ),
                Math.max( 0, Math.min( m - 1, (1.0 - Math.log( Math.tan( φ2 ) + 1.0 / Math.cos( φ2 ) ) / Math.PI) / 2.0 * m ) )
        );
    }
}
