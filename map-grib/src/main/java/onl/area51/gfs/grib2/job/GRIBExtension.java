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

import java.io.File;
import java.util.stream.Collectors;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.gfs.grib2.Grib2Filters;
import static onl.area51.gfs.grib2.job.MiscOps.*;
import onl.area51.gfs.grib2.layer.Grib2Layer;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.job.ext.Extension;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 *
 * @author peter
 */
@MetaInfServices(Extension.class)
public class GRIBExtension
        implements Extension
{

    @Override
    public String getName()
    {
        return "GRIBExtension";
    }

    @Override
    public String getVersion()
    {
        return "1.0";
    }

    @Override
    public ExpressionOperation getExpression( String name, ExpressionOperation... args )
    {
        switch( args == null ? 0 : args.length ) {
            case 1:
                switch( name ) {

                    case "openGrib":
                        return ( s, a ) -> new Grib2( MiscOps.getGribFile( args[0], "r", s ) );

                    // file = retrieveGrib( hourOffset );
                    case "retrieveGrib":
                        return ( s, a ) -> {
                            int offset = args[0].getInt( s );
                            try( GribRetriever r = new GribRetriever() ) {
                                r.selectLatestRun();
                                return r.retrieveOffset( offset );
                            }
                        };

                    // file = retrieveGribForced( hourOffset );
                    case "retrieveGribForced":
                        return ( s, a ) -> {
                            int offset = args[0].getInt( s );
                            try( GribRetriever r = new GribRetriever() ) {
                                r.selectLatestRun();
                                return r.retrieveOffset( offset, true );
                            }
                        };

                    default:
                        break;
                }
                break;

            case 2:
                switch( name ) {

                    case "openGrib":
                        return ( s, a ) -> new Grib2( MiscOps.getGribFile( args[0], args[1], s ) );

                    // file = retrieveGrib( file, hourOffset );
                    case "retrieveGrib":
                        return ( s, a ) -> {
                            File file = getFile( args[0], s );
                            int offset = args[1].getInt( s );
                            try( GribRetriever r = new GribRetriever() ) {
                                r.selectLatestRun();
                                return r.retrieveOffset( file, offset );
                            }
                        };

                    // Force download
                    // file = retrieveGribForced( file, hourOffset );
                    case "retrieveGribForced":
                        return ( s, a ) -> {
                            File file = getFile( args[0], s );
                            int offset = args[1].getInt( s );
                            try( GribRetriever r = new GribRetriever() ) {
                                r.selectLatestRun();
                                return r.retrieveOffset( file, offset, true );
                            }
                        };

                    default:
                        break;
                }
                break;

            case 3:
                switch( name ) {

                    // file = retrieveGrib( dir, hourOffset, force );
                    case "retrieveGrib":
                        return ( s, a ) -> {
                            File file = getFile( args[0], s );
                            int offset = args[1].getInt( s );
                            boolean force = args[2].isTrue( s );
                            try( GribRetriever r = new GribRetriever() ) {
                                r.selectLatestRun();
                                r.retrieveOffset( file, offset, force );
                            }
                            return file;
                        };

                    default:
                        break;
                }
                break;

            case 4:
                switch( name ) {

                    // list = findGribParameter( grib2, pid, catid, paramid );
                    case "findGribParameter":
                        return ( s, a ) -> getGrib2( args[0], s ).records()
                                .filter( Grib2Filters.filterProduct( args[1].getInt( s ), args[2].getInt( s ), args[3].getInt( s ) ) )
                                .collect( Collectors.toList() );

                    default:
                        break;
                }
                break;

            case 5:
                switch( name ) {

                    // list = findGribParameter( grib2, pid, catid, paramid, levelId );
                    case "findGribParameter":
                        return ( s, a ) -> getGrib2( args[0], s ).records()
                                .filter( Grib2Filters.filterProduct( args[1].getInt( s ), args[2].getInt( s ), args[3].getInt( s ) ) )
                                .filter( Grib2Filters.filterLevel1( args[4].getInt( s ) ) )
                                .collect( Collectors.toList() );

                    default:
                        break;
                }
                break;

            case 6:
                switch( name ) {

                    // list = findGribParameter( grib2, pid, catid, paramid, levelId, level );
                    case "findGribParameter":
                        return ( s, a ) -> getGrib2( args[0], s ).records()
                                .filter( Grib2Filters.filterProduct( args[1].getInt( s ), args[2].getInt( s ), args[3].getInt( s ) ) )
                                .filter( Grib2Filters.filterLevel1( args[4].getInt( s ) ) )
                                .filter( Grib2Filters.filterLevel1( args[5].getDouble( s ) ) )
                                .findAny()
                                .orElse( null );

                    default:
                        break;
                }
                break;

            default:
                break;
        }
        return null;
    }

    @Override
    public ExpressionOperation construct( String type, ExpressionOperation... exp )
    {
        switch( exp.length ) {
            case 2:
                switch( type ) {

                    // new gribTextLater( Grib2, Grib2Record );
                    case "gribTextLayer":
                        return ( s, a ) -> Grib2Layer.textLayer( getGrib2( exp[0], s ), getGrib2Record( exp[1], s ) );

                    default:
                        return null;
                }

            case 4:
                switch( type ) {

                    // new gribTextLater( Grib2, Grib2Record );
                    case "gribContourLayer":
                        return ( s, a ) -> {
                            String t = exp[3].getString( s );
                            switch( t.toLowerCase() ) {
                                case "line":
                                    return Grib2Layer.contourLineLayer( exp[0].get( s ), getGrib2( exp[1], s ), getGrib2Record( exp[2], s ) );

                                default:
                                    throw new IllegalArgumentException( "Unsupported contour type " + t );
                            }
                        };

                    default:
                        return null;
                }

            default:
                return null;
        }
    }

}
