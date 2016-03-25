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

import java.io.IOException;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 *
 * @author peter
 */
public class CloseableDataStore
        implements AutoCloseable,DataStore
{
    private final DataStore ds;

    public CloseableDataStore( DataStore ds )
    {
        this.ds = ds;
    }

    @Override
    public void close()
    {
        dispose();
    }

    
    @Override
    public void updateSchema( String typeName, SimpleFeatureType featureType )
            throws IOException
    {
        ds.updateSchema( typeName, featureType );
    }

    @Override
    public void removeSchema( String typeName )
            throws IOException
    {
        ds.removeSchema( typeName );
    }

    @Override
    public String[] getTypeNames()
            throws IOException
    {
        return ds.getTypeNames();
    }

    @Override
    public SimpleFeatureType getSchema( String typeName )
            throws IOException
    {
        return ds.getSchema( typeName );
    }

    @Override
    public SimpleFeatureSource getFeatureSource( String typeName )
            throws IOException
    {
        return ds.getFeatureSource( typeName );
    }

    @Override
    public SimpleFeatureSource getFeatureSource( Name typeName )
            throws IOException
    {
        return ds.getFeatureSource( typeName );
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader( Query query, Transaction transaction )
            throws IOException
    {
        return ds.getFeatureReader( query, transaction );
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter( String typeName, Filter filter, Transaction transaction )
            throws IOException
    {
        return ds.getFeatureWriter( typeName, filter, transaction );
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter( String typeName, Transaction transaction )
            throws IOException
    {
        return ds.getFeatureWriter( typeName, transaction );
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend( String typeName, Transaction transaction )
            throws IOException
    {
        return ds.getFeatureWriterAppend( typeName, transaction );
    }

    @Override
    public LockingManager getLockingManager()
    {
        return ds.getLockingManager();
    }

    @Override
    public ServiceInfo getInfo()
    {
        return ds.getInfo();
    }

    @Override
    public void createSchema( SimpleFeatureType featureType )
            throws IOException
    {
        ds.createSchema( featureType );
    }

    @Override
    public void updateSchema( Name typeName, SimpleFeatureType featureType )
            throws IOException
    {
        ds.updateSchema( typeName, featureType );
    }

    @Override
    public void removeSchema( Name typeName )
            throws IOException
    {
        ds.removeSchema( typeName );
    }

    @Override
    public List<Name> getNames()
            throws IOException
    {
        return ds.getNames();
    }

    @Override
    public SimpleFeatureType getSchema( Name name )
            throws IOException
    {
        return ds.getSchema( name );
    }

    @Override
    public void dispose()
    {
        ds.dispose();
    }
    
    
}
