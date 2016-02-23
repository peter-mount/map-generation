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
package onl.area51.mapgen.linear;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 *
 * @author peter
 */
public class LinearSupport
{

    public static void forEach( int min, int max, IntConsumer c )
    {
        if( min > max )
        {
            forEach( max, min, c );
        }
        else
        {
            IntStream.range( min, max ).forEach( c );
        }
    }

    public static void forEach( double min, double max, double s, DoubleConsumer c )
    {
        if( min > max )
        {
            forEach( max, min, s, c );
        }
        else
        {
            for( double x = min; x <= max; x += s )
            {
                c.accept( x );
            }
        }
    }
}
