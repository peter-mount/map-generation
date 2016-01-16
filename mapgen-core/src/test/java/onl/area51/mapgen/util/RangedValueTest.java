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
package onl.area51.mapgen.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author peter
 */
public class RangedValueTest
{

    private RangedValue v;

    @Before
    public void before()
    {
        // We want integers from 0 to 10 inclusive... hence size=11
        v = RangedValue.create( 0, 10, 11 );
    }

    /**
     * Test that getValue() returns the correct value
     */
    @Test
    public void getValue()
    {
        for( int i = 0; i < 10; i++ ) {
            assertEquals( i, v.getValue( i ), 0.001 );
        }
    }

    /**
     * Test getIndex() on each value returns the correct index
     */
    @Test
    public void getIndex()
    {
        int idx = 0;
        for( double d = 0.0; idx < v.size(); d += 1.0, idx++ ) {
            assertEquals( idx, v.getIndex( d ) );
        }
    }

    /**
     * Test getIndex() returns the correct index for a value between index points
     */
    @Test
    public void getIndex2()
    {
        // < min
        assertEquals( 0, v.getIndex( -347 ) );

        // Should return the index of the value below it
        assertEquals( 3, v.getIndex( 3.1415 ) );
        assertEquals( 4, v.getIndex( 4.5 ) );
        assertEquals( 7, v.getIndex( 7.23 ) );

        // > max
        assertEquals( 10, v.getIndex( Double.MAX_VALUE ) );
    }

}
