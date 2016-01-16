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

import java.awt.Graphics2D;
import onl.area51.mapgen.util.ColorMap;
import onl.area51.mapgen.grid.Grid;

/**
 * A representation of a Contour map
 *
 * @author peter
 */
public interface Contour
{

    /**
     * Set the data source
     *
     * @param data data source
     */
    default void setData( Grid data )
    {
        setData( data, data.getWidth(), data.getHeight() );
    }

    /**
     * Set the data source
     *
     * @param data data source
     * @param x    x step
     * @param y    y step
     */
    void setData( Grid data, int x, int y );

    /**
     * Get the current scale of data to image
     *
     * @return
     */
    float getScale();

    /**
     * Set the scale of data coordinates to image coordinates
     *
     * @param s
     */
    void setScale( float s );

    /**
     * Draw the contours
     *
     * @param g Graphics2D
     */
    void draw( Graphics2D g );

    default void setDrawGrid( boolean g )
    {
    }

    default boolean isDrawGrid()
    {
        return false;
    }

    default void setShowNumbers( boolean f )
    {
    }

    default boolean isShowNumbers()
    {
        return false;
    }

    default void setLogInterpolation( boolean f )
    {
    }

    default boolean isLogInterpolation()
    {
        return false;
    }

    /**
     * A basic contour map
     *
     * @param map
     *
     * @return
     */
    static Contour basic( ColorMap map )
    {
        return new BasicContour( map );
    }
}
