/*
 * Copyright 2015 peter.
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
package onl.area51.mapgen.vector;

import java.awt.Color;
import uk.trainwatch.gis.Coordinate;

/**
 * A point element on a map
 * <p>
 * @author peter
 */
public interface Point
        extends Element
{

    static Point create( Coordinate coord, String label, Color col )
    {
        return new DefaultPoint( coord, label, col );
    }

    static Point create( Coordinate coord, Color col )
    {
        return new DefaultPoint( coord, null, col );
    }

    static Point create( Coordinate coord, String label )
    {
        return new DefaultPoint( coord, label, Color.BLACK );
    }

    /**
     * The coordinate of this point
     * <p>
     * @return
     */
    Coordinate getCoordinate();

    /**
     * The label
     * <p>
     * @return
     */
    String getLabel();

    /**
     * The colour
     * <p>
     * @return
     */
    Color getColour();
}
