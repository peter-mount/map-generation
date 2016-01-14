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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import onl.area51.mapgen.gis.TileReference;
import onl.area51.mapgen.grid.GridPoint;
import onl.area51.mapgen.grid.GridSupport;

/**
 * Object used to store state during a render
 * <p>
 * @author peter
 */
public abstract class AbstractRenderer
        implements Renderer
{

    private final Graphics graphics;
    private final Rectangle visible;
    private final ImageObserver imageObserver;
    private final int zoom;

    private final int left, top, right, bottom, maxXY;

    public AbstractRenderer( Graphics graphics, Rectangle visible, ImageObserver imageObserver, int zoom )
    {
        this.graphics = graphics;
        this.zoom = zoom;
        this.visible = visible;
        this.imageObserver = imageObserver;

        // Max tile on each azis (exclusive) so range is 0 .. maxXY-1
        maxXY = 1 << getZoom();

        left = visible.x / TILE_SIZE;
        top = visible.y / TILE_SIZE;
        right = Math.min( left + (visible.width / TILE_SIZE) + 2, maxXY ) - 1;
        bottom = Math.min( top + (visible.height / TILE_SIZE) + 2, maxXY ) - 1;
    }

    @Override
    public final ImageObserver getImageObserver()
    {
        return imageObserver;
    }

    @Override
    public final int getZoom()
    {
        return zoom;
    }

    @Override
    public final Graphics getGraphics()
    {
        return graphics;
    }

    @Override
    public final Rectangle getVisible()
    {
        return visible;
    }

    @Override
    public void forEach( Consumer<? super Renderer> action )
    {
        // Although we handle a tile around the edge, we must keep it within 0<=v<maxXY
        IntUnaryOperator inRange = v -> Math.max( 0, Math.min( v, maxXY - 1 ) );

        GridSupport.stream( zoom,
                            inRange.applyAsInt( getTop() - 1 ),
                            inRange.applyAsInt( getBottom() + 1 ),
                            inRange.applyAsInt( getLeft() - 1 ),
                            inRange.applyAsInt( getRight() + 1 ) )
                .map( this::map )
                .forEach( action );
    }

    private Renderer map( GridPoint p )
    {
        return new RendererWrapper( this )
        {
            private TileReference ref;

            @Override
            public int getZoom()
            {
                return p.getZ();
            }

            @Override
            public int getX()
            {
                return p.getX();
            }

            @Override
            public int getY()
            {
                return p.getY();
            }

            @Override
            public void setY( int y )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setX( int x )
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public TileReference getTileReference()
            {
                if( ref == null ) {
                    ref = TileReference.of( p.getZ(), p.getX(), p.getY() );
                }
                return ref;
            }

            @Override
            public void forEach(
                    Consumer<? super Renderer> action )
            {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public final int getMaxXY()
    {
        return maxXY;
    }

    @Override
    public final int getLeft()
    {
        return left;
    }

    @Override
    public final int getTop()
    {
        return top;
    }

    @Override
    public final int getRight()
    {
        return right;
    }

    @Override
    public final int getBottom()
    {
        return bottom;
    }

    @Override
    public void drawImage( Image image )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawImage( Image image, int w, int h )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void drawImage( Image image, AffineTransform t )
    {
        throw new UnsupportedOperationException();
    }

}
