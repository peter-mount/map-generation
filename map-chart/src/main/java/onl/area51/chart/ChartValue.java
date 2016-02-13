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
package onl.area51.chart;

/**
 * Object used in streams when processing charts
 *
 * @author peter
 * @param <T> Type of axis label
 */
public interface ChartValue<T>
{

    /**
     * The value at this index
     *
     * @return
     */
    double getValue();

    /**
     * The index
     *
     * @return
     */
    int getIndex();

    /**
     * The label
     *
     * @return
     */
    T getAxisLabel();

    /**
     * Create a basic instance who's label is the index
     *
     * @param index
     * @param value
     *
     * @return
     */
    static ChartValue<Integer> create( int index, double value )
    {
        return create( index, value, index );
    }

    /**
     * Create an instance
     *
     * @param <T>   type of label
     * @param index index
     * @param value value
     * @param label label
     *
     * @return
     */
    static <T> ChartValue<T> create( int index, double value, T label )
    {
        return new ChartValue<T>()
        {
            @Override
            public double getValue()
            {
                return value;
            }

            @Override
            public int getIndex()
            {
                return index;
            }

            @Override
            public T getAxisLabel()
            {
                return label;
            }
        };
    }
}
