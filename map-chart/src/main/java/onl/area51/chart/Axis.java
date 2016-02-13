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

import java.util.Objects;
import java.util.function.DoubleFunction;

/**
 *
 * @author peter
 */
public interface Axis
{

    ChartLayer getChartLayer();

    Position getPosition();

    double getMinor();

    default boolean isMinor()
    {
        return getMinor() != Double.NaN;
    }

    double getMajor();

    default boolean isMajor()
    {
        return getMajor() != Double.NaN;
    }

    DoubleFunction<String> getLabel();

    String getTitle();

    default boolean isTitle()
    {
        return getTitle() != null && !getTitle().isEmpty();
    }

    /**
     *
     * @param a
     * @param ma major tick size
     * @param mi minor tick size
     * @param fs font size
     *
     * @return
     */
    static int adjustInset( Axis a, int ma, int mi, int fs )
    {
        int d = 1;
        if( a != null ) {
            if( a.isMajor() ) {
                d = ma + 2;
            }
            else if( a.isMinor() ) {
                d = mi + 2;
            }
            if( a.getLabel()!=null ) {
                d += fs;
            }
            if( a.isTitle() ) {
                d += fs;
            }
        }
        return d;
    }

    static enum Position
    {
        LEFT,
        RIGHT,
        X
    }

    static interface Builder
    {

        Builder setChartLayer( ChartLayer l );

        Builder setPosition( Position p );

        Builder setMajor( double v );

        Builder setMinor( double v );

        Builder setTitle( String s );

        Builder setLabel( DoubleFunction<String> f );

        Axis build();
    }

    static Builder builder()
    {
        return new Builder()
        {
            ChartLayer layer;
            Position position;
            double major = Double.NaN;
            double minor = Double.NaN;
            String title;
            DoubleFunction<String> f;

            @Override
            public Builder setChartLayer( ChartLayer l )
            {
                layer = l;
                return this;
            }

            @Override
            public Builder setPosition( Position p )
            {
                position = p;
                return this;
            }

            @Override
            public Builder setMajor( double v )
            {
                major = v;
                return this;
            }

            @Override
            public Builder setMinor( double v )
            {
                minor = v;
                return this;
            }

            @Override
            public Builder setTitle( String s )
            {
                title = s;
                return this;
            }

            @Override
            public Builder setLabel( DoubleFunction<String> f )
            {
                this.f = f;
                return this;
            }

            @Override
            public Axis build()
            {
                Objects.requireNonNull( layer );
                Objects.requireNonNull( position );

                if( f == null ) {
                    f = String::valueOf;
                }

                return new Axis()
                {
                    @Override
                    public ChartLayer getChartLayer()
                    {
                        return layer;
                    }

                    @Override
                    public Position getPosition()
                    {
                        return position;
                    }

                    @Override
                    public double getMinor()
                    {
                        return minor;
                    }

                    @Override
                    public double getMajor()
                    {
                        return major;
                    }

                    @Override
                    public DoubleFunction<String> getLabel()
                    {
                        return f;
                    }

                    @Override
                    public String getTitle()
                    {
                        return title;
                    }

                    @Override
                    public int hashCode()
                    {
                        return position.hashCode();
                    }

                    @Override
                    public boolean equals( Object obj )
                    {
                        return obj instanceof Axis && ((Axis) obj).getPosition() == position;
                    }

                };
            }
        };
    }
}
