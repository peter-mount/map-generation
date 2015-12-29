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

import static onl.area51.mapgen.job.MiscOps.*;

import onl.area51.mapgen.tilecache.MapTileServer;
import onl.area51.mapgen.tilecache.TileCache;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.job.ext.Extension;
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
        return "MapGeneraton";
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

            case 4:
                switch( name ) {
                    /**
                     * Retrieve a remote tile, caching it locally
                     * Tile = retrieveMapTile(server,zoom,x,y);
                     */
                    case "retrieveMapTile":
                        return ( s, a ) -> TileCache.INSTANCE.getTileSync(
                                MapTileServer.lookup( getString( args[0], s ) ),
                                getInt( args[1], s ),
                                getInt( args[2], s ),
                                getInt( args[3], s )
                        );
                }

            default:
        }

        return null;
    }

}
