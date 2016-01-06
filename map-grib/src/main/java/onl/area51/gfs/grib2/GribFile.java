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
import ucar.unidata.io.RandomAccessFile;

/**
 *
 * @author peter
 */
public class GribFile
        extends RandomAccessFile
{

    public GribFile( File file, String mode )
            throws IOException
    {
        this( new java.io.RandomAccessFile( file, mode ) );
    }

    public GribFile( java.io.RandomAccessFile file )
            throws IOException
    {
        super( defaultBufferSize );
        this.file = file;
        readonly = false;
    }

    public GribFile( int bufferSize )
            throws IOException
    {
        super( bufferSize );
    }

    public GribFile( String location, String mode )
            throws IOException
    {
        super( location, mode );
    }

    public GribFile( String location, String mode, int bufferSize )
            throws IOException
    {
        super( location, mode, bufferSize );
    }

}
