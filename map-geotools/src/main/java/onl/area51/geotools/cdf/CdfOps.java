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
package onl.area51.geotools.cdf;

import java.util.Arrays;
import onl.area51.geotools.MiscOps;
import org.geotools.coverage.io.netcdf.NetCDFReader;
import org.geotools.factory.Hints;
import org.geotools.map.MapContent;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 *
 * @author peter
 */
public class CdfOps
{

    static ExpressionOperation cdfReader( ExpressionOperation exp[] )
    {
        switch( exp.length ) {
            case 1:
                return ( s, a ) -> new CloseableNetCDFReader( s, MiscOps.getFile( exp[0], s ) );

            case 2:
                return ( s, a ) -> {
                    MapContent map = exp[1].get( s );
                    return new CloseableNetCDFReader( s, MiscOps.getFile( exp[0], s ),
                                                      new Hints( Hints.CRS, map.getViewport().getCoordinateReferenceSystem() )
                    );
                };

            default:
                return null;
        }
    }

    static ExpressionOperation cdfGetGridCoverageNames( ExpressionOperation exp[] )
    {
        if( exp.length == 1 ) {
            return ( s, a ) -> Arrays.asList( exp[0].<NetCDFReader>get( s ).getGridCoverageNames() );
        }
        return null;
    }

    static ExpressionOperation cdfGetGrid( ExpressionOperation exp[] )
    {
        if( exp.length == 2 ) {
            return ( s, a ) -> exp[0].<NetCDFReader>get( s ).read( exp[1].getString( s ), null );
        }
        return null;
    }
}
