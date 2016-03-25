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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import uk.trainwatch.job.lang.expr.ExpressionOperation;
import uk.trainwatch.util.MapBuilder;

/**
 *
 * @author peter
 */
public class Grid2DOps
{

    static ExpressionOperation getGridCoverageLayer( ExpressionOperation exp[] )
    {
        switch( exp.length ) {
            // Coverage layer as is
            case 2:
                return ( s, a ) -> new GridCoverageLayer( exp[0].get( s ), exp[1].get( s ) );

            // Reproject to map or other crs
            case 3:
                return ( s, a ) -> {
                    Object o = exp[0].get( s );
                    GridCoverage2D gridCoverage = exp[1].get( s );
                    Style style = exp[2].get( s );

                    CoordinateReferenceSystem crs = null;
                    if( o instanceof CoordinateReferenceSystem ) {
                        crs = (CoordinateReferenceSystem) o;
                    }
                    else if( o instanceof MapContent ) {
                        MapContent mapContent = (MapContent) o;
                        MapViewport mapViewport = mapContent.getViewport();
                        crs = mapViewport.getCoordinateReferenceSystem();
                    }
                    else if( o instanceof String ) {
                        crs = WKTFactory.INSTANCE.getCrs( o.toString() );
                    }

                    SimpleFeatureCollection feature = FeatureUtilities.wrapGridCoverage( gridCoverage );

                    return new FeatureLayer( feature, style );
                };

            default:
                return null;
        }
    }

    /**
     * Some GridCoverage2D cover range 0...360 longitude rather than -180..180 so this fixes that
     *
     * @param gridCoverage
     * @param west
     *
     * @return
     */
    public static GridCoverage2D correctGrid( final GridCoverage2D gridCoverage, final boolean west )
    {
        Envelope env = gridCoverage.getEnvelope();
        double[] lc = env.getLowerCorner().getCoordinate();
        double[] uc = env.getUpperCorner().getCoordinate();
        Envelope newEnv = west
                          ? new ReferencedEnvelope( lc[0] - 360, uc[0] - 360, lc[1], uc[1], DefaultGeographicCRS.WGS84 )
                          : new ReferencedEnvelope( lc[0], uc[0], 180, uc[1], DefaultGeographicCRS.WGS84 );
        RenderedImage img = gridCoverage.getRenderedImage();

        return new GridCoverageFactory( null ).create( "test", img, newEnv );
    }

    private static final Logger LOG = Logger.getGlobal();

    public static File toShape( GridCoverage2D gridCoverage, File file )
            throws SchemaException,
                   TransformException,
                   IOException
    {
        LOG.log( Level.INFO, () -> "Creating " + file.getAbsolutePath() );

        final SimpleFeatureType TYPE = DataUtilities.createType( "Location",
                                                                 // <- the geometry attribute: Point type
                                                                 "the_geom:Point:srid=4326,"
                                                                 // <- a String attribute
                                                                 //+ "name:String,"
                                                                 // a number attribute
                                                                 + "number:Integer"
        );

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder( TYPE );

        MathTransform2D transform = gridCoverage.getGridGeometry().getGridToCRS2D();

        RenderedImage rimg = gridCoverage.getRenderedImage();
        BufferedImage img = new BufferedImage( rimg.getColorModel(), rimg.copyData( null ), false, null );

        Point2D src = new Point2D.Double();
        Point2D dst = new Point2D.Double();

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> map = MapBuilder.<String, Serializable>builder()
                .add( "url", file.toURI().toURL() )
                .add( "create spatial index", true )
                .build();

        ShapefileDataStore dataStore = (ShapefileDataStore) dataStoreFactory.createDataStore( map );

        dataStore.createSchema( TYPE );

        try( Transaction transaction = new DefaultTransaction( "create" ) ) {
            String typeName = dataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = dataStore.getFeatureSource( typeName );
            SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();

            if( !(featureSource instanceof SimpleFeatureStore) ) {
                throw new UnsupportedOperationException( "Featrue source not a SimpleFeatureStore" );
            }

            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            List<SimpleFeature> features = new ArrayList<>();

            int dy = Math.max( 1, img.getHeight() / 100 );
            LOG.log( Level.INFO, () -> "Creating " + file.getAbsolutePath() );
            for( int y = 0; y < img.getHeight(); y++ ) {
                if( (y % dy) == 0 ) {
                    LOG.log( Level.INFO, "Completed {0}% {1}/{2}", new Object[]{100 * y / img.getHeight(), y, img.getHeight()} );
                }

                // One feature per scan line
                features.clear();

                for( int x = 0; x < img.getWidth(); x++ ) {
                    src.setLocation( img.getMinX() + x, img.getMinY() + y );
                    transform.transform( src, dst );

                    Point p = geometryFactory.createPoint( new Coordinate( dst.getX(), dst.getY() ) );
                    featureBuilder.add( p );
                    featureBuilder.add( img.getRGB( img.getMinX() + x, img.getMinY() + y ) );

                    features.add( featureBuilder.buildFeature( null ) );
                }

                featureStore.addFeatures( new ListFeatureCollection( TYPE, features ) );
            }

            LOG.log( Level.INFO, () -> "Completed " + img.getHeight() + " rows" );
        }

        return file;
    }
}
