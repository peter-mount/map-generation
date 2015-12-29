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

import onl.area51.mapgen.renderer.layer.LinkedLayers;
import onl.area51.mapgen.renderer.layer.Layers;
import onl.area51.mapgen.renderer.layer.ReadOnlyLayers;
import onl.area51.mapgen.renderer.layer.DefaultLayer;
import java.util.function.Consumer;
import java.util.function.Function;
import onl.area51.mapgen.renderer.Renderer;

/**
 * Wraps a {@link Layers} instance making it read-only. Note, this simply makes those methods do nothing rather than causing an error.
 * <p>
 * @author peter
 */
public class BaseLayers
        extends ReadOnlyLayers
{

    public BaseLayers( Function<MapTileServer, Consumer<Renderer>> supplier )
    {
        super( new LinkedLayers() );
        Layers l = getLayers();
        for( MapTileServer s: MapTileServer.values() ) {
            l.add( new DefaultLayer( s.toString(), supplier.apply( s ) ) );
        }
    }

}
