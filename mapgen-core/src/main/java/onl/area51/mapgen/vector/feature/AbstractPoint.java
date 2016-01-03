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
package onl.area51.mapgen.vector.feature;

import onl.area51.mapgen.vector.Point;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import onl.area51.mapgen.gis.TileReference;
import onl.area51.mapgen.renderer.Renderer;
import uk.trainwatch.gis.Coordinate;

/**
 *
 * @author peter
 */
public abstract class AbstractPoint
        implements Point
{

    private final Coordinate coord;
    private final Rectangle2D bounds;
    private final String label;
    private final Color colour;

    public AbstractPoint( Coordinate coord )
    {
        this( coord, null, Color.BLACK );
    }

    public AbstractPoint( Coordinate coord, Color colour )
    {
        this( coord, null, colour );
    }

    public AbstractPoint( Coordinate coord, String label, Color colour )
    {
        this.coord = coord;
        this.label = label;
        this.colour = colour;

        // Single point so bounds is 0.1degrees to keep it small
        // TODO this may need changing
        bounds = new Rectangle2D.Double( coord.getLongitude(), coord.getLatitude(), 0.01, 0.01 );
    }

    @Override
    public final Rectangle2D getBounds()
    {
        return bounds;
    }

    @Override
    public final Coordinate getCoordinate()
    {
        return coord;
    }

    @Override
    public final String getLabel()
    {
        return label;
    }

    @Override
    public final Color getColour()
    {
        return colour;
    }

    @Override
    public final void accept( Renderer r )
    {
        TileReference ref = TileReference.fromCoordinate( r.getZoom(), coord );
        if( r.getTileReference().contains( ref ) ) {
            drawPoint( r,
                       ref,
                       r.getXp() + ref.getPx( r.getX() ) - 3,
                       r.getYp() + ref.getPy( r.getY() ) - 3
            );
        }
    }

    protected abstract void drawPoint( Renderer r, TileReference ref, int x, int y );

}
