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

import java.util.Iterator;
import java.util.NoSuchElementException;
import onl.area51.mapgen.gis.TileReference;

/**
 *
 * @author peter
 */
class RendererIterator
        implements Iterator<Renderer>

{

    private int x, y;

    private final RendererWrapper wrapper;
    private TileReference ref;

    public RendererIterator( Renderer delegate )
    {
        // Start on top row but tile before the start on the x-axis
        x = delegate.getLeft() - 1;
        y = delegate.getTop();

        wrapper = new RendererWrapper( delegate )
        {

            @Override
            public int getX()
            {
                return x;
            }

            @Override
            public int getY()
            {
                return y;
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
                return ref;
            }

        };
    }

    @Override
    public boolean hasNext()
    {
        return y < wrapper.getBottom() || x < wrapper.getRight();
    }

    @Override
    public Renderer next()
    {
        x++;
        if( x > wrapper.getRight() ) {
            x = wrapper.getLeft();
            y++;
        }
        if( y <= wrapper.getBottom() && x <= wrapper.getRight() ) {
            ref = TileReference.of( wrapper.getZoom(), x, y );
            return wrapper;
        }
        throw new NoSuchElementException(
                String.format( "No such tile (%d,%d) in bounds (%d,%d,%d,%d)",
                               x, y,
                               wrapper.getLeft(), wrapper.getTop(),
                               wrapper.getRight(), wrapper.getBottom()
                )
        );
    }

}
