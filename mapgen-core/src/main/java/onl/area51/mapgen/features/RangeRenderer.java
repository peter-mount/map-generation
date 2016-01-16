/*
 * Copyright 2016 peter.
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
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import onl.area51.mapgen.layer.DefaultTiledLayer;
import onl.area51.mapgen.layer.Layer;
import onl.area51.mapgen.renderer.Renderer;

/**
 *
 * @author peter
 */
public abstract class RangeRenderer
        implements Consumer<Renderer>
{
    // Padding on y axis
    private static final int PADD_Y = 2;
    // Padding on x axis
    private static final int PADD_X = 4;
    // Offset in Y axis
    private static final int OFF_Y=PADD_Y>>1;

    private final String text;
    private final Color colour;
    private final Color background;
    private final Color border;
    private Layer layer;

    private static final Map<String, Create> CREATORS = new ConcurrentHashMap<>();

    static {
        CREATORS.put("bottomleft", RangeRenderer::bottomLeft );
        CREATORS.put("bottomright", RangeRenderer::bottomRight );
        CREATORS.put("topleft", RangeRenderer::topLeft );
        CREATORS.put("topright", RangeRenderer::topRight );
    }

    public static void register( String type, Create c )
    {
        CREATORS.put( type.toLowerCase(), c );
    }

    public static RangeRenderer create( String type, String text, Color colour, Color background, Color border )
    {
        Create c = CREATORS.get( type.toLowerCase() );
        if( c == null ) {
            throw new IllegalArgumentException( "Unsupported TextRenderer type " + type );
        }
        return c.create( text, colour, background, border );
    }

    public RangeRenderer( String text, Color colour, Color background, Color border )
    {
        this.text = " " + text.trim() + " ";
        this.colour = colour;
        this.background = background;
        this.border = border;
    }

    protected double getWidth( Graphics2D g )
    {
        return g.getFontMetrics().getStringBounds( text, g ).getWidth();
    }

    protected void paint( Graphics2D g, FontMetrics fm, int x, int y, int w, int h )
    {
        g.clipRect( x, y, w, h );

        if( background != null ) {
            g.setColor( background );
            g.fillRect(x, y, w + PADD_X, h + PADD_Y );
        }

        g.setColor( colour );
        g.drawString(text, x + PADD_Y, y + OFF_Y + fm.getMaxAscent() );

        if( border != null ) {
            g.setColor( border );
            g.drawRect( x, y, w - 1, h - 1 );
        }
    }

    @Override
    public void accept( Renderer r )
    {
        r.draw(g -> {

            FontMetrics fm = g.getFontMetrics();
            double w = getWidth( g );
            int h = fm.getMaxAscent() + fm.getMaxDescent();
            paint(r, g, fm, (int) w + PADD_X, h + PADD_Y );
        } );
    }

    public Layer getLayer()
    {
        if( layer == null ) {
            layer = new DefaultTiledLayer( text, true, this );
        }
        return layer;
    }

    protected abstract void paint( Renderer r, Graphics2D g, FontMetrics fm, int w, int h );

    public static RangeRenderer topLeft( String text, Color color, Color background, Color border )
    {
        return new RangeRenderer( text, color, background, border )
        {
            @Override
            protected void paint( Renderer r, Graphics2D g, FontMetrics fm, int w, int h )
            {
                Rectangle rect = r.getVisible();
                paint( g, fm, rect.x, rect.y, w, h );
            }

        };
    }

    public static RangeRenderer bottomLeft( String text, Color color, Color background, Color border )
    {
        return new RangeRenderer( text, color, background, border )
        {
            @Override
            protected void paint( Renderer r, Graphics2D g, FontMetrics fm, int w, int h )
            {
                Rectangle rect = r.getVisible();
                paint( g, fm, rect.x, rect.y + r.getVisible().height - h, w, h );
            }

        };
    }

    public static RangeRenderer topRight( String text, Color color, Color background, Color border )
    {
        return new RangeRenderer( text, color, background, border )
        {
            @Override
            protected void paint( Renderer r, Graphics2D g, FontMetrics fm, int w, int h )
            {
                Rectangle rect = r.getVisible();
                paint( g, fm, rect.x + r.getVisible().width - w, rect.y, w, h );
            }

        };
    }

    public static RangeRenderer bottomRight( String text, Color color, Color background, Color border )
    {
        return new RangeRenderer( text, color, background, border )
        {
            @Override
            protected void paint( Renderer r, Graphics2D g, FontMetrics fm, int w, int h )
            {
                Rectangle rect = r.getVisible();
                paint( g, fm, rect.x + r.getVisible().width - w, rect.y + r.getVisible().height - h, w, h );
            }

        };
    }

    @FunctionalInterface
    public static interface Create
    {

        RangeRenderer create( String text, Color color, Color background, Color border );
    }
}
