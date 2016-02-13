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

/**
 *
 * @author peter
 */
public class TextAlignmentTest
{

    /**
     * Test of valueOfWithDefault method, of class TextAlignment.
     */
    @Test
    public void valueOfWithDefault_String()
    {
        assertEquals( TextAlignment.LEFT, TextAlignment.valueOfWithDefault( "LEFT", null ) );
        assertEquals( TextAlignment.LEFT, TextAlignment.valueOfWithDefault( "left", null ) );

        assertEquals( TextAlignment.CENTER, TextAlignment.valueOfWithDefault( "CENTER", null ) );
        assertEquals( TextAlignment.CENTER, TextAlignment.valueOfWithDefault( "center", null ) );

        assertEquals( TextAlignment.RIGHT, TextAlignment.valueOfWithDefault( "RIGHT", null ) );
        assertEquals( TextAlignment.RIGHT, TextAlignment.valueOfWithDefault( "right", null ) );
    }

}
