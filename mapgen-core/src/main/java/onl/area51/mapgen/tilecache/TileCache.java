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
package onl.area51.mapgen.tilecache;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import onl.area51.mapgen.util.ImageUtils;
import uk.trainwatch.util.MapBuilder;

/**
 * A simple in memory/disk based cache used for map tiles
 * <p>
 * @author peter
 */
public enum TileCache
{

    INSTANCE;

    private static final Logger LOG = Logger.getLogger( TileCache.class.getName() );

    private final Map<MapTileServer, FileSystem> caches = new ConcurrentHashMap<>();

    private final Map<Long, Tile> tiles = new WeakHashMap<>();

    private TileCache()
    {
    }

    private synchronized Tile getCacheEntry( MapTileServer server, int zoom, int x, int y )
    {
        final long max = 1L << zoom;

        // Ensure we don't try to get tiles outside of the valid ranges
        if( x < 0 || x >= max || y < 0 || y >= max )
        {
            throw new IllegalArgumentException( "Tile (" + zoom + "," + x + "," + y + ") out of bounds " + max );
        }

        return tiles.computeIfAbsent( (((zoom * max) + x) * max + y) * 100 + server.ordinal(),
                                      index -> new Tile( index, server, zoom, x, y )
        );
    }

    public Tile getTileSync( MapTileServer server, int zoom, int x, int y )
            throws IOException
    {
        return getTile( server, zoom, x, y );
    }

    /**
     * Retrieve a tile from the cache
     * <p>
     * @param server
     * @param zoom
     * @param x      X
     * @param y      Y
     * <p>
     * @return Tile or null if x,y is invalid for this zoom
     */
    public Tile getTile( MapTileServer server, int zoom, int x, int y )
            throws IOException
    {
        final long max = 1L << zoom;
        final long lx = (long) x, ly = (long) y;
        if( lx < 0 || lx >= max || ly < 0 || ly >= max )
        {
            return null;
        }

        final Tile tile = getCacheEntry( server, zoom, x, y );

        // If no image but not pending (retrieving) or in error then background thread to retrieve it
        if( !tile.isImagePresent() && !tile.isError() && !tile.isPending() )
        {
            synchronized( tile )
            {

                if( !tile.isImagePresent() && !tile.isError() )
                {
                    loadTile( tile );
                }

                if( !tile.isImagePresent() && !tile.isError() && !tile.isPending() )
                {
                    try
                    {
                        retrieveTile( tile );
                        loadTile( tile );
                    } catch( IOException ex )
                    {
                        LOG.log( Level.SEVERE, ex, () -> tile.toString() );
                        throw ex;
                    }
                }
            }
        }

        return tile;
    }

    private FileSystem getCache( MapTileServer tileServer )
    {
        try
        {
            return FileSystems.newFileSystem( URI.create( "cache://" + tileServer.name() ),
                                              MapBuilder.<String, Object>builder()
                                              .add( "maxAge", TimeUnit.MILLISECONDS.convert( 2, TimeUnit.DAYS ) )
                                              .add( "scanDelay", TimeUnit.MILLISECONDS.convert( 1, TimeUnit.HOURS ) )
                                              .add( "expireOnStartup", true )
                                              .build() );
        } catch( IOException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }

    private Path getPath( final Tile tile )
    {
        return caches.computeIfAbsent( tile.getServer(), this::getCache )
                .getPath( String.valueOf( tile.getZ() ),
                          String.valueOf( tile.getX() ),
                          String.valueOf( tile.getY() ) + ".png" );
    }

    private void loadTile( final Tile tile )
    {
        synchronized( tile )
        {
            Path path = getPath( tile );
            LOG.log( Level.FINE, () -> "Load tile " + path );
            try
            {
                tile.setImage( ImageUtils.readImage( path ) );
            } catch( Exception ex )
            {
                tile.setImage( null );
            }
        }
    }

    private void retrieveTile( final Tile tile )
            throws IOException
    {
        synchronized( tile )
        {
            try
            {
                tile.setPending( true );
                Path path = getPath( tile );

                URL url = new URL( tile.getServer().getTileUrl( tile ) );
                LOG.log( Level.INFO, () -> "Retrieving " + url );

                URLConnection con = url.openConnection();
                con.connect();
                try( InputStream is = con.getInputStream() )
                {
                    Files.copy( is, path, StandardCopyOption.REPLACE_EXISTING );
                }
            } catch( IOException ex )
            {
                LOG.log( Level.SEVERE, null, ex );
                tile.setError( true );
                throw ex;
            }
            finally
            {
                LOG.log( Level.SEVERE, "Pending false" );
                tile.setPending( false );
            }
        }
    }
}
