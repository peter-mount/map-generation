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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Grids;
import org.geotools.grid.Lines;
import org.geotools.grid.ortholine.LineOrientation;
import org.geotools.grid.ortholine.OrthoLineDef;
import uk.trainwatch.job.Scope;
import uk.trainwatch.job.lang.expr.ExpressionOperation;
import uk.trainwatch.util.Functions;

/**
 *
 * @author peter
 */
public class GridOps
{

    /**
     * Creates a square grid
     *
     * @param exp
     *
     * @return
     */
    public static ExpressionOperation createSquareGrid( ExpressionOperation exp[] )
    {
        switch( exp.length ) {
            // createSquareGrid(envelope,sideLen)
            case 2:
                return ( s, a ) -> Grids.createSquareGrid( exp[0].get( s ), exp[1].getDouble( s ) );

            // createSquareGrid(envelope,sideLen, vertexSpacing)
            case 3:
                return ( s, a ) -> Grids.createSquareGrid( exp[0].get( s ), exp[1].getDouble( s ), exp[2].getDouble( s ) );

            default:
                return null;
        }
    }

    public static ExpressionOperation createLineGrid( ExpressionOperation exp[] )
    {
        if( exp.length < 2 ) {
            return null;
        }
        return ( s, a ) -> {
            List<Object> args = ExpressionOperation.invoke( exp, s );
            return Lines.createOrthoLines(
                    (ReferencedEnvelope) args.get( 0 ),
                    args.stream()
                    .skip( 1 )
                    .map( Functions.castTo( OrthoLineDef.class ) )
                    .filter( Objects::nonNull )
                    .collect( Collectors.toList() ),
                    0.1 );
        };
    }

    private static LineOrientation getOrientation( ExpressionOperation exp, Scope s )
            throws Exception
    {
        return LineOrientation.valueOf( exp.getString( s ).toUpperCase() );
    }

    public static ExpressionOperation newOrthoLineDef( ExpressionOperation exp[] )
    {
        if( exp.length == 3 ) {
            return ( s, a ) -> new OrthoLineDef( getOrientation( exp[0], s ), exp[1].getInt( s ), exp[2].getInt( s ) );
        }
        return null;
    }
}
