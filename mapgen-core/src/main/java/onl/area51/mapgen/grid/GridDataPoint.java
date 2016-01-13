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
package onl.area51.mapgen.grid;

import java.util.function.Function;

/**
 * A point within a grid with a value
 *
 * @author peter
 */
public interface GridDataPoint
        extends GridPoint
{

    double getValue();

    /**
     *
     * @param p GridPoint
     * @param v value at this point
     *
     * @return
     */
    static GridDataPoint of( GridPoint p, double v )
    {
        return new GridDataPoint()
        {
            @Override
            public double getValue()
            {
                return v;
            }

            @Override
            public int getZ()
            {
                return p.getZ();
            }

            @Override
            public int getX()
            {
                return p.getX();
            }

            @Override
            public int getY()
            {
                return p.getY();
            }
        };
    }

    /**
     *
     * @param p GridPoint
     * @param v Mapping function of GridPoint to Value
     *
     * @return
     */
    static GridDataPoint of( GridPoint p, Function<GridPoint, Double> v )
    {
        return new GridDataPoint()
        {
            @Override
            public double getValue()
            {
                return v.apply( this );
            }

            @Override
            public int getZ()
            {
                return p.getZ();
            }

            @Override
            public int getX()
            {
                return p.getX();
            }

            @Override
            public int getY()
            {
                return p.getY();
            }
        };
    }

    /**
     *
     * @param z zoom
     * @param x x
     * @param y y
     * @param v value at this point
     *
     * @return
     */
    static GridDataPoint of( int z, int x, int y, double v )
    {
        return new GridDataPoint()
        {
            @Override
            public int getZ()
            {
                return z;
            }

            @Override
            public int getX()
            {
                return x;
            }

            @Override
            public int getY()
            {
                return y;
            }

            @Override
            public double getValue()
            {
                return v;
            }

        };
    }
}
