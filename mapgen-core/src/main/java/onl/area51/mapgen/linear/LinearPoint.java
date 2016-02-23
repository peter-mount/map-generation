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

import java.util.function.Function;

/**
 * A linear point
 *
 * @author peter
 * @param <X> type of axis
 */
public interface LinearPoint<X>
{

    /**
     * The position of this point on the axis
     *
     * @return x
     */
    X getX();

    /**
     * The value at this point
     *
     * @return value or NaN if not available
     */
    default double getValue()
    {
        return Double.NaN;
    }

    /**
     * The Linear instance this point belongs to
     *
     * @return
     */
    Linear getLinear();

    /**
     * Return a point
     *
     * @param <X>    Type of axis
     * @param x      position on axis
     * @param linear Linear instance this point belongs to
     * @return instance
     */
    static <X> LinearPoint create( X x, Linear linear )
    {
        return new LinearPoint<X>()
        {
            @Override
            public X getX()
            {
                return x;
            }

            @Override
            public Linear getLinear()
            {
                return linear;
            }
        };
    }

    /**
     * Return a point
     *
     * @param <X>    Type of axis
     * @param x      position on axis
     * @param value  value of this point
     * @param linear Linear instance this point belongs to
     * @return instance
     */
    static <X> LinearPoint create( X x, double value, Linear linear )
    {
        return new LinearPoint<X>()
        {
            @Override
            public X getX()
            {
                return x;
            }

            @Override
            public double getValue()
            {
                return value;
            }

            @Override
            public Linear getLinear()
            {
                return linear;
            }
        };
    }

    /**
     * Return a point
     *
     * @param <X>      Type of axis
     * @param x        position on axis
     * @param function the function that will supply the value of this point
     * @param linear   Linear instance this point belongs to
     * @return instance
     */
    static <X> LinearPoint create( X x, Function<X, Double> function, Linear linear )
    {
        return new LinearPoint<X>()
        {
            @Override
            public X getX()
            {
                return x;
            }

            @Override
            public double getValue()
            {
                return function.apply( x );
            }

            @Override
            public Linear getLinear()
            {
                return linear;
            }
        };
    }
}
