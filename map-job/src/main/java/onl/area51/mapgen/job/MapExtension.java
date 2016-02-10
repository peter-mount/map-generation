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
package onl.area51.mapgen.job;

import java.awt.geom.Rectangle2D;
import static onl.area51.mapgen.job.MiscOps.*;
import onl.area51.mapgen.renderer.RendererUtils;
import onl.area51.mapgen.util.ImageType;
import onl.area51.mapgen.layer.DefaultTiledLayer;
import onl.area51.mapgen.layer.LinkedLayers;
import onl.area51.mapgen.util.ColourType;
import onl.area51.mapgen.util.ImageUtils;
import onl.area51.mapgen.features.GridRenderer;
import onl.area51.mapgen.features.TileRenderer;

import onl.area51.mapgen.tilecache.MapTileServer;
import onl.area51.mapgen.tilecache.TileCache;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.job.ext.Extension;
import uk.trainwatch.job.lang.Statement;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 * Map Generation extensions to Job
 * <p>
 * @author peter
 */
@MetaInfServices(Extension.class)
public class MapExtension
        implements Extension
{

    @Override
    public String getName()
    {
        return "MapGeneration";
    }

    @Override
    public String getVersion()
    {
        return "1.0";
    }

    @Override
    public ExpressionOperation getExpression( String name, ExpressionOperation... args )
    {
        final int argc = args == null ? 0 : args.length;

        switch( argc ) {
            case 1:
                switch( name ) {
                    case "readImage":
                        return ( s, a ) -> ImageUtils.readImage( args[0].invoke( s, a ) );

                    default:
                        break;
                }
                break;

            case 2:
                switch( name ) {
                    // Image = createImage(w,h);
                    case "createImage":
                        return ( s, a ) -> ImageType.INT_RGB.create( args[0].getInt( s ), args[1].getInt( s ) );

                    default:
                        break;
                }
                break;

            case 3:
                switch( name ) {
                    // Image = createImage("type",w,h);
                    case "createImage":
                        return ( s, a ) -> ImageType.lookup( args[0].getString( s ) ).create( args[1].getInt( s ), args[2].getInt( s ) );

                    default:
                        break;
                }
                break;

            case 4:
                switch( name ) {

                    /**
                     * Retrieve a remote tile, caching it locally
                     * Tile = retrieveMapTile(server,zoom,x,y);
                     */
                    case "retrieveMapTile":
                        return ( s, a ) -> TileCache.INSTANCE.getTileSync(
                                MapTileServer.lookup( args[0].getString( s ) ),
                                args[1].getInt( s ),
                                args[2].getInt( s ),
                                args[3].getInt( s )
                        );

                    default:
                        break;
                }
                break;

            default:
                break;
        }

        return null;
    }

    @Override
    public Statement getStatement( String name, ExpressionOperation... args )
    {
        final int argc = args == null ? 0 : args.length;

        switch( argc ) {
            case 2:
                switch( name ) {

                    case "writeImage":
                        return ( s, a ) -> ImageUtils.writeImage( getImage( args[0], s ), args[1].invoke( s, a ) );

                    default:
                        break;
                }
                break;

            case 3:
                switch( name ) {
                    case "writeImage":
                        return ( s, a ) -> ImageUtils.writeImage( getImage( args[0], s ), args[1].invoke( s, a ), args[2].invoke( s, a ) );

                    default:
                        break;
                }
                break;

            case 5:
                switch( name ) {
                    // Image = renderMapImage(image,z,x,y,layers);
                    case "renderMap":
                        return ( s, a ) -> RendererUtils.render( args[0].get( s ),
                                                                 args[1].getInt( s ),
                                                                 args[2].getDouble( s ),
                                                                 args[3].getDouble( s ),
                                                                 args[4].get( s ) );

                    default:
                        break;
                }
                break;

            default:
                break;
        }

        return null;
    }

    @Override
    public ExpressionOperation construct( String type, ExpressionOperation... exp )
    {
        switch( exp.length ) {
            case 0:
                switch( type ) {

                    case "tileGridLayer":
                        return ( s, a ) -> new DefaultTiledLayer( "Tile grid", new GridRenderer( true ) ).setEnabled( true );

                    case "tileLayerSet":
                        return ( s, a ) -> new LinkedLayers();

                    default:
                        return null;
                }

            case 1:
                switch( type ) {
                    // Create a new base tile layer for a server
                    case "baseTileLayer":
                        return ( s, a ) -> {
                            MapTileServer server = MapTileServer.lookup( exp[0].getString( s ) );
                            return new DefaultTiledLayer( server.getTitle(), new TileRenderer( server ) ).setEnabled( true );
                        };

                    case "tileGridLayer":
                        return ( s, a ) -> new DefaultTiledLayer( "Tile grid", new GridRenderer( exp[0].isTrue( s ) ) ).setEnabled( true );

                    default:
                        return null;
                }

            case 2:
                switch( type ) {

                    // new rectangle(w,h)
                    case "rectangle":
                        return ( s, a ) -> new Rectangle2D.Double( 0.0, 0.0, exp[0].getDouble( s ), exp[1].getDouble( s ) );

                    case "tileGridLayer":
                        return ( s, a ) -> new DefaultTiledLayer( "Tile grid",
                                                                  new GridRenderer( exp[0].isTrue( s ),
                                                                                    ColourType.lookup( exp[1].getString( s ) ).getColor() ) )
                                .setEnabled( true );

                    default:
                        return null;
                }

            case 4:
                switch( type ) {
                    // new rectangle(x,y,w,h)
                    case "rectangle":
                        return ( s, a ) -> new Rectangle2D.Double( exp[0].getDouble( s ), exp[1].getDouble( s ), exp[2].getDouble( s ), exp[3].getDouble( s ) );
                }
            default:
                return null;
        }
    }

}
