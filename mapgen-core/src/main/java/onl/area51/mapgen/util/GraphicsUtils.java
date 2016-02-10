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
package onl.area51.mapgen.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 *
 * @author peter
 */
public class GraphicsUtils
{

    /**
     * Draw onto an image
     *
     * @param image  Image
     * @param action Action to draw
     */
    public static void draw( BufferedImage image, Consumer<Graphics2D> action )
    {
        Graphics2D g = image.createGraphics();
        try {
            action.accept( g );
        }
        finally {
            g.dispose();
        }
    }

    /**
     * Draw using a new Graphics2D
     *
     * @param g0     Existing Graphics2d
     * @param action action to draw
     */
    public static void draw( Graphics2D g0, Consumer<Graphics2D> action )
    {
        Graphics2D g = (Graphics2D) g0.create();
        try {
            action.accept( g );
        }
        finally {
            g.dispose();
        }
    }

    /**
     * Returns the image bounds
     *
     * @param image
     *
     * @return
     */
    public static Rectangle getImageBounds( BufferedImage image )
    {
        return new Rectangle( image.getWidth()-1, image.getHeight()-1 );
    }

    /**
     * Clear an image
     *
     * @param image      Image
     * @param background optional background colour
     * @param foreground optional border colour
     */
    public static void clearImage( BufferedImage image, Color background, Color foreground )
    {
        clearImage( image, background, foreground, getImageBounds( image ) );
    }

    /**
     * Clear part of an image
     *
     * @param image       Image
     * @param background  optional background colour
     * @param foreground  optional border colour
     * @param imageBounds Bounds of area to clear
     */
    public static void clearImage( BufferedImage image, Color background, Color foreground, Rectangle imageBounds )
    {
        draw( image, g -> {
          if( background != null ) {
              g.setPaint( background );
              g.fill( imageBounds );
          }
          if( foreground != null ) {
              g.setPaint( foreground );
              g.setStroke( new BasicStroke( 1 ) );
              g.draw( imageBounds );
          }
      } );
    }

}
