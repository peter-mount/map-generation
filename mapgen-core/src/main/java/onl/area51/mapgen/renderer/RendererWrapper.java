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
import java.awt.Rectangle;
import java.util.Iterator;

/**
 *
 * @author peter
 */
public class RendererWrapper
        implements Renderer
{

    private final Renderer delegate;

    public RendererWrapper( Renderer delegate )
    {
        this.delegate = delegate;
    }

    @Override
    public int getBottom()
    {
        return delegate.getBottom();
    }

    @Override
    public Graphics getGraphics()
    {
        return delegate.getGraphics();
    }

    @Override
    public int getLeft()
    {
        return delegate.getLeft();
    }

    @Override
    public int getMaxXY()
    {
        return delegate.getMaxXY();
    }

    @Override
    public int getRight()
    {
        return delegate.getRight();
    }

    @Override
    public int getTop()
    {
        return delegate.getTop();
    }

    @Override
    public Rectangle getVisible()
    {
        return delegate.getVisible();
    }

    @Override
    public int getX()
    {
        return delegate.getX();
    }

    @Override
    public int getY()
    {
        return delegate.getY();
    }

    @Override
    public int getZoom()
    {
        return delegate.getZoom();
    }

    @Override
    public void setX( int x )
    {
        delegate.setX( x );
    }

    @Override
    public void setY( int y )
    {
        delegate.setY( y );
    }

    @Override
    public Iterator<Renderer> iterator()
    {
        return delegate.iterator();
    }

}
