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
package onl.area51.gfs.grib2;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import ucar.grib.grib2.Grib2Record;

public class Grib2Test
{

    private static final File file = new File( "/home/peter/gfs.grib" );

    /*
    @Test
    public void testReadGrib()
            throws IOException
    {
        Grib2 in = new Grib2( new GribFile( file, "r" ) );

        in.forEachRecord( ( i, r ) -> {
            System.out.println( Grib2.toJson( i, r ).build() );
        } );

        fail( "The test case is a prototype." );
    }

    @Test
    public void findRecords()
            throws IOException
    {
        Grib2 in = new Grib2( new GribFile( file, "r" ) );

        // 0,0,0 happens to be Temperature
        Collection<Grib2Record> records = in.records()
                .filter( Grib2Filters.filterProduct( 0, 0, 0 ) )
                .collect( Collectors.toList() );
        assertFalse( records.isEmpty() );

        records.forEach( r -> System.out.printf( "{%d,%f} %s\n",
                                                 r.getPDS().getPdsVars().getLevelType1(),
                                                 r.getPDS().getPdsVars().getLevelValue1(),
                                                 Grib2.toJson( 0, r ).build()
        ) );
        fail( "The test case is a prototype." );
    }
     */
}
