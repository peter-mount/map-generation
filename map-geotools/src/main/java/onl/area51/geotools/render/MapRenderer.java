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
package onl.area51.geotools.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;
import onl.area51.mapgen.util.ColourType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import uk.trainwatch.util.Consumers;
import static onl.area51.mapgen.util.GraphicsUtils.*;
import uk.trainwatch.job.Scope;
import uk.trainwatch.job.lang.expr.ExpressionOperation;
import uk.trainwatch.util.MapBuilder;

/**
 * Render a map
 *
 * @author peter
 */
public interface MapRenderer
{

    void render();

    /**
     * Builder of a MapRenderer
     */
    static interface Builder
    {

        /**
         * Render to an image
         *
         * @param img
         *
         * @return
         */
        Builder renderToImage( BufferedImage img );

        /**
         * Render to a file
         *
         * @param file
         *
         * @return
         */
        Builder renderToFile( File file );

        /**
         * Set the Map to render
         *
         * @param map
         *
         * @return
         */
        Builder mapContent( MapContent map );

        /**
         * Optional colour of map border
         *
         * @param c
         *
         * @return
         */
        Builder foreground( Color c );

        /**
         * Optional border colour based on a colour type
         *
         * @param c
         *
         * @return
         */
        default Builder foregroundType( ColourType c )
        {
            return c == null ? this : foreground( c.getColor() );
        }

        boolean isForegroundSet();

        /**
         * Optional background colour
         *
         * @param c
         *
         * @return
         */
        Builder background( Color c );

        /**
         * Optional background colour based on a colour type
         *
         * @param c
         *
         * @return
         */
        default Builder backgroundType( ColourType c )
        {
            return background( c.getColor() );
        }

        boolean isBackgroundSet();

        /**
         * The map bounds. For an image this will be the area inside the image the map will be rendered into
         *
         * @param r
         *
         * @return
         */
        Builder bounds( Rectangle r );

        boolean isBoundsSet();

        /**
         * The page/image size. Only used with output to file
         *
         * @param r
         *
         * @return
         */
        Builder pageSize( Rectangle r );

        boolean isPageSizeSet();

        /**
         * Build the final renderer
         *
         * @return
         */
        MapRenderer build();

        /**
         * Invoke all expressions
         *
         * @param s
         * @param exp
         *
         * @return
         *
         * @throws Exception
         */
        default Builder invokeAll( Scope s, ExpressionOperation... exp )
                throws Exception
        {
            for( ExpressionOperation ex: exp ) {
                invoke( ex, s );
            }
            return this;
        }

        /**
         * Invoke the supplied expression, adding thre result to the builder
         *
         * @param ex
         * @param s
         *
         * @return
         *
         * @throws Exception
         */
        default Builder invoke( ExpressionOperation ex, Scope s )
                throws Exception
        {
            Object o = ex.invoke( s );
            if( o instanceof BufferedImage ) {
                renderToImage( (BufferedImage) o );
            }
            else if( o instanceof MapContent ) {
                mapContent( (MapContent) o );
            }
            else if( o instanceof File ) {
                renderToFile( (File) o );
            }
            else if( o instanceof Color ) {
                if( !isBackgroundSet() ) {
                    background( (Color) o );
                }
                else if( !isForegroundSet() ) {
                    foreground( (Color) o );
                }
            }
            else if( o instanceof Rectangle ) {
                if( !isBoundsSet() ) {
                    bounds( (Rectangle) o );
                }
                else if( !isPageSizeSet() ) {
                    pageSize( (Rectangle) o );
                }
            }
            else {
                ColourType ct = ColourType.lookupOrNull( o.toString() );
                if( ct != null ) {
                    if( !isBackgroundSet() ) {
                        backgroundType( ct );
                    }
                    else if( !isForegroundSet() ) {
                        foregroundType( ct );
                    }
                }
                else {
                    renderToFile( new File( o.toString() ) );
                }
            }
            return this;
        }
    }

    static Builder builder()
    {
        return new Builder()
        {
            private BufferedImage img;
            private File file;
            private MapContent map;
            private Color fg;
            private Color bg;
            private Rectangle bounds;
            private Rectangle pageSize;

            private void assertOutput()
            {
                if( img != null ) {
                    throw new IllegalArgumentException( "Image output already defined" );
                }
                if( file != null ) {
                    throw new IllegalArgumentException( "File output already defined" );
                }
            }

            @Override
            public Builder renderToImage( BufferedImage img )
            {
                Objects.requireNonNull( img, "Image required" );
                assertOutput();
                this.img = img;
                return this;
            }

            @Override
            public Builder renderToFile( File file )
            {
                Objects.requireNonNull( "File required" );
                assertOutput();
                this.file = file;
                return this;
            }

            @Override
            public Builder mapContent( MapContent map )
            {
                if( this.map != null ) {
                    throw new IllegalArgumentException( "MapContent already defined" );
                }
                this.map = map;
                return this;
            }

            @Override
            public boolean isForegroundSet()
            {
                return fg != null;
            }

            @Override
            public Builder foreground( Color c )
            {
                this.fg = c;
                return this;
            }

            @Override
            public boolean isBackgroundSet()
            {
                return bg != null;
            }

            @Override
            public Builder background( Color c )
            {
                this.bg = c;
                return this;
            }

            @Override
            public Builder bounds( Rectangle r )
            {
                this.bounds = r;
                return this;
            }

            @Override
            public boolean isBoundsSet()
            {
                return bounds != null;
            }

            @Override
            public Builder pageSize( Rectangle r )
            {
                this.pageSize = r;
                return this;
            }

            @Override
            public boolean isPageSizeSet()
            {
                return pageSize != null;
            }

            @Override
            public MapRenderer build()
            {
                Objects.requireNonNull( map, "MapContext is required" );

                // Image bounds is optional for Images
                if( bounds == null && img != null ) {
                    bounds = getImageBounds( img );
                }

                Objects.requireNonNull( bounds, "Bounds is required" );

                // Build our rendering pipeline
                Consumer<Graphics2D> op = null;

                if( bg != null ) {
                    op = Consumers.andThen( op, g -> {
                                        g.setPaint( bg );
                                        g.fill( bounds );
                                    } );
                }
                if( fg != null ) {
                    op = Consumers.andThen( op, g -> {
                                        g.setPaint( fg );
                                        g.setStroke( new BasicStroke( 1 ) );
                                        g.draw( bounds );
                                    } );
                }

                // Finally render the map
                Consumer<Graphics2D> finalOp = Consumers.andThen(
                        op,
                        g -> {
                            ReferencedEnvelope mapBounds = map.getViewport().getBounds();

                            GTRenderer renderer = new StreamingRenderer();
                            renderer.setRendererHints( MapBuilder.builder()
                                    .add( StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, Boolean.TRUE )
                                    .add( StreamingRenderer.CONTINUOUS_MAP_WRAPPING, Boolean.TRUE )
                                    .build() );
                            System.out.println(renderer.getRendererHints());
                            renderer.setMapContent( map );
                            renderer.paint( g, bounds, mapBounds );
                        } );

                // Now the final renderer
                if( img != null ) {
                    return () -> draw( img, finalOp );
                }

                if( file != null ) {
                    return () -> RenderUtils.draw( file, pageSize == null ? bounds : pageSize, finalOp );
                }

                // If we get here we've left out the output type
                throw new IllegalArgumentException( "No valid output defined" );
            }

        };
    }
}
