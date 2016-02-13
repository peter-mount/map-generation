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
public class ColourTypeTest
{
    @Test
    public void lookup()
    {
        assertEquals(ColourType.BLACK, ColourType.lookup( "BLACK"));
        assertEquals(ColourType.BLACK, ColourType.lookup( "BLACK  "));
        assertEquals(ColourType.BLACK, ColourType.lookup( "black"));

        assertEquals(ColourType.DARK_GRAY, ColourType.lookup( "DARK_GRAY"));
        assertEquals(ColourType.DARK_GRAY, ColourType.lookup( "DARK_GRAY  "));
        assertEquals(ColourType.DARK_GRAY, ColourType.lookup( "DARK GRAY"));
        assertEquals(ColourType.DARK_GREY, ColourType.lookup( "dark grey"));
        assertEquals(ColourType.DARK_GREY, ColourType.lookup( "dark grey  "));
        assertEquals(ColourType.DARK_GRAY, ColourType.lookup( "dark GRAY"));
    }
    
}
