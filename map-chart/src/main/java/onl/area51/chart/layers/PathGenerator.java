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

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.function.DoubleConsumer;

/**
 *
 * @author peter
 */
public class PathGenerator
        implements Shape,
                   DoubleConsumer
{

    private final Path2D.Double path = new Path2D.Double();
    private int x;
    private boolean move = true;

    public void setMove()
    {
        this.move = true;
    }

    public void setX( int x )
    {
        this.x = x;
        this.move = true;
    }

    @Override
    public void accept( double value )
    {
        if( value == Double.NaN ) {
            x++;
            move = true;
        }
        else if( move ) {
            path.moveTo( x++, value );
            move = false;
        }
        else {
            path.lineTo( x++, value );
        }
    }

    @Override
    public Rectangle getBounds()
    {
        return path.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D()
    {
        return path.getBounds2D();
    }

    @Override
    public boolean contains( double x, double y )
    {
        return path.contains( x, y );
    }

    @Override
    public boolean contains( Point2D p )
    {
        return path.contains( p );
    }

    @Override
    public boolean intersects( double x, double y, double w, double h )
    {
        return path.intersects( x, y, w, h );
    }

    @Override
    public boolean intersects( Rectangle2D r )
    {
        return path.intersects( r );
    }

    @Override
    public boolean contains( double x, double y, double w, double h )
    {
        return path.contains( x, y, w, h );
    }

    @Override
    public boolean contains( Rectangle2D r )
    {
        return path.contains( r );
    }

    @Override
    public PathIterator getPathIterator( AffineTransform at )
    {
        return path.getPathIterator( at );
    }

    @Override
    public PathIterator getPathIterator( AffineTransform at, double flatness )
    {
        return path.getPathIterator( at, flatness );
    }

}
