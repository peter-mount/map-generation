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
package onl.area51.mapgen.contour;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import onl.area51.mapgen.features.GridRenderer;
import onl.area51.mapgen.features.TileRenderer;
import onl.area51.mapgen.util.ColorMap;
import onl.area51.mapgen.util.ImageType;
import onl.area51.mapgen.renderer.RendererUtils;
import org.junit.Test;
import onl.area51.mapgen.grid.Grid;
import onl.area51.mapgen.layer.DefaultFullLayer;
import onl.area51.mapgen.layer.DefaultTiledLayer;
import onl.area51.mapgen.layer.Layers;
import onl.area51.mapgen.layer.LinkedLayers;
import onl.area51.mapgen.tilecache.MapTileServer;

/**
 *
 * @author peter
 */
public class ContourTest
{

    private static final int N_CONTOURS = 10;

    private static final double[][] DATA
                                    = {{-0.44, -0.44, -0.44, -0.44, -0.44, -0.45, -0.48, -0.51, -0.52, -0.50, -0.49, -0.51, -0.55, -0.59, -0.60},
                                       {-0.45, -0.48, -0.50, -0.49, -0.47, -0.44, -0.44, -0.44, -0.41, -0.40, -0.43, -0.43, -0.47, -0.55, -0.59},
                                       {-0.52, -0.57, -0.60, -0.59, -0.56, -0.50, -0.44, -0.37, -0.33, -0.46, -0.56, -0.45, -0.36, -0.50, -0.58},
                                       {-0.59, -0.58, -0.53, -0.54, -0.59, -0.58, -0.47, -0.32, -0.33, -0.52, -0.35, -0.55, -0.47, -0.46, -0.57},
                                       {-0.58, -0.40, -0.20, -0.25, -0.47, -0.60, -0.51, -0.32, -0.35, -0.39, 0.23, -0.33, -0.55, -0.44, -0.56},
                                       {-0.52, -0.18, 0.14, 0.06, -0.31, -0.58, -0.54, -0.34, -0.33, -0.46, -0.10, -0.47, -0.53, -0.45, -0.56},
                                       {-0.52, -0.19, 0.12, 0.05, -0.32, -0.58, -0.55, -0.37, -0.28, -0.46, -0.55, -0.57, -0.45, -0.48, -0.58},
                                       {-0.58, -0.41, -0.23, -0.27, -0.49, -0.60, -0.53, -0.40, -0.31, -0.35, -0.43, -0.44, -0.45, -0.54, -0.59},
                                       {-0.59, -0.59, -0.54, -0.55, -0.60, -0.57, -0.49, -0.42, -0.40, -0.41, -0.43, -0.47, -0.53, -0.58, -0.60},
                                       {-0.52, -0.57, -0.59, -0.58, -0.55, -0.50, -0.44, -0.44, -0.48, -0.51, -0.53, -0.56, -0.58, -0.60, -0.60}};

    @Test
    public void rawContour()
            throws IOException
    {
        Grid data = Grid.of( DATA );

        ColorMap cmap = ColorMap.gradient( Color.RED, Color.BLUE, N_CONTOURS + 1 );

        Contour contour = Contour.basic( cmap );
        contour.setData( data );
        contour.setScale( 40f );

        Layers layers = new LinkedLayers();
        layers.add( new DefaultTiledLayer( new GridRenderer( true ) ).setEnabled( true ) );
        layers.add( new DefaultFullLayer( r -> r.draw( contour::draw ) ).setEnabled( true ) );
        layers.add( new DefaultTiledLayer( new TileRenderer( MapTileServer.OPEN_STREET_MAP ) ).setEnabled( true ) );

        BufferedImage img = ImageType.INT_RGB.create( 800, 800 );
        RendererUtils.render( img, 2, 0, 0, layers );

        ImageIO.write( img, "png", new File( "contour-test.png" ) );
    }

    @Test
    public void contourLayer()
            throws IOException
    {
        Grid data = Grid.of( DATA );

        ColorMap cmap = ColorMap.gradient( Color.RED, Color.BLUE, N_CONTOURS + 1 );

        Contour contour = Contour.basic( cmap );
        contour.setData( data );
        contour.setScale( 40f );

        Layers layers = new LinkedLayers();
        layers.add( new DefaultTiledLayer( new GridRenderer( true ) ).setEnabled( true ) );
        layers.add( new ContourLayer( contour ).setEnabled( true ) );
        layers.add( new DefaultTiledLayer( new TileRenderer( MapTileServer.OPEN_STREET_MAP ) ).setEnabled( true ) );

        BufferedImage img = ImageType.INT_RGB.create( 800, 800 );
        RendererUtils.render( img, 2, 0, 0, layers );

        ImageIO.write( img, "png", new File( "contour-test.png" ) );
    }

}
