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
import java.awt.image.ImageObserver;

/**
 *
 * @author peter
 */
public final class DefaultRenderer
        extends AbstractRenderer
{

    public DefaultRenderer( Graphics graphics, Rectangle visible, int zoom )
    {
        super( graphics, visible, null, zoom );
    }

    public DefaultRenderer( Graphics graphics, Rectangle visible, ImageObserver imageObserver, int zoom )
    {
        super( graphics, visible, imageObserver, zoom );
    }

    @Override
    public int getX()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getY()
    {
        throw new UnsupportedOperationException();
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

}
