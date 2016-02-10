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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Objects;
import onl.area51.mapgen.util.ColourType;
import onl.area51.mapgen.util.GraphicsUtils;
import static onl.area51.mapgen.util.GraphicsUtils.*;
import onl.area51.mapgen.util.ImageType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
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

    /**
     * Render a map onto an image.
     *
     * @param map   MapContent
     * @param image Image
     */
    public static void renderMap( MapContent map, BufferedImage image )
    {
        renderMap( map, image, getImageBounds( image ) );
    }

    /**
     * Render a map onto an image
     *
     * @param map         MapContent
     * @param image       Image
     * @param imageBounds Bounds within the image to render into
     */
    public static void renderMap( MapContent map, BufferedImage image, Rectangle imageBounds )
    {
        draw( image, g -> {
          ReferencedEnvelope mapBounds = map.getViewport().getBounds();

          GTRenderer renderer = new StreamingRenderer();
          renderer.setMapContent( map );
          renderer.paint( g, imageBounds, mapBounds );
      } );
    }

    public static Statement renderMap( ExpressionOperation args[] )
    {
        if( args == null || args.length < 2 ) {
            return null;
        }

        return ( s, a ) -> {
            BufferedImage image = null;
            MapContent map = null;

            Color fg = null, bg = null;
            Rectangle imageBounds = null;
            for( Object o: ExpressionOperation.invoke( args, s ) ) {
                if( o instanceof BufferedImage ) {
                    image = (BufferedImage) o;
                }
                else if( o instanceof MapContent ) {
                    map = (MapContent) o;
                }
                else if( o instanceof Color ) {
                    if( bg == null ) {
                        bg = (Color) o;
                    }
                    else {
                        bg = (Color) o;
                    }
                }
                else if( o instanceof Rectangle ) {
                    imageBounds = (Rectangle) o;
                }
                else {
                    ColourType ct = ColourType.lookup( o.toString() );
                    if( ct != null ) {
                        if( bg == null ) {
                            bg = ct.getColor();
                        }
                        else {
                            fg = ct.getColor();
                        }
                    }
                }
            }

            Objects.requireNonNull( image, "Image is requried" );
            Objects.requireNonNull( map, "MapContent is required" );

            if( imageBounds == null ) {
                imageBounds = GraphicsUtils.getImageBounds( image );
            }

            if( fg != null || bg != null ) {
                GraphicsUtils.clearImage( image, bg, fg, imageBounds );
            }

            renderMap( map, image );
        };
    }
}
