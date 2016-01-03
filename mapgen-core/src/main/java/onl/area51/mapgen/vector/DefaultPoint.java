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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import onl.area51.mapgen.gis.TileReference;
import onl.area51.mapgen.renderer.Renderer;
import uk.trainwatch.gis.Coordinate;

/**
 *
 * @author peter
 */
public class DefaultPoint
        implements Point
{

    private final Coordinate coord;
    private final Rectangle2D bounds;
    private final String label;
    private final Color colour;

    DefaultPoint( Coordinate coord, String label, Color colour )
    {
        this.coord = coord;
        this.label = label;
        this.colour = colour;

        // Single point so bounds is 0.1degrees to keep it small
        // TODO this may need changing
        bounds = new Rectangle2D.Double( coord.getLongitude(), coord.getLatitude(), 0.01, 0.01 );
    }

    @Override
    public Rectangle2D getBounds()
    {
        return bounds;
    }

    @Override
    public Coordinate getCoordinate()
    {
        return coord;
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    @Override
    public Color getColour()
    {
        return colour;
    }

    @Override
    public void accept( Renderer r )
    {
        TileReference ref = TileReference.fromCoordinate( r.getZoom(), coord );
        if( r.getTileReference().contains( ref ) ) {
            int x = r.getXp() + ref.getPx( r.getX() ) - 3;
            int y = r.getYp() + ref.getPy( r.getY() ) - 3;
            Graphics2D g = r.getGraphics2D();
            r.setColor( getColour() );
            g.fillOval( x, y, 7, 7 );
            if( label != null && !label.isEmpty() ) {
                g.drawString( label, x, y );
            }
        }
    }

}
