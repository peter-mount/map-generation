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
import onl.area51.geotools.postgis.PostGISOps;
import org.geotools.styling.SLD;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.job.ext.Extension;
import uk.trainwatch.job.lang.Statement;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 * JobControl extension to support Geotools
 *
 * @author peter
 */
@MetaInfServices(Extension.class)
public class GeotoolsExtension
        implements Extension
{

    @Override
    public String getName()
    {
        return "Geotools";
    }

    @Override
    public String getVersion()
    {
        return "13.5";
    }

    @Override
    public ExpressionOperation construct( String type, ExpressionOperation... exp )
    {
        switch( type ) {

            case "featureLayer":
                return FeatureOps.getFeatureLayer( exp );

            case "gridCoverageLayer":
                return Grid2DOps.getGridCoverageLayer( exp );

            case "mapContent":
                return FeatureOps.getMapContent( exp );

            case "orthoLineDef":
                return GridOps.newOrthoLineDef( exp );

            default:
                return null;
        }
    }

    @Override
    public ExpressionOperation getExpression( String name, ExpressionOperation... args )
    {
        switch( name ) {

            case "grid2shape":
                switch( args.length ) {
                    case 1:
                        return ( s, a ) -> Grid2DOps.toShape( args[0].get( s ), File.createTempFile( "grid", ".shp" ) );
                    case 2:
                        return ( s, a ) -> Grid2DOps.toShape( args[0].get( s ), MiscOps.getFile( args[1], s ) );
                    default:
                        return null;
                }

            case "correctGridCoverageHemisphere":
                switch( args.length ) {
                    case 1:
                        return ( s, a ) -> Grid2DOps.correctGrid( args[0].get( s ), true );
                    case 2:
                        return ( s, a ) -> Grid2DOps.correctGrid( args[0].get( s ), args[1].isTrue( s ) );
                    default:
                        return null;
                }

            case "getCoordinateReferenceSystem":
                switch( args.length ) {
                    case 1:
                        return ( s, a ) -> CrsOps.getCoordinateReferenceSystem( args[0], s );
                    default:
                        return null;
                }

            case "createReferencedEnvelope":
                return FeatureOps.createReferencedEnvelope( args );

            case "getDataStore":
                return PostGISOps.getDataStore( args );

            case "createFill":
                return StyleOps.createFill( args );

            case "createGreyscaleStyle":
                return StyleOps.createGreyscaleStyle( args );

            case "createLineGrid":
                return GridOps.createLineGrid( args );

            case "createLineStyle":
                return StyleOps.createLineStyle( args );

            case "createMapImage":
                return RenderOps.createMapImage( args );

            case "createPointStyle":
                return StyleOps.createPointStyle( args );

            case "createPolygonStyle":
                return StyleOps.createPolygonStyle( args );

            case "createSimpleStyle":
                switch( args.length ) {
                    case 1:
                        return ( s, a ) -> SLD.createSimpleStyle( FeatureOps.getFeatureType( args[0], s ) );
                    case 2:
                        return ( s, a ) -> SLD.createSimpleStyle( FeatureOps.getFeatureType( args[0], s ), MiscOps.getColor( args[1], s ) );
                    default:
                        return null;
                }

            case "createSquareGrid":
                return GridOps.createSquareGrid( args );

            case "createStroke":
                return StyleOps.createStroke( args );

            case "getFeatureReader":
                return ( s, a ) -> MiscOps.getFileDataStore( args[0], s ).getFeatureReader();

            case "getFeatureSource":
                switch( args.length ) {
                    case 1:
                        return ( s, a ) -> MiscOps.getFileDataStore( args[0], s ).getFeatureSource();

                    case 2:
                        return ( s, a ) -> PostGISOps.getFeatureSource( args[0], args[1], s );

                    default:
                        return null;
                }

            case "getMapBounds":
                return RenderOps.getMapBounds( args );
            default:
                break;
        }

        return null;
    }

    @Override
    public Statement getStatement( String name, ExpressionOperation... args )
    {
        switch( name ) {
            case "renderMapContent":
                return RenderOps.renderMap( args );

            case "setMapBounds":
                return FeatureOps.setMapBounds( args );

            case "setMapProjection":
                return FeatureOps.setMapProjection( args );

            default:
                return null;
        }
    }

}
