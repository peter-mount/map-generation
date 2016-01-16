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
package onl.area51.mapgen.features;

import java.awt.Color;
import java.util.function.Consumer;
import onl.area51.mapgen.renderer.Renderer;

/**
 * A Grid renderer which draws a grid around each tile.
 * <p>
 * @author peter
 */
public class GridRenderer
        implements Consumer<Renderer>
{

    private boolean showLabel;
    private Color color;

    /**
     * Show a red grid, no label
     */
    public GridRenderer()
    {
        this( false, Color.RED );
    }

    /**
     * Show a grid of the specified colour, no label
     * <p>
     * @param color Color of the grid & text
     */
    public GridRenderer( Color color )
    {
        this( false, color );
    }

    /**
     * Show a red grid with the tile label (z,x,y)
     * <p>
     * @param showLabel true to show a label of the tile (z,x,y)
     */
    public GridRenderer( boolean showLabel )
    {
        this( showLabel, Color.RED );
        this.showLabel = showLabel;
    }

    /**
     * Show a grid of the specified colour and the tile label (z,x,y)
     * <p>
     * @param showLabel true to show a label of the tile (z,x,y)
     * @param color     Colour of the grid & text
     */
    public GridRenderer( boolean showLabel, Color color )
    {
        this.showLabel = showLabel;
        this.color = color;
    }

    @Override
    public void accept( Renderer r )
    {
        int xp = r.getXp(), yp = r.getYp();
        r.setColor( color );
        r.drawRect( xp, yp, Renderer.TILE_SIZE, Renderer.TILE_SIZE );

        if( showLabel ) {
            //r.getGraphics().setFont(  );
            r.getGraphics().drawString( String.format( "(%d,%d,%d)", r.getZoom(), r.getX(), r.getY() ),
                                        xp + (Renderer.TILE_SIZE >>> 2),
                                        yp + (Renderer.TILE_SIZE >>> 1) );
        }
    }
}
