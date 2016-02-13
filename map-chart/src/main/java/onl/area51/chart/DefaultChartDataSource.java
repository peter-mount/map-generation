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
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author peter
 */
public class DefaultChartDataSource<T>
        implements ChartDataSource<T>
{

    private final int size;
    private final IntToDoubleFunction values;
    private final IntFunction<T> labels;
    private final Supplier<DoubleSummaryStatistics> statsSupplier;
    private transient volatile DoubleSummaryStatistics stats;

    /**
     * Create a new instance
     *
     * @param size   Number of entries in this source
     * @param values function to map index to value
     * @param labels function to get labels
     */
    public DefaultChartDataSource( int size, IntToDoubleFunction values, IntFunction<T> labels )
    {
        this( size, values, labels, () -> IntStream.range( 0, size ).mapToDouble( values ).summaryStatistics() );
    }

    /**
     * Create a new instance
     *
     * @param size          Number of entries in this source
     * @param values        function to map index to value
     * @param labels        function to get labels
     * @param statsSupplier Supply the statistics
     */
    public DefaultChartDataSource( int size, IntToDoubleFunction values, IntFunction<T> labels, Supplier<DoubleSummaryStatistics> statsSupplier )
    {
        this.size = size;
        this.values = values;
        this.labels = labels;
        this.statsSupplier = statsSupplier;
    }

    @Override
    public synchronized DoubleSummaryStatistics getStatistics()
    {
        if( stats == null ) {
            stats = Objects.requireNonNull( statsSupplier.get() );
        }
        return stats;
    }

    @Override
    public int getSize()
    {
        return size;
    }

    @Override
    public double getValue( int index )
    {
        return values.applyAsDouble( index );
    }

    @Override
    public T getLabel( int index )
    {
        return labels.apply( index );
    }

    @Override
    public void forEach( DoubleConsumer action )
    {
        for( int i = 0; i < size; i++ ) {
            action.accept( values.applyAsDouble( i ) );
        }
    }

    @Override
    public void forEach( BiConsumer<Integer, Double> action )
    {
        for( int i = 0; i < size; i++ ) {
            action.accept( i, values.applyAsDouble( i ) );
        }
    }

    @Override
    public Stream<ChartValue<T>> stream()
    {
        return IntStream.range( 0, size )
                .mapToObj( i -> ChartValue.create( size, values.applyAsDouble( i ), labels.apply( i ) ) );
    }

}
