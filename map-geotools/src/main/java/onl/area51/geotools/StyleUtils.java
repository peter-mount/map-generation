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

import java.awt.Color;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.FilterFactory;

/**
 * A collection of utilities for creating stiles
 *
 * @author peter
 */
public class StyleUtils
{

    public static final StyleFactory STYLE_FACTORY = CommonFactoryFinder.getStyleFactory();
    public static final FilterFactory FILTER_FACTORY = CommonFactoryFinder.getFilterFactory();

    public static Stroke createStroke( Color c, double width )
    {
        return STYLE_FACTORY.createStroke( FILTER_FACTORY.literal( c ), FILTER_FACTORY.literal( width ) );
    }

    public static Stroke createStroke( Color c, double width, double opacity )
    {
        return STYLE_FACTORY.createStroke( FILTER_FACTORY.literal( c ), FILTER_FACTORY.literal( width ), FILTER_FACTORY.literal( opacity ) );
    }

    public static Fill createFill( Color c, double opacity )
    {
        return STYLE_FACTORY.createFill( FILTER_FACTORY.literal( c ), FILTER_FACTORY.literal( opacity ) );
    }

    public static Style createStyle( Symbolizer sym )
    {
        Rule rule = STYLE_FACTORY.createRule();
        rule.symbolizers().add( sym );
        FeatureTypeStyle fts = STYLE_FACTORY.createFeatureTypeStyle( new Rule[]{rule} );
        Style style = STYLE_FACTORY.createStyle();
        style.featureTypeStyles().add( fts );

        return style;
    }

    public static Style createPolygonStyle( Stroke s, Fill f )
    {
        return createStyle( STYLE_FACTORY.createPolygonSymbolizer( s, f, null ) );

    }

    public static Style createLineStyle( Stroke s )
    {
        return createStyle( STYLE_FACTORY.createLineSymbolizer( s, null ) );
    }

    public static Style createPointStyle( MarkType t, double size, Stroke s, Fill f )
    {
        Graphic gr = STYLE_FACTORY.createDefaultGraphic();

        Mark mark = t.getMark();
        mark.setStroke( s );
        mark.setFill( f );

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add( mark );
        gr.setSize( FILTER_FACTORY.literal( size ) );
        return createPointStyle( gr );
    }

    public static Style createPointStyle( Graphic gr )
    {
        return createStyle( STYLE_FACTORY.createPointSymbolizer( gr, null ) );
    }
}
