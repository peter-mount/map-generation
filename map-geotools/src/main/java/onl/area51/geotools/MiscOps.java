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
package onl.area51.geotools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import onl.area51.mapgen.util.ColorMap;
import onl.area51.mapgen.util.ColourType;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import uk.trainwatch.job.Scope;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 * Misc JOB operations
 * <p>
 * @author peter
 */
public class MiscOps
{

    public static File getFile( ExpressionOperation op, Scope s )
            throws Exception
    {
        Object o = op.invoke( s );
        if( o instanceof File ) {
            return (File) o;
        }
        if( o instanceof Path ) {
            return ((Path) o).toFile();
        }
        return new File( o.toString() );
    }

    public static URL getURL( ExpressionOperation op, Scope s )
            throws Exception
    {
        Object o = op.invoke( s );
        if( o instanceof URL ) {
            return (URL) o;
        }
        if( o instanceof File ) {
            return ((File) o).toURI().toURL();
        }
        if( o instanceof Path ) {
            return ((Path) o).toFile().toURI().toURL();
        }
        return new URL( o.toString() );
    }

    public static FileDataStore getFileDataStore( ExpressionOperation op, Scope s )
            throws Exception
    {
        Object o = op.invoke( s );
        if( o instanceof URL ) {
            return FileDataStoreFinder.getDataStore( (URL) o );
        }
        if( o instanceof File ) {
            return FileDataStoreFinder.getDataStore( (File) o );
        }
        if( o instanceof Path ) {
            return FileDataStoreFinder.getDataStore( ((Path) o).toFile() );
        }
        return FileDataStoreFinder.getDataStore( new File( o.toString() ) );
    }

    public static Path getPath( ExpressionOperation op, Scope s )
            throws Exception
    {
        Object o = op.invoke( s );
        if( o instanceof File ) {
            return ((File) o).toPath();
        }
        if( o instanceof Path ) {
            return (Path) o;
        }
        return Paths.get( new URI( o.toString() ) );
    }

    public static BufferedImage getImage( ExpressionOperation op, Scope s )
            throws Exception
    {
        return (BufferedImage) op.invoke( s );
    }

    public static Color getColor( ExpressionOperation op, Scope s )
            throws Exception
    {
        Object o = op.invoke( s );
        if( o == null ) {
            return null;
        }
        if( o instanceof Color ) {
            return (Color) o;
        }
        String c = o.toString();

        if( c.startsWith( "#" ) ) {
            return ColourType.decodeHex( c );
        }

        return ColourType.lookup( o.toString() ).getColor();
    }

}
