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
package onl.area51.geotools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author peter
 */
public enum WKTFactory
{
    INSTANCE;

    private final Map<String, String> WKT = new ConcurrentHashMap<>();
    private final Map<String, CoordinateReferenceSystem> CRS_MAP = new ConcurrentHashMap<>();

//    private WKTFactory()
//    {
//        this.WKT = MapBuilder.<String, String>builder()
//                .add( "AREA51:WGS84", "GEOGCS[\"WGS 84\","
//                                      + "  DATUM[\"WGS_1984\","
//                                      + "    SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],"
//                                      + "    TOWGS84[0,0,0,0,0,0,0],"
//                                      + "    AUTHORITY[\"EPSG\",\"6326\"]],"
//                                      + "  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
//                                      + "  UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],"
//                                      + "  AXIS[\"Lat\",NORTH],"
//                                      + "  AXIS[\"Long\",EAST],"
//                                      + "  AUTHORITY[\"EPSG\",\"4326\"]"
//                                      + "]" )
//                .add( "AREA51:ROBINSON", "PROJCS[\"World_Robinson\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"\n"
//                                         + "D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]\n"
//                                         + "],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\n"
//                                         + "\"Robinson\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],\n"
//                                         + "PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]" )
//                .add( "XAREA51:WINKEL_TRIPEL", "PROJCS[\"World_Winkel_Tripel_NGS\",\n"
//                                               + "    GEOGCS[\"GCS_WGS_1984\",\n"
//                                               + "        DATUM[\"D_WGS_1984\",\n"
//                                               + "            SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],\n"
//                                               + "        PRIMEM[\"Greenwich\",0.0],\n"
//                                               + "        UNIT[\"Degree\",0.0174532925199433]"
//                                               + "    ],\n"
//                                               + "    PROJECTION[\"Winkel_Tripel\"],\n"
//                                               + "    PARAMETER[\"False_Easting\",0.0],\n"
//                                               + "    PARAMETER[\"False_Northing\",0.0],\n"
//                                               + "    PARAMETER[\"Central_Meridian\",15.0],\n"
//                                               + "    PARAMETER[\"Standard_Parallel_1\",40.0],\n"
//                                               + "    UNIT[\"Meter\",1.0]"
//                                               + "]" )
//                .add( "AREA51:WINKEL_TRIPEL", "PROJCS[\"World_Winkel_Tripel_NGS\",\n"
//                                              + "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563]],PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.01745329251994328]],"
//                                              + "PROJECTION[\"Winkel_Tripel\"],\n"
////                                              + "    PARAMETER[\"False_Easting\",0.0],\n"
////                                              + "    PARAMETER[\"False_Northing\",0.0],\n"
////                                              + "    PARAMETER[\"Central_Meridian\",15.0],\n"
//                                              + "    PARAMETER[\"Standard_Parallel_1\",40.0],\n"
//                                              + "    UNIT[\"Meter\",1.0]"
//                                              + "]" )
//                .add( "AREA51:MERCATOR", "PROJCS[\"OBLIQUE MERCATOR\","
//                                         + "GEOGCS[\"WGS 84\","
//                                         + "DATUM[\"WGS_1984\","
//                                         + "SPHEROID[\"WGS 84\",6378137,298.257223563]],"
//                                         + "PRIMEM[\"Greenwich\",0],"
//                                         + "UNIT[\"degree\",0.01745329251994328]],"
//                                         + "PROJECTION[\"Hotine_Oblique_Mercator\"],"
//                                         // Centre location tags added to be replaced
//                                         + "PARAMETER[\"latitude_of_center\",0.0],"
//                                         + "PARAMETER[\"longitude_of_center\",0.0],"
//                                         // Azimuth and rectified grid angle set slightly below 90 to avoid
//                                         // precision errors
//                                         + "PARAMETER[\"azimuth\",89.999999],"
//                                         + "PARAMETER[\"rectified_grid_angle\",89.999999],"
//                                         + "PARAMETER[\"scale_factor\",1],"
//                                         + "PARAMETER[\"false_easting\",0],"
//                                         + "PARAMETER[\"false_northing\",0]," + "UNIT[\"metre\",1]]" )
//                .build();
//    }
    public CoordinateReferenceSystem getCrs( String n )
    {
        return CRS_MAP.computeIfAbsent( n, this::decodeCrs );
    }

    private CoordinateReferenceSystem decodeCrs( String n )
    {
        try {
            if( n.contains( ":" ) ) {
                return CRS.decode( n );
            }
            return loadCrs( n );
        } catch( FactoryException ex ) {
            Logger.getLogger( WKTFactory.class.getName() ).log( Level.SEVERE, null, ex );
            return null;
        }
    }

    private CoordinateReferenceSystem loadCrs( String n )
    {
        Logger LOG = Logger.getGlobal();
        LOG.log( Level.WARNING, () -> "/META-INF/wkt/" + n + ".wkt" );
        try( InputStream is = getClass().getResourceAsStream( "/META-INF/wkt/" + n + ".wkt" ) ) {
            LOG.log( Level.WARNING, () -> Objects.toString( is ) );
            if( is == null ) {
                return null;
            }
            try( BufferedReader br = new BufferedReader( new InputStreamReader( is ) ) ) {
                return ReferencingFactoryFinder.getCRSFactory( null )
                        .createFromWKT(
                                br.lines().collect( Collectors.joining( "\n" ) )
                        );
            }
        } catch( IOException | FactoryException ex ) {
            LOG.log( Level.WARNING, ex.getMessage(), ex );
            return null;
        }
    }
}
