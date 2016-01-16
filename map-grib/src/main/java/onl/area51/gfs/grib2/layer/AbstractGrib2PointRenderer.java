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

import java.io.IOException;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.gfs.grib2.Grib2MetaData;
import onl.area51.mapgen.renderer.Renderer;
import ucar.grib.grib2.Grib2Record;
import onl.area51.mapgen.grid.GridReferencedDataPoint;
import onl.area51.mapgen.grid.Grid;

/**
 *
 * @author peter
 */
public abstract class AbstractGrib2PointRenderer
        extends AbstractGrib2Renderer
{

    public AbstractGrib2PointRenderer( Grib2MetaData meta, Grib2 file, Grib2Record record )
            throws IOException
    {
        super( meta, file, record );
    }

    public AbstractGrib2PointRenderer( Grib2MetaData meta, Grid data )
            throws IOException
    {
        super( meta, data );
    }

    @Override
    public void accept( Renderer r )
    {
        // For now trust the outer caller to be calling us correctly as this only works when longitude is positive.
        //RectangleTileReference tileBounds = RectangleTileReference.fromRectangle( tr.getZ(), getMeta().getBounds() );
        //if( tileBounds.contains( tr ) ) {

        stream( r )
                .map( p -> GridReferencedDataPoint.of( p, this::getCoordinate ) )
                .forEach( p -> plot( r, p ) );

        //}
    }

    protected abstract void plot( Renderer r, GridReferencedDataPoint p );

}
