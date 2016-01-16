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
package onl.area51.mapgen.features;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import onl.area51.mapgen.layer.DefaultTiledLayer;
import onl.area51.mapgen.layer.Layers;
import onl.area51.mapgen.layer.LinkedLayers;
import onl.area51.mapgen.renderer.RendererUtils;
import onl.area51.mapgen.tilecache.MapTileServer;
import onl.area51.mapgen.util.ImageType;
import org.junit.Test;

/**
 *
 * @author peter
 */
public class TextRendererTest
{

    @Test
    public void render()
            throws IOException,
                   InterruptedException
    {
        Layers layers = new LinkedLayers();

        layers.add( TextRenderer.topLeft( "Top left", Color.yellow, Color.darkGray, Color.red ).getLayer() );
        layers.add( TextRenderer.topRight( "Top right", Color.yellow, Color.darkGray, Color.red ).getLayer() );
        layers.add( TextRenderer.bottomLeft( "bottom left", Color.yellow, Color.darkGray, Color.red ).getLayer() );
        layers.add( TextRenderer.bottomRight( "Map © OpenStreetMap contributors", Color.BLUE, Color.WHITE, Color.BLACK ).getLayer() );

        // Base map
        layers.add( new DefaultTiledLayer( new TileRenderer( MapTileServer.OPEN_STREET_MAP ) ).setEnabled( true ) );

        BufferedImage img = ImageType.INT_RGB.create( 1024, 1024 );
        RendererUtils.render( img, 6, 29, 18, layers );
        ImageIO.write( img, "png", new File( "TextRendererTest.png" ) );
    }

}
