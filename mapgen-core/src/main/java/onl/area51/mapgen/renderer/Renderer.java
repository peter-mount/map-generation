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

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;
import onl.area51.mapgen.gis.TileReference;
import static onl.area51.mapgen.renderer.AbstractRenderer.TILE_SIZE;

/**
 *
 * @author peter
 */
public interface Renderer
        extends GraphicsExt
{

    static final int TILE_SIZE = 256;

    /**
     * Call the specified consumer for each visible tile in this map
     * <p>
     * @param action
     */
    void forEach( Consumer<? super Renderer> action );

    /**
     * If the current tile is visible then pass it to the supplied consumer
     * <p>
     * @param action
     */
    default void render( Consumer<? super Renderer> action )
    {
        if( isVisible() ) {
            action.accept( this );
        }
    }

    /**
     * The bottom Y tile number that is visible
     * <p>
     * @return
     */
    int getBottom();

    /**
     * The left X tile number that is visible
     * <p>
     * @return
     */
    int getLeft();

    /**
     * The highest tile number on each axis (exclusive), so range is 0 ... maxXY-1
     * <p>
     * @return
     */
    int getMaxXY();

    /**
     * The right X tile number that is visible
     * <p>
     * @return
     */
    int getRight();

    /**
     * The top Y tile number that is visible
     * <p>
     * @return
     */
    int getTop();

    /**
     * The visible rectangle
     * <p>
     * @return
     */
    Rectangle getVisible();

    /**
     * The current tile X
     * <p>
     * @return
     */
    int getX();

    /**
     * The current tile X in pixels not tiles
     * <p>
     * @return
     */
    default int getXp()
    {
        return getX() * TILE_SIZE;
    }

    /**
     * The current tile Y
     * <p>
     * @return
     */
    int getY();

    /**
     * The current tile Y in pixels not tiles
     * <p>
     * @return
     */
    default int getYp()
    {
        return getY() * TILE_SIZE;
    }

    /**
     * The map zoom scale, 0..18
     * <p>
     * @return
     */
    int getZoom();

    /**
     * Is the current tile visible
     * <p>
     * @return
     */
    default boolean isVisible()
    {
        int x = getX(), y = getY();
        return x >= getLeft() && x <= getRight() && y >= getTop() && y <= getBottom();
    }

    /**
     * The new tile X
     * <p>
     * @param x
     */
    void setX( int x );

    /**
     * The new tile Y
     * <p>
     * @param y
     */
    void setY( int y );

    default Rectangle2D getBounds()
    {
        return null;
    }

    default TileReference getTileReference()
    {
        return TileReference.of( getZoom(), getX(), getY() );
    }

    void drawImage( Image image );

    void drawImage( Image image, int w, int h );

    void drawImage( Image image, AffineTransform t );
}
