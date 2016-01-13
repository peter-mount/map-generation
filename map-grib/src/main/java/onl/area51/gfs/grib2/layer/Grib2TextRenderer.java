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

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.gfs.grib2.Grib2MetaData;
import onl.area51.mapgen.gis.TileReference;
import onl.area51.mapgen.grid.GridReferencedDataPoint;
import onl.area51.mapgen.renderer.Renderer;
import ucar.grib.grib2.Grib2Record;
import uk.trainwatch.gis.Coordinate;

/**
 * A renderer that draws text onto a map from GribData
 *
 * @author peter
 */
public class Grib2TextRenderer
        extends AbstractGrib2Renderer
{

    private final Color colour;

    public Grib2TextRenderer( Grib2MetaData meta, Grib2 file, Grib2Record record )
            throws IOException
    {
        this( meta, file, record, Color.BLACK );
    }

    public Grib2TextRenderer( Grib2MetaData meta, Grib2 file, Grib2Record record, Color colour )
            throws IOException
    {
        super( meta, file, record );
        this.colour = colour;
    }

    @Override
    protected void plot( Renderer r, TileReference tr, GridReferencedDataPoint p )
    {
        TileReference t = TileReference.fromCoordinate( tr.getZ(), Coordinate.of( p.getΛ(), p.getΦ() ) );

        int x = r.getXp() + t.getPx( r.getX() ) - 3;
        int y = r.getYp() + t.getPy( r.getY() ) - 3;

        Graphics2D g = r.getGraphics2D();
        r.setColor( colour );
        g.drawString( Integer.toString( (int) Math.floor( p.getValue() ) ), x, y );
    }
}
