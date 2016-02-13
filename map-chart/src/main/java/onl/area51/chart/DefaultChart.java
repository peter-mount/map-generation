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
package onl.area51.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import onl.area51.mapgen.util.GraphicsUtils;

/**
 *
 * @author peter
 */
public class DefaultChart
        implements Chart
{

    private static final int TICK_MINOR = 3;
    private static final int TICK_MAJOR = 5;

    private final Rectangle bounds;
    private Rectangle chartBounds;
    private final int width;
    private final int height;
    private final List<ChartLayer> layers = new ArrayList<>();
    private boolean refresh = true;
    private double xaxis, yaxis;
    private Insets insets;

    private Font font;
    private Color axisColour = Color.BLACK;
    private Color chartFrameColour = null;
    private Color chartFillColour = Color.WHITE;
    private Color backgroundColour = Color.WHITE;

    private final Map<Axis.Position, Axis> axes = new EnumMap<>( Axis.Position.class );

    private String title;

    public DefaultChart( Rectangle r )
    {
        // FIXME -1 is so border is not clipped
        this.bounds = new Rectangle( r.width - 1, r.height - 1 );
        width = this.bounds.width;
        height = this.bounds.height;
        font = new Font( Font.SANS_SERIF, Font.PLAIN, 10 );
    }

    public DefaultChart( int width, int height )
    {
        this( new Rectangle( width, height ) );
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public Chart setTitle( String title )
    {
        this.title = title;
        return this;
    }

    @Override
    public Color getAxisColour()
    {
        return axisColour;
    }

    @Override
    public Chart setAxisColour( Color axisColour )
    {
        this.axisColour = axisColour;
        return this;
    }

    @Override
    public Color getChartFrameColour()
    {
        return chartFrameColour;
    }

    @Override
    public Chart setChartFrameColour( Color chartFrameColour )
    {
        this.chartFrameColour = chartFrameColour;
        return this;
    }

    @Override
    public Color getChartFillColour()
    {
        return chartFillColour;
    }

    @Override
    public Chart setChartFillColour( Color chartFillColour )
    {
        this.chartFillColour = chartFillColour;
        return this;
    }

    @Override
    public Color getBackgroundColour()
    {
        return backgroundColour;
    }

    @Override
    public Chart setBackgroundColour( Color backgroundColour )
    {
        this.backgroundColour = backgroundColour;
        return this;
    }

    @Override
    public Font getFont()
    {
        return font;
    }

    @Override
    public Chart setFont( Font font )
    {
        this.font = font;
        refresh = true;
        return this;
    }

    @Override
    public Chart setAxis( Axis a )
    {
        axes.put( a.getPosition(), a );
        return this;
    }

    private void refresh()
    {
        if( refresh ) {

            int fontSize = font.getSize();
            insets = new Insets( title == null || title.isEmpty() ? 1 : fontSize + 2,
                                 Axis.adjustInset( axes.get( Axis.Position.LEFT ), TICK_MAJOR, TICK_MINOR, fontSize ),
                                 Axis.adjustInset( axes.get( Axis.Position.X ), TICK_MAJOR, TICK_MINOR, fontSize ),
                                 Axis.adjustInset( axes.get( Axis.Position.RIGHT ), TICK_MAJOR, TICK_MINOR, fontSize ) );

            chartBounds = new Rectangle( bounds.x + insets.left,
                                         bounds.y + insets.top,
                                         bounds.width - insets.left - insets.right,
                                         bounds.height - insets.top - insets.bottom );

            Chart t = this;
            xaxis = Integer.MIN_VALUE;

            List<Point2D> l = layers.stream()
                    .map( layer -> layer.refresh( t ) )
                    .collect( Collectors.toList() );

            xaxis = l.stream().mapToDouble( Point2D::getX ).max().orElse( 0.0 );
            yaxis = l.stream().mapToDouble( Point2D::getY ).max().orElse( 0.0 );

            refresh = false;
        }
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public double getXAxisPosition()
    {
        refresh();
        return xaxis;
    }

    @Override
    public double getYAxisPosition()
    {
        refresh();
        return yaxis;
    }

    @Override
    public int getLayerCount()
    {
        return layers.size();
    }

    @Override
    public ChartLayer getLayer( int index )
    {
        return layers.get( index );
    }

    @Override
    public Chart addLayer( ChartLayer layer )
    {
        layers.add( layer );
        refresh = true;
        return this;
    }

    @Override
    public Chart removeLayer( ChartLayer layer )
    {
        layers.remove( layer );
        refresh = true;
        return this;
    }

    @Override
    public void forEach( Consumer<ChartLayer> action )
    {
        refresh();
        layers.forEach( action );
    }

    @Override
    public Stream<ChartLayer> stream()
    {
        refresh();
        return layers.stream();
    }

    @Override
    public Rectangle getBounds()
    {
        return bounds;
    }

    @Override
    public Rectangle getChartBounds()
    {
        return chartBounds;
    }

    @Override
    public void accept( Graphics2D g0 )
    {
        forEach( l -> l.refresh( this ) );

        if( backgroundColour != null ) {
            GraphicsUtils.draw( g0, g -> {
                            g.setPaint( backgroundColour );
                            g.fill( bounds );
                        } );
        }

        // The chart content
        GraphicsUtils.draw( g0, g -> {
                        g.clip( chartBounds );

                        if( chartFillColour != null ) {
                            g.setPaint( chartFillColour );
                            g.fill( chartBounds );
                        }

                        g.translate( chartBounds.getMinX(), chartBounds.getMaxY() );
                        g.scale( 1.0, -1.0 );
                        forEach( l -> l.accept( g ) );
                    } );

        if( title != null && !title.isEmpty() ) {
            GraphicsUtils.draw( g0, g -> {
                            g.setFont( font );
                            g.setPaint( axisColour );
                            g.drawString( title, chartBounds.x, chartBounds.y - 2 );
                        } );
        }
        // Axes
        axes.values().forEach( a -> GraphicsUtils.draw( g0, g -> drawAxis( a, g ) ) );

        // The outer chart frame
        if( chartFrameColour != null ) {
            GraphicsUtils.draw( g0, g -> {
                            g.setPaint( chartFrameColour );
                            g.setStroke( new BasicStroke( 1 ) );
                            g.draw( new Rectangle( bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2 ) );
                        } );
        }
    }

    private void drawAxis( Axis a, Graphics2D g )
    {
        ChartLayer layer = a.getChartLayer();
        ChartDataSource ds = layer.getDataSource();
        DoubleSummaryStatistics stats = ds.getStatistics();

        g.setFont( font );
        g.setPaint( axisColour );
        g.setStroke( new BasicStroke( 1 ) );
        int w = 0, s = 1, so = 0, ts = 1, to = 0;
        double r, xs = 0;
        switch( a.getPosition() ) {
            case LEFT:
                g.translate( chartBounds.getMinX(), chartBounds.getMaxY() );
                g.rotate( -Math.PI / 2.0 );
                w = chartBounds.height;
                r = layer.getMaxY() - layer.getMinY();
                ts = -1;
                to = -g.getFont().getSize();
                xs = 0;
                break;

            case RIGHT:
                g.translate( chartBounds.getMaxX(), chartBounds.getMinY() );
                g.rotate( Math.PI / 2.0 );
                s = -1;
                so = chartBounds.width;
                w = -chartBounds.height;
                r = layer.getMaxY() - layer.getMinY();
                ts = -1;
                to = -g.getFont().getSize();
                xs = chartBounds.height;
                break;

            case X:
            default:
                g.translate( chartBounds.getMinX(), chartBounds.getMaxY() );
                w = chartBounds.width;
                r = ds.getSize();
                ts = 1;
                to = g.getFont().getSize() + 3;
                xs = 0;
                break;
        }

        g.drawLine( 0, 0, Math.abs( w ), 0 );

        if( a.isTitle() ) {
            g.drawString( a.getTitle(), 0, to * 2 );
        }

        if( a.isMajor() ) {
            drawTicks( g, layer.getMinY(), layer.getMaxY(), a.getMajor(),
                       d -> String.format( "%.0f", d ),
                       xs, a.getMajor() * w / r, ts * TICK_MAJOR, to );
        }
        if( a.isMinor() ) {
            drawTicks( g, layer.getMinY(), layer.getMaxY(), a.getMinor(),
                       null,
                       xs, a.getMinor() * w / r, ts * TICK_MINOR, to );
        }

    }

    private void drawTicks( Graphics2D g, double s, double e, double d, DoubleFunction<String> f, double x0, double xs, int tl, int to )
    {
        double x = x0, v = s;
        while( v < e ) {
            g.drawLine( (int) x, 0, (int) x, tl );
            if( f != null ) {
                String t = f.apply( v );
                if( t != null && !t.isEmpty() ) {
                    g.drawString( t, (float) x, to );
                }
            }
            v += d;
            x += xs;
        }
    }
}
