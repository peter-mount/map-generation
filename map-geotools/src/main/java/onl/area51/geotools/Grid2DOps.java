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

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

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
                    ReferencedEnvelope env = exp[0].<MapContent>get( s ).getViewport().getBounds();

                    GridCoverage2D coverage = exp[1].get( s );
                    Style style = exp[2].get( s );

                    SimpleFeatureCollection feature = FeatureUtilities.wrapGridCoverage( coverage );
                    return new FeatureLayer(feature, style );
                };

            default:
                return null;
        }
    }

}
