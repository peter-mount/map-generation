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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.function.Consumer;
import javax.swing.JComponent;
import onl.area51.mapgen.renderer.DefaultRenderer;
import onl.area51.mapgen.renderer.Renderer;

/**
 * A utility class to generate a map as an image
 * <p>
 * @author peter
 */
public class RendererUtils
{

    public static final int TILE_SIZE = 256;

    public static void render( Graphics g, JComponent c, int z, Consumer<Renderer> action )
    {
        render( (Graphics2D) g, c, z, action );
    }

    public static void render( Graphics2D g, JComponent c, int z, Consumer<Renderer> action )
    {
        render( g, c.getVisibleRect(), c, z, action );
    }

    public static BufferedImage render( BufferedImage image, int z, int x, int y, Consumer<Renderer> action )
    {
        return render( image, z, (double)x, (double)y, action);
    }

    public static BufferedImage render( BufferedImage image, int z, double x, double y, Consumer<Renderer> action )
    {
        if( action != null ) {
            double left = x * TILE_SIZE;
            double top = y * TILE_SIZE;
            final Rectangle visible = new Rectangle( (int)left, (int)top, image.getWidth(), image.getHeight() );

            Graphics2D g = image.createGraphics();
            try {
                g.clearRect( 0, 0, image.getWidth(), image.getHeight() );
                g.setColor( Color.red );
                g.drawRect( 0, 0, image.getWidth(), image.getHeight() );
                g.translate( -left, -top );
                //g.translate( left, top );
                render( g, visible, null, z, action );
            }
            finally {
                g.dispose();
            }
        }

        return image;
    }

    public static void render( Graphics2D g, Rectangle visible, ImageObserver observer, int z, Consumer<Renderer> action )
    {
        if( action != null ) {
            final Shape ccache = g.getClip();
            try {
                g.setColor( Color.BLACK );
                g.setBackground( Color.WHITE );
                action.accept( new DefaultRenderer( g, visible, observer, z ));
            }
            finally {
                g.setClip( ccache );
            }
        }
    }

}
