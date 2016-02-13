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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import onl.area51.chart.AbstractLayer;
import onl.area51.chart.ChartDataSource;

/**
 *
 * @author peter
 */
public class LineLayer
        extends AbstractLayer
{

    public LineLayer( ChartDataSource src )
    {
        super( src );
    }

    @Override
    public void accept( Graphics2D g )
    {
        g.setStroke( new BasicStroke( 1 ) );
        g.setPaint( Color.BLACK );

        double x0 = 0, y0 = 0;
        boolean move = true;
        int s = dataSource.getSize();
        for( int i = 0; i < s; i++ ) {
            double v = dataSource.getValue( i );
            if( v == Double.NaN ) {
                move = true;
            }
            else {
                double x1 = (i + xOffset) * xr, y1 = v * yr;
                if( !move ) {
                    g.drawLine( (int) x0, (int) y0, (int) x1, (int) y1 );
                }
                else {
                    move = false;
                }
                x0 = x1;
                y0 = y1;
            }
        }
    }

}
