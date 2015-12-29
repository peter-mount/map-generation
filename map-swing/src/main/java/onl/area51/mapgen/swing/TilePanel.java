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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import onl.area51.mapgen.tilecache.Tile;
import onl.area51.mapgen.tilecache.TileCache;
import onl.area51.mapgen.renderer.DefaultRenderer;
import onl.area51.mapgen.renderer.Renderer;
import onl.area51.mapgen.tilecache.MapPreset;

/**
 *
 * @author peter
 */
public class TilePanel
        extends JPanel
{

    private boolean showGrid;

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

    /**
     * Called when something changes like the Zoom or Underlying map
     */
    public void repaintMap()
    {
        setPreset( null );
    }

    public void setPreset( MapPreset preset )
    {
        int x = Renderer.TILE_SIZE * (1 << TileCache.INSTANCE.getZoom());
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
//        JScrollPane p = (JScrollPane) SwingUtilities.getAncestorOfClass( JScrollPane.class, this );
//        Point pt = p.getViewport().getViewPosition();
//        SwingUtils.setStatus( "zoom=%s x=%d y=%d", TileCache.INSTANCE.getZoom(), pt.x, pt.y );

        Shape ccache = g.getClip();
        Renderer renderer = new DefaultRenderer( g, getVisibleRect(), TileCache.INSTANCE.getZoom() );
        renderer.foreach( this::paintTile );

        g.setClip( ccache );
    }

    private void paintTile( Renderer r )
    {
        Graphics g = r.getGraphics();

        Tile tile = TileCache.INSTANCE.getTile( r.getX(), r.getY(), t -> repaint(), ( t, e ) -> repaint() );
        if( tile != null ) {
            if( tile.isImagePresent() ) {
                r.drawImage( tile.getImage(), getBackground(), this );
            }

            if( !tile.isImagePresent() || isShowGrid() ) {
                paintMissingTile( r );
            }
        }
    }

    private void paintMissingTile( Renderer r )
    {
        int xp = r.getXp(), yp = r.getYp();
        r.setColor( Color.red );
        r.drawRect( xp, yp, Renderer.TILE_SIZE, Renderer.TILE_SIZE );
        setFont( getFont() );
        r.getGraphics().drawString( String.format( "(%d,%d,%d)", r.getZoom(), r.getX(), r.getY() ),
                                    xp + (Renderer.TILE_SIZE >>> 2),
                                    yp + (Renderer.TILE_SIZE >>> 1) );
    }
}
