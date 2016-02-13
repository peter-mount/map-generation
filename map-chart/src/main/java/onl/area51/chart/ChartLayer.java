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

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

/**
 * A layer in a chart. This is usually a specific data set being plotted.
 *
 * @author peter
 */
public interface ChartLayer
        extends Consumer<Graphics2D>
{

    ChartDataSource getDataSource();

    double getMinY();

    ChartLayer setMinY( double minY );

    double getMaxY();

    ChartLayer setMaxY( double maxY );

    boolean isKeepYAxisVisible();

    ChartLayer setKeepYAxisVisible( boolean f );

    boolean isKeepXAxisVisible();

    ChartLayer setKeepXAxisVisible( boolean f );

    double getXOffset();

    ChartLayer setXOffset( double xOffset );

    Point2D refresh( Chart chart );

}
