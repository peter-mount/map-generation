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
package onl.area51.mapgen.vector;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import onl.area51.mapgen.renderer.layer.DefaultLayer;
import onl.area51.mapgen.renderer.layer.Layers;
import onl.area51.mapgen.renderer.layer.LinkedLayers;
import onl.area51.mapgen.renderer.util.ImageType;
import onl.area51.mapgen.renderer.util.RendererUtils;
import onl.area51.mapgen.renderers.GridRenderer;
import onl.area51.mapgen.renderers.TileRenderer;
import onl.area51.mapgen.tilecache.MapTileServer;
import org.junit.Test;
import uk.trainwatch.gis.Coordinate;

/**
 *
 * @author peter
 */
public class VectorLayerTest
{

    private static final Coordinate MAIDSTONE = Coordinate.of( 0.529, 51.272 );
    private static final Coordinate SOUTHAMPTON = Coordinate.of( -1.4, 50.9 );
    private static final Coordinate CARDIFF = Coordinate.of( -3.11, 51.29 );

    @Test
    public void point()
            throws IOException,
                   InterruptedException
    {
        VectorLayer layer = VectorLayer.create();
        layer.addPoint( 0.529, 51.272, "Maidstone" );
        layer.addPoint( -1.4, 50.9, "Southampton" );
        layer.addPoint( -3.11, 51.29, "Cardiff" );
        layer.addPoint( -0.1275, 51.507222, "London" );

        Layers layers = new LinkedLayers();
        layers.add( new DefaultLayer( layer ).setEnabled( true ) );
        layers.add( new DefaultLayer( new GridRenderer( true ) ).setEnabled( true ) );
        layers.add( new DefaultLayer( new TileRenderer( MapTileServer.OPEN_STREET_MAP ) ).setEnabled( true ) );

        BufferedImage img = ImageType.INT_RGB.create( 1024, 1024 );
        RendererUtils.render( img, 6, 29, 18, layers );
        ImageIO.write( img, "png", new File( "Vector-Point.png" ) );
    }

}
