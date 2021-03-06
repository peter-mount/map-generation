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

import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import uk.trainwatch.util.MapBuilder;

/**
 *
 * @author peter
 */
public enum MapTileServer
{

    OPEN_STREET_MAP( "OpenStreetMap", "http://c.tile.openstreetmap.org/%z/%x/%y.png", MapTileLayout.OPEN_STREET_MAP, 0, 18 ),
    OPEN_CYCLE_MAP( "OpenCycleMap", "http://a.tile.opencyclemap.org/cycle/%z/%x/%y.png", MapTileLayout.OPEN_STREET_MAP, 0, 10 ),
    MAP_QUEST( "MapQuest", "http://otile1.mqcdn.com/tiles/1.0.0/osm/%z/%x/%y.jpg", MapTileLayout.OPEN_STREET_MAP, 0, 10 );

    private final String title;
    private final String uri;
    private final MapTileLayout layout;
    private final int minZoom;
    private final int maxZoom;

    private static final Map<String, MapTileServer> SERVERS = MapBuilder.<String, MapTileServer>builder()
            .readonly()
            .concurrent()
            .keyMapper( n -> n == null || n.isEmpty() ? "" : n.trim().toUpperCase() )
            .key( v -> v.name().toUpperCase() )
            .addAll( values() )
            .key( v -> v.name().replace( '_', ' ' ).toUpperCase() )
            .addAll( values() )
            .key( v -> v.getTitle().toUpperCase() )
            .addAll( values() )
            .build();

    public static MapTileServer lookup( String name )
    {
        return SERVERS.getOrDefault( name, OPEN_STREET_MAP );
    }

    public static ComboBoxModel newComboBoxModel()
    {
        return new DefaultComboBoxModel( MapTileServer.values() );
    }

    private MapTileServer( String title, String uri, MapTileLayout layout, int minZoom, int maxZoom )
    {
        this.title = title;
        this.uri = uri;
        this.layout = layout;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    public String getTitle()
    {
        return title;
    }

    @Override
    public String toString()
    {
        return title;
    }

    public MapTileLayout getLayout()
    {
        return layout;
    }

    public int getMinZoom()
    {
        return minZoom;
    }

    public int getMaxZoom()
    {
        return maxZoom;
    }

    public String getTileUrl( Tile tile )
    {
        return uri.replace( "%z", String.valueOf( tile.getZ() ) )
                .replace( "%x", String.valueOf( tile.getX() ) )
                .replace( "%y", String.valueOf( tile.getY() ) );
    }
}
