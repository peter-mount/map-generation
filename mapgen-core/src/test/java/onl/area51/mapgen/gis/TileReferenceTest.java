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

import org.junit.Test;
import static org.junit.Assert.*;
import uk.trainwatch.gis.Coordinate;

/**
 *
 * @author peter
 */
public class TileReferenceTest
{

    // Brandenburg Gate, Berlin
    private static final double LONG = 13.37771496361961;
    private static final double LAT = 52.51628011262304;
    private static final int ZOOM = 17;
    private static final int TILE_X = 70406;
    private static final int TILE_Y = 42987;
    private static final double TILE_Xd = 70406.67737697653;
    private static final double TILE_Yd = 42987.96455135485;
    private static final double delta = 0.01;

    @Test
    public void of()
    {
        TileReference r = TileReference.of( ZOOM, TILE_X, TILE_Y );
        assertEquals( ZOOM, r.getZ() );
        assertEquals( TILE_X, r.getX() );
        assertEquals( TILE_Y, r.getY() );
    }

    @Test
    public void fromCoordinate()
    {
        Coordinate c = Coordinate.of( LONG, LAT );

        TileReference r = TileReference.fromCoordinate( ZOOM, c );
        assertEquals( ZOOM, r.getZ() );
        assertEquals( TILE_X, r.getX() );
        assertEquals( TILE_Y, r.getY() );
        assertEquals( TILE_Xd, r.getDecimalX(), delta );
        assertEquals( TILE_Yd, r.getDecimalY(), delta );
    }

    @Test
    public void toCoordinate()
    {
        TileReference r = TileReference.of( ZOOM, TILE_X, TILE_Y );
        Coordinate c = r.toCoordinate();
        assertEquals( LONG, c.getLongitude(), delta );
        assertEquals( LAT, c.getLatitude(), delta );
    }

    @Test
    public void contains()
    {
        // Tile containing most of south & south west England.
        final TileReference tile = TileReference.of( 6, 31, 21 );

        // Southampton
        TileReference southampton = TileReference.fromCoordinate( 6, Coordinate.of( -1.4, 50.9 ) );

        assertTrue( "Tile should contain point",tile.contains( southampton ) );
    }
}
