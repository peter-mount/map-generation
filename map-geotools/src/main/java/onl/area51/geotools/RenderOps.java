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
package onl.area51.geotools;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.function.Consumer;
import onl.area51.geotools.render.MapRenderer;
import onl.area51.mapgen.util.ImageType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import uk.trainwatch.job.lang.Statement;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 *
 * @author peter
 */
public class RenderOps
{

    /**
     * Returns the image bounds that will contain the specified map based on the supplied width.
     *
     * @param map
     * @param width
     *
     * @return
     */
    public static Rectangle getMapBounds( MapContent map, int width )
    {
        ReferencedEnvelope mapBounds = map.getViewport().getBounds();
        double heightToWidth = mapBounds.getSpan( 1 ) / mapBounds.getSpan( 0 );
        return new Rectangle( 0, 0, width, (int) Math.round( width * heightToWidth ) );
    }

    public static ExpressionOperation getMapBounds( ExpressionOperation exp[] )
    {
        if( exp.length == 2 ) {
            return ( s, a ) -> {
                MapContent map = exp[0].get( s );
                int width = exp[1].getInt( s );
                return getMapBounds( map, width );
            };
        }
        return null;
    }

    public static ExpressionOperation createMapImage( ExpressionOperation exp[] )
    {
        switch( exp.length ) {
            case 2:
                return ( s, a ) -> {
                    MapContent map = exp[0].get( s );
                    int width = exp[1].getInt( s );
                    return ImageType.INT_RGB.create( getMapBounds( map, width ) );
                };

            case 3:
                return ( s, a ) -> {
                    MapContent map = exp[0].get( s );
                    String type = exp[1].getString( s );
                    int width = exp[2].getInt( s );
                    return ImageType.lookup( type ).create( getMapBounds( map, width ) );
                };

            default:
                return null;
        }
    }

    public static Statement renderMap( ExpressionOperation args[] )
    {
        // Must have 2 args, map & output
        if( args == null || args.length < 2 ) {
            return null;
        }

        return ( s, a ) -> MapRenderer.builder()
                .invokeAll( s, args )
                .build()
                .render();
    }

}
