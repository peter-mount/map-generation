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
package onl.area51.geotools.render;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import onl.area51.mapgen.util.GraphicsUtils;
import onl.area51.mapgen.util.ImageType;
import onl.area51.mapgen.util.ImageUtils;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;

/**
 *
 * @author peter
 */
public class RenderUtils
{

    public static void draw( File file, Rectangle size, Consumer<Graphics2D> action )
    {
        try {
            String n = file.getName();
            if( n.endsWith( ".png" ) || n.endsWith( ".jpg" ) ) {
                drawImage( file, size, action );
            }
            else if( n.endsWith( ".svg" ) ) {
                drawSvg( file, size, action );
            }
            else {
                throw new UnsupportedOperationException( "Unable to render to " + n );
            }
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
        catch( ParserConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

    public static void drawImage( File file, Rectangle size, Consumer<Graphics2D> action )
            throws IOException
    {
        BufferedImage img = ImageType.INT_RGB.create( size );
        GraphicsUtils.draw( img, action );
        ImageUtils.writeImage( img, file );
    }

    public static void drawSvg( File file, Rectangle size, Consumer<Graphics2D> action )
            throws IOException,
                   ParserConfigurationException
    {
        Dimension canvasSize = new Dimension( size.x + size.width, size.y + size.height );

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // Create an instance of org.w3c.dom.Document
        Document document = db.getDOMImplementation().createDocument( null, "svg", null );

        // Set up the map
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault( document );
        ctx.setComment( "Generated by Area51 MapGen with GeoTools2 and Batik SVG Generator" );

        SVGGraphics2D g2d = new SVGGraphics2D( ctx, true );
        try {
            g2d.setSVGCanvasSize( canvasSize );
            action.accept( g2d );

            try( FileWriter osw = new FileWriter( file ) ) {
                g2d.stream( osw );
            }
        }
        finally {
            g2d.dispose();
        }
    }

}