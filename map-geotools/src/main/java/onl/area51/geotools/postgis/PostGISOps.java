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
package onl.area51.geotools.postgis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import onl.area51.geotools.MiscOps;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import static org.jaitools.CollectionFactory.map;
import uk.trainwatch.job.Scope;
import uk.trainwatch.job.lang.Statement;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 *
 * @author peter
 */
public class PostGISOps
{

    public static DataStore getDataStore( Scope s, Map params )
            throws IOException
    {
        CloseableDataStore ds = new CloseableDataStore( DataStoreFinder.getDataStore( params ) );
        s.getJob().getJobOutput().addResource( ds );
        return ds;
    }

    public static ExpressionOperation getDataStore( ExpressionOperation args[] )
    {
        switch( args.length ) {
            case 1:
                return ( s, a ) -> getDataStore( s, args[0].get( s ) );

            default:
                return null;
        }
    }

    public static FeatureSource getFeatureSource( ExpressionOperation op, ExpressionOperation nameOp, Scope s )
            throws Exception
    {
        DataStore ds;
        Object o = op.invoke( s );

        if( o instanceof DataStore ) {
            ds = (DataStore) o;
        }
        else if( o instanceof Map ) {
            ds = getDataStore( s, (Map) o );
        }
        else {
            throw new UnsupportedOperationException( "Unsupported: " + o );
        }

        return ds.getFeatureSource( nameOp.getString( s ) );
    }

    public static Statement importGrib( ExpressionOperation args[] )
    {
        switch( args.length ) {
            case 2:
                return ( s, a ) -> {
                    DataStore ds = args[0].get( s );
                    Path path = MiscOps.getPath( args[1], s );
                };

            default:
                return null;
        }
    }
}
