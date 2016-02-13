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
package onl.area51.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A chart of some kind
 *
 * @author peter
 */
public interface Chart
        extends Consumer<Graphics2D>
{

    /**
     * The required width of the chart in "pixels".
     * <p>
     * This must be greater than one.
     *
     * @return
     */
    int getWidth();

    /**
     * The required height of the chart in "pixels".
     * <p>
     * This must be greater than one however for some chart types, if this is 0 then the charts height may be generated automatically.
     *
     * @return
     */
    int getHeight();

    Font getFont();

    Chart setFont( Font font );

    Color getChartFrameColour();

    Chart setChartFrameColour( Color chartFrameColour );

    Color getChartFillColour();

    Chart setChartFillColour( Color chartFillColour );

    Color getBackgroundColour();

    Chart setBackgroundColour( Color chartFillColour );

    Color getAxisColour();

    Chart setAxisColour( Color axisColour );

    Chart setAxis( Axis a );

    String getTitle();

    Chart setTitle( String t );

    default Rectangle getBounds()
    {
        return new Rectangle( getWidth(), getHeight() );
    }

    default Rectangle getChartBounds()
    {
        return getBounds();
    }

    /**
     * The position of the X axis 0 value from the top of the chart in "pixels"
     *
     * @return
     */
    double getXAxisPosition();

    /**
     * The position of the Y axis 0 value from the left of the chart in "pixels"
     *
     * @return
     */
    double getYAxisPosition();

    int getLayerCount();

    ChartLayer getLayer( int index );

    Chart addLayer( ChartLayer layer );

    Chart removeLayer( ChartLayer layer );

    void forEach( Consumer<ChartLayer> action );

    Stream<ChartLayer> stream();

}
