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
package onl.area51.gfs.grib2.job;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.gfs.grib2.GribFile;
import ucar.grib.grib2.Grib2Record;
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

    public static GribFile getGribFile( ExpressionOperation fileOp, ExpressionOperation modeOp, Scope s )
            throws Exception
    {
        return new GribFile( getFile( fileOp, s ), Objects.toString( modeOp.invoke( s ), "r" ) );
    }

    public static GribFile getGribFile( ExpressionOperation fileOp, String mode, Scope s )
            throws Exception
    {
        return new GribFile( getFile( fileOp, s ), mode );
    }

    public static Grib2 getGrib2( ExpressionOperation op, Scope s )
            throws Exception
    {
        return (Grib2) op.invoke( s );
    }

    public static Grib2Record getGrib2Record( ExpressionOperation op, Scope s )
            throws Exception
    {
        return (Grib2Record) op.invoke( s );
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

}
