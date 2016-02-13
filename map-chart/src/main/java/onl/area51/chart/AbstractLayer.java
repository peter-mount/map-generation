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

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.DoubleSummaryStatistics;

/**
 * A basic layer that draws a line
 *
 * @author peter
 */
public abstract class AbstractLayer
        implements ChartLayer
{

    protected final ChartDataSource dataSource;
    protected double xr, yr;

    protected Chart chart;

    protected boolean keepYAxisVisible = true, keepXAxisVisible = true;
    protected double minY = Double.NaN, maxY = Double.NaN, xOffset = 0.0;

    public AbstractLayer( ChartDataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    @Override
    public ChartDataSource getDataSource()
    {
        return dataSource;
    }

    
    @Override
    public boolean isKeepXAxisVisible()
    {
        return keepXAxisVisible;
    }

    @Override
    public ChartLayer setKeepXAxisVisible( boolean f )
    {
        keepXAxisVisible = f;
        return this;
    }

    @Override
    public boolean isKeepYAxisVisible()
    {
        return keepYAxisVisible;
    }

    @Override
    public ChartLayer setKeepYAxisVisible( boolean f )
    {
        keepYAxisVisible = f;
        return this;
    }

    @Override
    public double getXOffset()
    {
        return xOffset;
    }

    @Override
    public ChartLayer setXOffset( double xOffset )
    {
        this.xOffset = xOffset;
        return this;
    }

    @Override
    public double getMinY()
    {
        return minY;
    }

    @Override
    public ChartLayer setMinY( double minY )
    {
        this.minY = minY;
        return this;
    }

    @Override
    public double getMaxY()
    {
        return maxY;
    }

    @Override
    public ChartLayer setMaxY( double maxY )
    {
        this.maxY = maxY;
        return this;
    }

    @Override
    public Point2D refresh( Chart chart )
    {
        DoubleSummaryStatistics stats = dataSource.getStatistics();

        Rectangle bounds = chart.getChartBounds();
        
        xr = bounds.getWidth() / (double) (dataSource.getSize() - 1);

        double y0 = minY == Double.NaN ? stats.getMin() : minY;
        double y1 = maxY == Double.NaN ? stats.getMax() : maxY;

        double r;
        if( y1 < 0 ) {
            // everything is below 0 so keep 0 in the range
            r = -y0;
        }
        else if( y0 < 0 ) {
            // Allow to go below 0
            r = y1 - y0;
        }
        else {
            // Keep 0 in the range
            r = y1;
        }

        yr = bounds.getHeight() / r;

        return new Point2D.Double( xOffset, (double) -stats.getMin() * yr );
    }

}
