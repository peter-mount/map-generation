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
package onl.area51.chart.layers;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import onl.area51.chart.Axis;
import onl.area51.chart.Chart;
import onl.area51.chart.ChartDataSource;
import onl.area51.chart.ChartLayer;
import onl.area51.chart.DefaultChart;
import onl.area51.mapgen.util.GraphicsUtils;
import onl.area51.mapgen.util.ImageType;
import onl.area51.mapgen.util.ImageUtils;
import org.junit.Test;

/**
 *
 * @author peter
 */
public class LineLayerTest
{

    /**
     * A simple line plot of Southeastern's Daily PPM figures for January 2016
     *
     * @throws java.io.IOException
     */
    @Test
    public void southeastern012016()
            throws IOException
    {
        Rectangle dimension = new Rectangle( 600, 400 );

        ChartLayer layer = new LineLayer( ChartDataSource.create( TempData.SE_PPM_JAN_2016 ) )
                .setMinY( 0 )
                .setMaxY( 100 );

//        ChartLayer pc100 = new LineLayer( ChartDataSource.create( TempData.PC100 ) )
//                .setMinY( 0 )
//                .setMaxY( 100 );
        Chart chart = new DefaultChart( dimension )
                .setTitle( "Southeastern PPM January 2016" )
                //.setChartFrameColour( Color.DARK_GRAY )
                .setBackgroundColour( Color.LIGHT_GRAY )
                .setChartFillColour( Color.WHITE )
                .setAxis( Axis.builder()
                        .setChartLayer( layer )
                        .setPosition( Axis.Position.LEFT )
                        .setMajor( 20 )
                        .setMinor( 10 )
                        .setTitle( "PPM" )
                        .build() )
                .setAxis( Axis.builder()
                        .setChartLayer( layer )
                        .setPosition( Axis.Position.RIGHT )
                        .setMajor( 20 )
                        .setMinor( 10 )
                        .setTitle( "PPM" )
                        .build() )
                .setAxis( Axis.builder()
                        .setChartLayer( layer )
                        .setPosition( Axis.Position.X )
                        .setMajor( 10 )
                        .setMinor( 1 )
                        .setTitle( "Day of Month" )
                        .build() )
                .addLayer( layer );

        BufferedImage img = ImageType.INT_RGB.create( dimension );
        GraphicsUtils.clearImage( img, Color.WHITE, null );

        GraphicsUtils.draw( img, chart );

        ImageUtils.writeImage( img, "rtppm_se_201601.png" );
        throw new RuntimeException();
    }

}
