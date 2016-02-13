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

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A source of data for a chart
 *
 * @author peter
 * @param <T> type of label
 */
public interface ChartDataSource<T>
{

    /**
     * The statistics for this data set
     *
     * @return
     */
    DoubleSummaryStatistics getStatistics();

    /**
     * The number of entries in this set, should match {@link DoubleSummaryStatistics#getCount() } but can be better performance wise
     *
     * @return
     */
    int getSize();

    /**
     * The value at a specific index
     *
     * @param index
     *
     * @return
     */
    double getValue( int index );

    /**
     * The label at the specified index
     *
     * @param index
     *
     * @return
     */
    T getLabel( int index );

    /**
     * Pass each value to a consumer
     *
     * @param action
     */
    void forEach( DoubleConsumer action );

    /**
     * Pass each value to a consumer
     *
     * @param action
     */
    void forEach( BiConsumer<Integer, Double> action );

    /**
     * A stream of the underlying data
     *
     * @return
     */
    Stream<ChartValue<T>> stream();

    /**
     * A basic data source of a double array
     *
     * @param data
     *
     * @return
     */
    static ChartDataSource<Integer> create( double... data )
    {
        return create( data.length, i -> data[i], i -> i );
    }

    /**
     * A basic data source of a double array
     *
     * @param <T>   Type of label
     * @param label function to translate index to label
     * @param data  data
     *
     * @return
     */
    static <T> ChartDataSource<T> create( IntFunction<T> label, double... data )
    {
        return create( data.length, i -> data[i], label );
    }

    /**
     * Creates a basic data source over a list
     *
     * @param data
     *
     * @return
     */
    static ChartDataSource<Integer> create( List<Double> data )
    {
        return create( data, i -> i );
    }

    /**
     * Creates a basic data source over a list
     *
     * @param <T>
     * @param data
     * @param label
     *
     * @return
     */
    static <T> ChartDataSource<T> create( List<Double> data, IntFunction<T> label )
    {
        int size = data.size();
        return create( size, data::get, label );
    }

    /**
     * Create a new instance
     *
     * @param <Integer>
     * @param size   Number of entries in this source
     * @param values function to map index to value
     *
     * @return
     */
    static <Integer> ChartDataSource<Integer> create( int size, IntToDoubleFunction values )
    {
        return new DefaultChartDataSource( size, values, i -> i );
    }

    /**
     * Create a new instance
     *
     * @param <T>    Type of data
     * @param size   Number of entries in this source
     * @param values function to map index to value
     * @param labels function to get labels
     *
     * @return
     */
    static <T> ChartDataSource<T> create( int size, IntToDoubleFunction values, IntFunction<T> labels )
    {
        return new DefaultChartDataSource( size, values, labels );
    }

    /**
     * Create a new instance
     *
     * @param <T>           Type of data
     * @param size          Number of entries in this source
     * @param values        function to map index to value
     * @param labels        function to get labels
     * @param statsSupplier Supply the statistics
     *
     * @return
     */
    static <T> ChartDataSource<T> create( int size, IntToDoubleFunction values, IntFunction<T> labels, Supplier<DoubleSummaryStatistics> statsSupplier )
    {
        return new DefaultChartDataSource( size, values, labels, statsSupplier );
    }
}
