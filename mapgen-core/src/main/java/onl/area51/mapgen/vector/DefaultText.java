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
import onl.area51.mapgen.util.tile.TileReference;
import onl.area51.mapgen.renderer.Renderer;
import onl.area51.mapgen.vector.Text;
import uk.trainwatch.gis.Coordinate;

/**
 * Some text drawn on a map.
 * <p>
 * @author peter
 */
public class DefaultText
        extends AbstractPoint
        implements Text
{

    public DefaultText( Coordinate coord, String label, Color colour )
    {
        super( coord, label, colour );
    }

    @Override
    protected void drawPoint( Renderer r, TileReference ref, int x, int y )
    {
        String label = getLabel();
        if( label != null && !label.isEmpty() ) {
            Graphics2D g = r.getGraphics2D();
            r.setColor( getColour() );
            g.drawString( label, x, y );
        }
    }
}
