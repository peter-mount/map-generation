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
import org.kohsuke.MetaInfServices;
import uk.trainwatch.job.ext.Extension;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 * NetCDF and Grib file support
 *
 * @author peter
 */
@MetaInfServices(Extension.class)
public class CdfExtension
        implements Extension
{

    @Override
    public String getName()
    {
        return "NetCDF";
    }

    @Override
    public String getVersion()
    {
        return "1.0";
    }

    @Override
    public ExpressionOperation construct( String type, ExpressionOperation... exp )
    {
        switch( type ) {
            case "cdfReader":
                return CdfOps.cdfReader( exp );

            default:
                return null;
        }
    }

    @Override
    public ExpressionOperation getExpression( String name, ExpressionOperation... args )
    {
        switch( name ) {
            // Returns the grid coverage names from a reader as a list
            case "cdfGetGridCoverageNames":
                return CdfOps.cdfGetGridCoverageNames( args );

            // Get a GridCoverage2D by name
            case "cdfGetGrid":
                return CdfOps.cdfGetGrid( args );

            default:
                return null;
        }
    }

}
