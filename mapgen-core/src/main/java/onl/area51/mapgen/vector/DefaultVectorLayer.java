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
package onl.area51.mapgen.vector;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import onl.area51.mapgen.renderer.Renderer;

/**
 *
 * @author peter
 */
public class DefaultVectorLayer
        implements VectorLayer
{

    private final List<Element> elements = new ArrayList<>();

    private Rectangle2D bounds = new Rectangle2D.Double();

    public DefaultVectorLayer add( Element e )
    {
        // If first entry then set the bounds to the single component
        if( elements.isEmpty() ) {
            bounds.setRect( e.getBounds() );
        }
        else {
            bounds.add( e.getBounds() );
        }
        elements.add( e );
        return this;
    }

    @Override
    public Rectangle2D getBounds()
    {
        return bounds;
    }

    @Override
    public void accept( Renderer t )
    {
        elements.forEach( e -> e.accept( t ) );
    }

}
