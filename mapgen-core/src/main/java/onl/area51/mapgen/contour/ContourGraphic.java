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
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.function.BiFunction;

/**
 *
 * @author peter
 */
public interface ContourGraphic
{

    Graphics2D getGraphics();

    void setGraphics( Graphics2D graphics );

    void drawLine( int x1, int y1, int x2, int y2 );

    void setColor( Color c );

    void drawString( String str, int x, int y );

    void drawString( String str, float x, float y );

    void fillRect( int x, int y, int width, int height );

    static ContourGraphic create()
    {
        return new ContourGraphic()

        {
            private Graphics2D graphics;

            @Override
            public Graphics2D getGraphics()
            {
                return graphics;
            }

            @Override
            public void setGraphics( Graphics2D graphics )
            {
                this.graphics = graphics;
            }

            @Override
            public void drawLine( int x1, int y1, int x2, int y2 )
            {
                graphics.drawLine( x1, y1, x2, y2 );
            }

            @Override
            public void setColor( Color c )
            {
                graphics.setColor( c );
            }

            @Override
            public void drawString( String str, int x, int y )
            {
                graphics.drawString( str, x, y );
            }

            @Override
            public void drawString( String str, float x, float y )
            {
                graphics.drawString( str, x, y );
            }

            @Override
            public void fillRect( int x, int y, int width, int height )
            {
                graphics.fillRect( x, y, width, height );
            }

        };
    }

    static ContourGraphic create( BiFunction<Integer, Integer, Point2D> f )
    {
        return new ContourGraphic()

        {
            private Graphics2D graphics;

            @Override
            public Graphics2D getGraphics()
            {
                return graphics;
            }

            @Override
            public void setGraphics( Graphics2D graphics )
            {
                this.graphics = graphics;
            }

            @Override
            public void drawLine( int x1, int y1, int x2, int y2 )
            {
                Point2D p1 = f.apply( x1, y1 );
                Point2D p2 = f.apply( x2, y2 );
                graphics.drawLine( (int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY() );
            }

            @Override
            public void setColor( Color c )
            {
                graphics.setColor( c );
            }

            @Override
            public void drawString( String str, int x, int y )
            {
                Point2D p1 = f.apply( x, y );
                graphics.drawString( str, x, y );
            }

            @Override
            public void drawString( String str, float x, float y )
            {
                Point2D p1 = f.apply( (int) x, (int) y );
                graphics.drawString( str, (int) p1.getX(), (int) p1.getY() );
            }

            @Override
            public void fillRect( int x, int y, int width, int height )
            {
                Point2D p1 = f.apply( x, y );
                graphics.fillRect( (int) p1.getX(), (int) p1.getY(), width, height );
            }

        };
    }
}
