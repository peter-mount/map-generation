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
package onl.area51.gfs.grib2.layer;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.function.Consumer;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.gfs.grib2.Grib2MetaData;
import onl.area51.mapgen.grid.GridSupport;
import onl.area51.mapgen.grid.GridPoint;
import onl.area51.mapgen.gis.TileReference;
import onl.area51.mapgen.renderer.Renderer;
import ucar.grib.grib2.Grib2Record;
import uk.trainwatch.gis.Bounds;
import uk.trainwatch.gis.Coordinate;
import onl.area51.mapgen.grid.GridReferencedDataPoint;

/**
 *
 * @author peter
 */
public abstract class AbstractGrib2Renderer
        implements Consumer<Renderer>
{

    private final float[] data;
    private final Grib2MetaData meta;
    /**
     * Difference in longitude between columns
     */
    private final double Δλ;
    /**
     * Difference in latitude between rows
     */
    private final double Δφ;

    AbstractGrib2Renderer( Grib2MetaData meta, Grib2 file, Grib2Record record )
            throws IOException
    {
        this.meta = meta;
        data = file.getData( record );

        Rectangle2D bounds = meta.getBounds();
        Δλ = meta.getColumns() / (bounds.getX() + bounds.getWidth());
        Δφ = meta.getRows() / (bounds.getY() - bounds.getHeight());
    }

    protected final double getΔλ()
    {
        return Δλ;
    }

    protected final double getΔφ()
    {
        return Δφ;
    }

    private static double fixλ( double λ )
    {
        return λ >= 180.0 ? λ - 360.0 : λ;
    }

    private static double normaliseλ( double λ )
    {
        return λ < 0.0 ? λ + 360.0 : λ;
    }

    protected final double getλ( int x )
    {
        return normaliseλ( meta.getBounds().getX() + (x / Δλ) );
    }

    protected final double getφ( int y )
    {
        return meta.getBounds().getY() - (y / Δφ);
    }

    protected final float getVal( int x, int y )
    {
        return data[x + (y * meta.getColumns())];
    }

    protected final double getVal( GridPoint p )
    {
        return getVal( p.getX(), p.getY() );
    }

    protected final float[] getData()
    {
        return data;
    }

    protected final Grib2MetaData getMeta()
    {
        return meta;
    }

    @Override
    public void accept( Renderer r )
    {
        // For now trust the outer caller to be calling us correctly as this only works when longitude is positive.
        //RectangleTileReference tileBounds = RectangleTileReference.fromRectangle( tr.getZ(), getMeta().getBounds() );
        //if( tileBounds.contains( tr ) ) {

        // Get the relevant bounds
        TileReference tr = r.getTileReference();

        Bounds<Coordinate> cBounds = tr.getCoordinateBounds();
        final double φ1 = cBounds.getTopLeft().getLatitude();
        final double λ1 = normaliseλ( cBounds.getTopLeft().getLongitude() );
        final double φ2 = cBounds.getBottomRight().getLatitude();
        final double λ2 = normaliseλ( cBounds.getBottomRight().getLongitude() );

        GridSupport.stream( r.getZoom(),
                            0, meta.getRows(),
                            GridSupport.between( φ1, φ2, this::getφ ),
                            0, meta.getColumns(),
                            GridSupport.betweenPosition( λ1, λ2, this::getλ ) )
                .map( p -> GridReferencedDataPoint.of( p, this::getVal, this::getCoordinate ) )
                .forEach( p -> plot( r, tr, p ) );

        //}
    }

    private Coordinate getCoordinate( GridPoint p )
    {
        return Coordinate.of( getλ( p.getX() ), getφ( p.getY() ) );
    }

    protected abstract void plot( Renderer r, TileReference tr, GridReferencedDataPoint p );

}
