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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.stream.Stream;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.gfs.grib2.Grib2MetaData;
import onl.area51.mapgen.grid.GridSupport;
import onl.area51.mapgen.grid.GridPoint;
import onl.area51.mapgen.util.tile.TileReference;
import onl.area51.mapgen.grid.GridDataPoint;
import onl.area51.mapgen.renderer.Renderer;
import ucar.grib.grib2.Grib2Record;
import uk.trainwatch.gis.Bounds;
import uk.trainwatch.gis.Coordinate;
import onl.area51.mapgen.grid.Grid;

/**
 *
 * @author peter
 */
public abstract class AbstractGrib2Renderer
        implements Consumer<Renderer>
{

    private final Grid data;
    private final Grib2MetaData meta;
    /**
     * Difference in longitude between columns
     */
    private final double Δλ;
    /**
     * Difference in latitude between rows
     */
    private final double Δφ;

    public AbstractGrib2Renderer( Grib2MetaData meta, Grib2 file, Grib2Record record )
            throws IOException
    {
        this(meta, Grid.of( file.getDoubleData( record ), meta.getColumns(), meta.getRows() ) );
    }

    public AbstractGrib2Renderer( Grib2MetaData meta, Grid data )
            throws IOException
    {
        this.meta = meta;
        this.data = data;
        Rectangle2D bounds = meta.getBounds();

        // FIXME should be cols not cols-1 here but for grib data it fails.
        Δλ = (meta.getColumns() - 1) / (bounds.getX() + bounds.getWidth());
//        Δλ = meta.getColumns() / (bounds.getX() + bounds.getWidth());

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

    protected final double getλ( double x )
    {
        return normaliseλ( meta.getBounds().getX() + (x / Δλ) );
    }

    protected final double getφ( double y )
    {
        return meta.getBounds().getY() - (y / Δφ);
    }

    protected final double getVal( int x, int y )
    {
        return data.getValue( x, y );
    }

    protected final double getVal( GridPoint p )
    {
        return getVal( p.getX(), p.getY() );
    }

    public final Grid getData()
    {
        return data;
    }

    public final Grib2MetaData getMeta()
    {
        return meta;
    }

    protected IntPredicate getXPredicate( Renderer r )
    {
        TileReference tr = r.getTileReference();
        Bounds<Coordinate> cBounds = tr.getCoordinateBounds();
        final double λ1 = normaliseλ( cBounds.getTopLeft().getLongitude() );
        final double λ2 = normaliseλ( cBounds.getBottomRight().getLongitude() );
        return GridSupport.betweenPosition( λ1, λ2, this::getλ );
    }

    protected IntPredicate getYPredicate( Renderer r )
    {
        TileReference tr = r.getTileReference();
        Bounds<Coordinate> cBounds = tr.getCoordinateBounds();
        final double φ1 = cBounds.getTopLeft().getLatitude();
        final double φ2 = cBounds.getBottomRight().getLatitude();
        return GridSupport.between( φ1, φ2, this::getφ );
    }

    protected final Stream<GridDataPoint> stream( Renderer r )
    {
        return data.dataStream( r.getZoom(), getXPredicate( r ), getYPredicate( r ) );
    }

    protected final Coordinate getCoordinate( GridPoint p )
    {
        return Coordinate.of( getλ( p.getX() ), getφ( p.getY() ) );
    }

    protected final Coordinate getCoordinate( double x, double y )
    {
        return Coordinate.of( getλ( x ), getφ( y ) );
    }

    protected final Point2D getCoordinatePoint( double x, double y )
    {
        Coordinate c = getCoordinate( x, y );
        return new Point2D.Double( c.getLongitude(), c.getLatitude() );
    }

}
