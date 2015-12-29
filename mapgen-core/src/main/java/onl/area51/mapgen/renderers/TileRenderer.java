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
package onl.area51.mapgen.renderers;

import java.awt.Color;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import onl.area51.mapgen.renderer.Renderer;
import onl.area51.mapgen.tilecache.MapTileServer;
import onl.area51.mapgen.tilecache.Tile;
import onl.area51.mapgen.tilecache.TileCache;

/**
 * Renderer to handle rendering of tiles
 * <p>
 * @author peter
 */
public class TileRenderer
        extends GridRenderer
        implements Consumer<Renderer>
{

    private final Consumer<Tile> tileLoaded;
    private final BiConsumer<Tile, Exception> tileError;
    private final MapTileServer server;

    public TileRenderer( MapTileServer server )
    {
        this( server,
              t -> {
              },
              ( t, e ) -> {
              } );
    }

    /**
     *
     * @param tileLoaded Consumer to accept a tile if it's been updated in the cache or an error occurred
     */
    public TileRenderer( MapTileServer server, Consumer<Tile> tileLoaded )
    {
        this( server, tileLoaded, ( t, e ) -> tileLoaded.accept( t ) );
    }

    /**
     *
     * @param tileLoaded Consumer to accept a tile if it's been updated in the cache
     * @param tileError  Consumer to accept a tile if an error occurred in the cache
     */
    public TileRenderer( MapTileServer server, Consumer<Tile> tileLoaded, BiConsumer<Tile, Exception> tileError )
    {
        super( true, Color.RED );
        this.tileLoaded = tileLoaded;
        this.tileError = tileError;
        this.server = server;
    }

    private Tile getTile( Renderer r )
    {
        if( r.getImageObserver() == null ) {
            try {
                return TileCache.INSTANCE.getTileSync( server, r.getZoom(), r.getX(), r.getY() );
            }
            catch( InterruptedException |
                   TimeoutException ex ) {
                Logger.getLogger( TileRenderer.class.getName() ).log( Level.SEVERE, null, ex );
                throw new RuntimeException( "Failed to get tile" + server + " " + r.getZoom() + " " + r.getX() + "," + r.getY(), ex );
            }
        }
        else {
            return TileCache.INSTANCE.getTile( server, r.getZoom(), r.getX(), r.getY(), tileLoaded, tileError );
        }
    }

    @Override
    public void accept( Renderer r )
    {
        //Tile tile = TileCache.INSTANCE.getTile( server, r.getZoom(), r.getX(), r.getY(), tileLoaded, tileError );
        Tile tile = getTile( r );
        if( tile != null && tile.isImagePresent() ) {
            r.drawImage( tile.getImage() );
        }
        else {
            super.accept( r );
        }
    }

}
