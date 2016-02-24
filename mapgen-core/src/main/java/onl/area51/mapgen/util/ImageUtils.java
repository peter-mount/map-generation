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
package onl.area51.mapgen.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Various Image related utilities
 *
 * @author peter
 */
public class ImageUtils
{

    public static Path getPath( Object name )
            throws IOException
    {
        Objects.requireNonNull( name );
        if( name instanceof Path ) {
            return (Path) name;
        }
        if( name instanceof File ) {
            return ((File) name).toPath();
        }
        String p = name.toString();
        if( p.matches( "^.+?://.*?/.*$" ) ) {
            try {
                return Paths.get( new URI( p ) );
            }
            catch( URISyntaxException ex ) {
                throw new IOException( ex );
            }
        }
        return Paths.get( p );
    }

    public static BufferedImage readImage( Object o )
            throws IOException
    {
        try( InputStream is = Files.newInputStream( getPath( o ), StandardOpenOption.READ ) ) {
            return ImageIO.read( is );
        }
    }

    public static Path writeImage( BufferedImage image, Object name )
            throws IOException
    {
        Objects.requireNonNull( image, "Image is null" );
        Objects.requireNonNull( name, "Name is null" );

        Path path = getPath( name );
        try( OutputStream os = Files.newOutputStream( path, StandardOpenOption.WRITE, StandardOpenOption.CREATE ) ) {
            ImageIO.write( image, getFileType( path.getName( path.getNameCount() - 1 ).toString() ), os );
        }
        return path;
    }

    public static void writeImage( BufferedImage image, Object name, Object type )
            throws IOException
    {
        Objects.requireNonNull( image, "Image is null" );
        Objects.requireNonNull( name, "Name is null" );
        Objects.requireNonNull( type, "Type is null" );

        try( OutputStream os = Files.newOutputStream( getPath( name ), StandardOpenOption.WRITE, StandardOpenOption.CREATE ) ) {
            ImageIO.write( image, type.toString(), os );
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
