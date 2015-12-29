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
package onl.area51.mapgen.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import onl.area51.mapgen.renderer.Renderer;
import onl.area51.mapgen.renderer.util.RendererUtils;
import onl.area51.mapgen.renderers.GridRenderer;
import onl.area51.mapgen.tilecache.MapPreset;
import uk.trainwatch.util.Consumers;

/**
 *
 * @author peter
 */
public class TilePanel
        extends JPanel
{

    private boolean showGrid;
    private int zoom;
    private final Consumer<Renderer> gridRenderer = new GridRenderer( true );
    private Consumer<String> mapNotifier;
    private Consumer<Renderer> renderer;

    public TilePanel()
    {
        setFont( Font.decode( Font.MONOSPACED ) );
    }

    public void setShowGrid( boolean showGrid )
    {
        this.showGrid = showGrid;
    }

    public boolean isShowGrid()
    {
        return showGrid;
    }

    public int getZoom()
    {
        return zoom;
    }

    public void setZoom( int zoom )
    {
        this.zoom = zoom;
    }

    /**
     * If not null a consumer that is informed of the map specifications for the displayed map
     * <p>
     * @return
     */
    public Consumer<String> getMapNotifier()
    {
        return mapNotifier;
    }

    /**
     * If not null a consumer that is informed of the map specifications for the displayed map
     * <p>
     * @param mapNotifier
     */
    public void setMapNotifier( Consumer<String> mapNotifier )
    {
        this.mapNotifier = mapNotifier;
    }

    public Consumer<Renderer> getRenderer()
    {
        return renderer;
    }

    public void setRenderer( Consumer<Renderer> renderer )
    {
        this.renderer = renderer.andThen( Consumers.ifThen( t -> showGrid, gridRenderer ) );
    }

    /**
     * Called when something changes like the Zoom or Underlying map
     */
    public void repaintMap()
    {
        setPreset( null );
    }

    public void setPreset( MapPreset preset )
    {
        int x = Renderer.TILE_SIZE * (1 << getZoom());
        Dimension d = new Dimension( x, x );
        setMaximumSize( d );
        setPreferredSize( d );
        invalidate();
        repaint();

        if( preset != null ) {
            SwingUtils.invokeLater( () -> {
                JScrollPane p = (JScrollPane) SwingUtilities.getAncestorOfClass( JScrollPane.class, this );
                JViewport viewport = p.getViewport();
                viewport.setViewPosition( new Point( preset.getX(), preset.getY() ) );
            } );
        }
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        // Shows the current position which can be used to generate predefined views
        if( mapNotifier != null ) {
            JViewport p = ((JScrollPane) SwingUtilities.getAncestorOfClass( JScrollPane.class, this )).getViewport();
            Point pt = p.getViewPosition();
            mapNotifier.accept( String.format( "zoom=%s x=%d y=%d size=%s ret=%s",
                                               getZoom(), pt.x, pt.y,
                                               p.getViewSize(),
                                               p.getViewRect() ) );
        }

        if( renderer != null ) {
            RendererUtils.render( g, this, zoom, renderer );
        }
    }

}
