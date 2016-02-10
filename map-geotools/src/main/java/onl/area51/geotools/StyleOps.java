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
import org.geotools.styling.Fill;
import org.geotools.styling.Stroke;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 *
 * @author peter
 */
public class StyleOps
{

    /**
     * createStroke()
     * createStroke(Colour)
     * createStroke(Colour,width)
     * createStroke(Colour,width,opacity)
     *
     * @param args
     *
     * @return
     */
    public static ExpressionOperation createStroke( ExpressionOperation args[] )
    {
        switch( args.length ) {
            case 0:
                return ( s, a ) -> StyleUtils.createStroke( Color.BLACK, 1 );

            case 1:
                return ( s, a ) -> StyleUtils.createStroke( MiscOps.getColor( args[0], s ), 1 );

            case 2:
                return ( s, a ) -> StyleUtils.createStroke( MiscOps.getColor( args[0], s ), args[1].getDouble( s ) );

            case 3:
                return ( s, a ) -> StyleUtils.createStroke( MiscOps.getColor( args[0], s ), args[1].getDouble( s ), args[2].getDouble( s ) );

            default:
                return null;
        }
    }

    /**
     * createFill();
     * createFill(Colour);
     * createFill(Colour,opacity);
     *
     * @param args
     *
     * @return
     */
    public static ExpressionOperation createFill( ExpressionOperation args[] )
    {
        switch( args.length ) {
            case 0:
                return ( s, a ) -> StyleUtils.createFill( Color.WHITE, 0.5 );

            case 1:
                return ( s, a ) -> StyleUtils.createFill( MiscOps.getColor( args[0], s ), 0.5 );

            case 2:
                return ( s, a ) -> StyleUtils.createFill( MiscOps.getColor( args[0], s ), args[1].getDouble( s ) );

            default:
                return null;
        }
    }

    public static ExpressionOperation createPolygonStyle( ExpressionOperation args[] )
    {
        switch( args.length ) {
            case 0:
                return ( s, a ) -> StyleUtils.createPolygonStyle( StyleUtils.createStroke( Color.BLACK, 1 ), StyleUtils.createFill( Color.WHITE, 0.5 ) );

            case 1:
                return ( s, a ) -> StyleUtils.createPolygonStyle( args[0].get( s ), StyleUtils.createFill( Color.WHITE, 0.5 ) );

            case 2:
                return ( s, a ) -> StyleUtils.createPolygonStyle( args[0].get( s ), args[1].get( s ) );

            default:
                return null;
        }
    }

    public static ExpressionOperation createLineStyle( ExpressionOperation args[] )
    {
        switch( args.length ) {
            case 0:
                return ( s, a ) -> StyleUtils.createLineStyle( StyleUtils.createStroke( Color.BLACK, 1 ) );

            case 1:
                return ( s, a ) -> StyleUtils.createLineStyle( args[0].get( s ) );

            default:
                return null;
        }
    }

    public static ExpressionOperation createPointStyle( ExpressionOperation args[] )
    {
        int l = args.length;

        return ( s, a ) -> {
            MarkType t = l < 1 ? MarkType.CIRCLE : MarkType.lookup( args[0].getString( s ) );
            double width = l < 2 ? 5 : args[1].getDouble( s );
            Stroke stroke = l < 3 ? StyleUtils.createStroke( Color.BLACK, 1 ) : args[2].get( s );
            Fill fill = l < 4 ? StyleUtils.createFill( Color.WHITE, 0.5 ) : args[2].get( s );;
            return StyleUtils.createPointStyle( t, width, stroke, fill );
        };
    }
}
