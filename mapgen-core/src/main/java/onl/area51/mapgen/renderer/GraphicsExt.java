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
package onl.area51.mapgen.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;

/**
 *
 * @author peter
 */
public interface GraphicsExt
{

    /**
     * The Graphics context
     * <p>
     * @return
     */
    Graphics getGraphics();

    default Graphics2D getGraphics2D()
    {
        return (Graphics2D) getGraphics();
    }

    default void setColor( Color c )
    {
        getGraphics().setColor( c );
    }

    default void setFont( Font f )
    {
        getGraphics().setFont( f );
    }

    default void drawRect( int x, int y, int w, int h )
    {
        getGraphics().drawRect( x, y, w, h );
    }

    default void drawLine( int x1, int y1, int x2, int y2 )
    {
        getGraphics().drawLine( x1, y1, x2, y2 );
    }

    default void drawPolygon( Polygon p )
    {
        getGraphics().drawPolygon( p );
    }

    default void draw( Shape s )
    {
        getGraphics2D().draw( s );
    }

    default void fill( Shape s )
    {
        getGraphics2D().fill( s );
    }

    default void setPaint( Paint paint )
    {
        getGraphics2D().setPaint( paint );
    }

    default void setStroke( Stroke s )
    {
        getGraphics2D().setStroke( s );
    }
}
