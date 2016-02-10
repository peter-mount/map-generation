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

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.CRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import uk.trainwatch.job.Scope;
import uk.trainwatch.job.lang.Statement;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 *
 * @author peter
 */
public class FeatureOps
{

    public static FeatureType getFeatureType( ExpressionOperation op, Scope s )
            throws Exception
    {
        Object o = op.get( s );
        if( o instanceof FeatureType ) {
            return (FeatureType) o;
        }
        if( o instanceof FeatureSource ) {
            return ((FeatureSource) o).getSchema();
        }
        throw new IllegalArgumentException( "Unknown FeatureType " + o.getClass() );
    }

    /**
     * Gets a FeatureSource from an expression.
     * <p>
     * This will expect a FeatureSource, FileDataStore, File, Path or URL.
     *
     * @param op
     * @param s
     *
     * @return
     *
     * @throws Exception
     */
    public static FeatureSource getFeatureSource( ExpressionOperation op, Scope s )
            throws Exception
    {
        Object o = op.invoke( s );

        if( o instanceof FeatureSource ) {
            return (FeatureSource) o;
        }
        else if( o instanceof FileDataStore ) {
            return ((FileDataStore) o).getFeatureSource();
        }
        else if( o instanceof File ) {
            return FileDataStoreFinder.getDataStore( (File) o ).getFeatureSource();
        }
        else if( o instanceof Path ) {
            return FileDataStoreFinder.getDataStore( ((Path) o).toFile() ).getFeatureSource();
        }
        else if( o instanceof File ) {
            return FileDataStoreFinder.getDataStore( (URL) o ).getFeatureSource();
        }
        else {
            return FileDataStoreFinder.getDataStore( new File( o.toString() ) ).getFeatureSource();
        }
    }

    /**
     * Handles the new featureLayer() constructor.
     * <p>
     * There's 4 flavours here, all take a source (FeatureSource, File or URL) then optionally a style, title or style & title.
     *
     * @param exp
     *
     * @return
     */
    public static ExpressionOperation getFeatureLayer( ExpressionOperation exp[] )
    {
        switch( exp.length ) {
            // new featureLater( source )
            case 1:
                return ( s, a ) -> {
                    FeatureSource src = getFeatureSource( exp[0], s );
                    Style style = SLD.createSimpleStyle( src.getSchema() );
                    return new FeatureLayer( src, style );
                };

            // new featureLater( source, style)
            // new featureLater( source, title)
            case 2:
                return ( s, a ) -> {
                    FeatureSource src = getFeatureSource( exp[0], s );
                    Object o = exp[1].invoke( s );
                    if( o instanceof Style ) {
                        return new FeatureLayer( src, (Style) o );
                    }
                    return new FeatureLayer( src,
                                             SLD.createSimpleStyle( src.getSchema() ),
                                             Objects.toString( o ) );
                };

            // new featureLater( source, style, title)
            case 3:
                return ( s, a ) -> new FeatureLayer( getFeatureSource( exp[0], s ),
                                                     (Style) exp[1].get( s ),
                                                     Objects.toString( exp[2].invoke( s ) ) );

            default:
                return null;
        }
    }

    /**
     * Creates a new MapContent.
     * <p>
     * If any arguments are included they are added to the content. Order is not important but can be one of:
     * A layer
     * Collection of layers,
     * map viewport,
     * CoordinateReferenceSystem,
     * String. If the string starts with EPSG: then it's a CoordinateReferenceSystem otherwise the map title.
     * important here.
     * <p>
     * Note:
     *
     * @param exp
     *
     * @return
     */
    public static ExpressionOperation getMapContent( ExpressionOperation exp[] )
    {
        if( exp == null || exp.length == 0 ) {
            return ( s, a ) -> new CloseableMapContent( s );
        }
        else {
            return ( s, a ) -> {
                CloseableMapContent map = new CloseableMapContent( s );
                for( Object o: ExpressionOperation.invoke( exp, s ) ) {
                    if( o instanceof Collection ) {
                        map.addLayers( (Collection<Layer>) o );
                    }
                    else if( o instanceof Layer ) {
                        map.addLayer( (Layer) o );
                    }
                    else if( o instanceof MapViewport ) {
                        map.setViewport( (MapViewport) o );
                    }
                    else if( o instanceof CoordinateReferenceSystem ) {
                        map.getViewport().setCoordinateReferenceSystem( (CoordinateReferenceSystem) o );
                    }
                    else if( o != null ) {
                        String v = o.toString();
                        if( v.startsWith( "EPSG:" ) ) {
                            map.getViewport().setCoordinateReferenceSystem( CRS.decode( v ) );
                        }
                        else {
                            map.setTitle( v );
                        }
                    }
                }
                return map;
            };
        }
    }

    /**
     * setMapBounds( map, westLong, northLat, eastLong, southLat );
     *
     * @param exp
     *
     * @return
     */
    public static Statement setMapBounds( ExpressionOperation exp[] )
    {
        if( exp == null || exp.length != 5 ) {
            return null;
        }

        return ( s, a ) -> {
            MapContent map = exp[0].get( s );
            CrsOps.clipMap( map,
                            exp[4].getDouble( s ), exp[2].getDouble( s ), exp[1].getDouble( s ), exp[3].getDouble( s ) );
        };
    }
}
