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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import uk.trainwatch.job.Scope;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 * Coordinate Reference Operations
 *
 * @author peter
 */
public class CrsOps
{

    /**
     * Returns a CoordinateReferenceSystem from an expression.
     * <p>
     * expression result can be: CoordinateReferenceSystem, returned as is MapContent, returns the map's crs String, decodes the
     * reference, eg "EPSG:5070"
     *
     * @param exp
     * @param s
     *
     * @return
     *
     * @throws Exception
     */
    public static CoordinateReferenceSystem getCoordinateReferenceSystem( ExpressionOperation exp, Scope s )
            throws Exception
    {
        Object o = exp.invoke( s );
        if( o == null ) {
            return DefaultGeographicCRS.WGS84;
        }
        if( o instanceof CoordinateReferenceSystem ) {
            return (CoordinateReferenceSystem) o;
        }
        if( o instanceof MapContent ) {
            return ((MapContent) o).getCoordinateReferenceSystem();
        }
        return WKTFactory.INSTANCE.getCrs( o.toString() );
    }

    public static void setCrs( MapContent map, String crs )
            throws FactoryException
    {
        setCrs( map, CRS.decode( crs ) );
    }

    public static void setCrs( MapContent map, CoordinateReferenceSystem crs )
    {
        map.getViewport().setCoordinateReferenceSystem( crs );
    }

    public static void clipMapToCrs( MapContent map )
            throws FactoryException,
                   TransformException
    {
        clipMapToCrs( map, CRS.decode( "EPSG:4326" ) );
    }

    public static void clipMapToCrs( MapContent map, CoordinateReferenceSystem boundsCrs )
            throws TransformException,
                   FactoryException
    {
        CoordinateReferenceSystem crs = map.getCoordinateReferenceSystem();
        Extent crsExtent = crs.getDomainOfValidity();
        for( GeographicExtent element : crsExtent.getGeographicElements() ) {
            if( element instanceof GeographicBoundingBox ) {
                GeographicBoundingBox bounds = (GeographicBoundingBox) element;
                ReferencedEnvelope bbox = new ReferencedEnvelope(
                        bounds.getSouthBoundLatitude(),
                        bounds.getNorthBoundLatitude(),
                        bounds.getWestBoundLongitude(),
                        bounds.getEastBoundLongitude(),
                        boundsCrs
                );
                ReferencedEnvelope envelope = bbox.transform( crs, true );
                map.getViewport().setBounds( envelope );
            }
        }
    }

    public static void clipMap( MapContent map, double southBoundLatitude, double northBoundLatitude, double westBoundLongitude,
                                double eastBoundLongitude )
            throws TransformException,
                   FactoryException
    {
        clipMap( map, southBoundLatitude, northBoundLatitude, westBoundLongitude, eastBoundLongitude, CRS.decode( "EPSG:4326" ) );
    }

    public static void clipMap( MapContent map,
                                double southBoundLatitude, double northBoundLatitude, double westBoundLongitude,
                                double eastBoundLongitude,
                                CoordinateReferenceSystem boundsCrs )
            throws TransformException,
                   FactoryException
    {
        clipMap( map,
                 new ReferencedEnvelope( southBoundLatitude, northBoundLatitude, westBoundLongitude, eastBoundLongitude, boundsCrs ),
                 boundsCrs );
    }

    public static void clipMap( MapContent map, ReferencedEnvelope bbox, CoordinateReferenceSystem boundsCrs )
            throws TransformException,
                   FactoryException
    {
        ReferencedEnvelope envelope = bbox.transform( map.getCoordinateReferenceSystem(), true );
        map.getViewport().setBounds( envelope );
    }
}
