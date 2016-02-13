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
package onl.area51.geotools;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.geotools.styling.Mark;
import uk.trainwatch.util.MapBuilder;

/**
 * Allow default Mark's to be referenced by name
 *
 * @author peter
 */
public enum MarkType
{

    CIRCLE( "circle", () -> StyleUtils.STYLE_FACTORY.getCircleMark() ),
    CROSS( "cross", () -> StyleUtils.STYLE_FACTORY.getCrossMark() ),
    DEFAULT( "default", () -> StyleUtils.STYLE_FACTORY.getDefaultMark() ),
    SQUARE( "square", () -> StyleUtils.STYLE_FACTORY.getSquareMark() ),
    STAR( "star", () -> StyleUtils.STYLE_FACTORY.getStarMark() ),
    TRIANGLE( "triangle", () -> StyleUtils.STYLE_FACTORY.getTriangleMark() ),
    X( "x", () -> StyleUtils.STYLE_FACTORY.getXMark() );

    private static final Map<String, MarkType> TYPES = MapBuilder.<String, MarkType>builder()
            .keyMapper( k -> k == null ? "" : k.toLowerCase().trim() )
            .key( MarkType::getName )
            .addAll( values() )
            .build();

    public static MarkType lookup( String n )
    {
        return TYPES.getOrDefault( Objects.toString( n, "" ).toLowerCase().trim(), CIRCLE );
    }

    private final String name;
    private final Supplier<Mark> f;

    private MarkType( String name, Supplier<Mark> f )
    {
        this.name = name;
        this.f = f;
    }

    public String getName()
    {
        return name;
    }

    public Mark getMark()
    {
        return f.get();
    }

}
