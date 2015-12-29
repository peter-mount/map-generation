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
package onl.area51.mapgen.renderer.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 *
 * @author peter
 */
public class ImageUtils
{

    /**
     * Convenience method to call {@link ImageIO#read(java.io.InputStream)} from a {@link Path}.
     * <p>
     * @param path
     *             <p>
     * @return
     *         <p>
     * @throws IOException
     */
    public static BufferedImage readImage( Path path )
            throws IOException
    {
        try( InputStream is = Files.newInputStream( path, StandardOpenOption.READ ) ) {
            return ImageIO.read( is );
        }
    }

    public static BufferedImage readImage( Object o )
            throws IOException
    {
        Objects.requireNonNull( o );
        if( o instanceof Path ) {
            return readImage( (Path) o );
        }
        if( o instanceof File ) {
            return ImageIO.read( (File) o );
        }
        if( o instanceof InputStream ) {
            return ImageIO.read( (InputStream) o );
        }
        return ImageIO.read( new File( o.toString() ) );
    }

    public static File writeImage( BufferedImage image, Object name )
            throws IOException
    {
        Objects.requireNonNull( image, "Image is null" );
        Objects.requireNonNull( name, "Name is null" );

        File file = name instanceof File ? (File) name : new File( Objects.toString( name ) );
        writeImage( image, file );
        return file;
    }

    public static void writeImage( BufferedImage image, Object name, Object type )
            throws IOException
    {
        Objects.requireNonNull( image, "Image is null" );
        Objects.requireNonNull( name, "Name is null" );
        Objects.requireNonNull( type, "Type is null" );

        String t = type.toString();

        if( name instanceof File ) {
            ImageIO.write( image, t, (File) name );
        }
        else if( name instanceof OutputStream ) {
            ImageIO.write( image, t, (OutputStream) name );
        }
        else {
            ImageIO.write( image, t, new File( name.toString() ) );
        }
    }

    public static void writeImage( BufferedImage image, File file )
            throws IOException
    {
        ImageIO.write( image, getFileType( file.getName() ), file );
    }

    public static void writeImage( BufferedImage image, Path path )
            throws IOException
    {
        try( OutputStream os = Files.newOutputStream( path, StandardOpenOption.WRITE, StandardOpenOption.CREATE ) ) {
            ImageIO.write( image, getFileType( path.getName( path.getNameCount() - 1 ).toString() ), os );
        }
    }

    public static String getFileType( String name )
    {
        String n = name;
        int i = n.lastIndexOf( '.' );
        if( i > 0 ) {
            n = n.substring( i + 1 );
        }
        switch( n ) {
            case "jpeg":
            case "jpg":
                return "jpeg";

            case "png":
            default:
                return "png";
        }
    }
}
