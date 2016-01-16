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
package onl.area51.mapgen.job;

import java.awt.Color;
import java.util.Collection;
import onl.area51.mapgen.contour.Contour;
import onl.area51.mapgen.util.ColorMap;
import onl.area51.mapgen.util.ColourType;
import onl.area51.mapgen.util.RangedColorMap;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.job.ext.Extension;
import uk.trainwatch.job.lang.expr.ExpressionOperation;

/**
 *
 * @author peter
 */
@MetaInfServices(Extension.class)
public class ContourExtension
        implements Extension
{

    @Override
    public String getName()
    {
        return "ContourExtension";
    }

    @Override
    public String getVersion()
    {
        return "1.0";
    }

    @Override
    public ExpressionOperation construct( String type, ExpressionOperation... exp )
    {
        if( "colourMap".equals( type ) || "colorMap".equals( type ) ) {
            return constructColourMap( exp );
        }
        else {
            return constructImpl( type, exp );
        }
    }

    private ExpressionOperation constructColourMap( ExpressionOperation exp[] )
    {
        switch( exp.length ) {
            // colorMap(Color)
            case 1:
                return ( s, a ) -> ColorMap.single( exp[0].get( s ) );

            // colorMap(Color,size)
            case 2:
                return ( s, a ) -> ColorMap.single( exp[0].get( s ), exp[1].getInt( s ) );

            // colorMap(Color,Color,Color...)
            default:
                return ( s, a ) -> {
                    Collection<Object> col = ExpressionOperation.invoke( exp, s );
                    return ColorMap.create( col.toArray( new Color[col.size()] ) );
                };
        }
    }

    private ExpressionOperation constructImpl( String type, ExpressionOperation exp[] )
    {
        switch( exp.length ) {
            case 0:
                switch( type ) {
                    case "rainfallColourMap":
                    case "rainfallColorMap":
                        return ( s, a ) -> ColorMap.rainfall();
                        
                    case "tempColourMap":
                    case "tempColorMap":
                        return ( s, a ) -> RangedColorMap.temp();

                    default:
                        return null;
                }

            case 1:
                switch( type ) {
                    case "colourMap":
                    case "colorMap":
                        return ( s, a ) -> ColorMap.single( ColourType.lookup( exp[0].getString( s ) ).getColor(),
                                                            exp[1].getInt( s ) );

                    // new contourLayer( colorMap )
                    case "contourLayer":
                        return ( s, a ) -> {
                            String t = exp[3].getString( s );
                            switch( t.toLowerCase() ) {
                                case "line":
                                    return Contour.basic( exp[0].get( s ) );

                                default:
                                    throw new IllegalArgumentException( "Unsupported contour type " + t );
                            }
                        };

                    default:
                        return null;
                }

            case 2:
                switch( type ) {

                    // colourMap( colour, size )
                    case "colourMap":
                    case "colorMap":
                        return ( s, a ) -> ColorMap.single( ColourType.lookup( exp[0].getString( s ) ).getColor(),
                                                            exp[1].getInt( s ) );

                    default:
                        return null;
                }

            case 3:
                switch( type ) {

                    // gradientColourMap( colour1, colour2, size )
                    case "gradientColourMap":
                    case "gradientColorMap":
                        return ( s, a ) -> ColorMap.gradient( ColourType.lookup( exp[0].getString( s ) ).getColor(),
                                                              ColourType.lookup( exp[1].getString( s ) ).getColor(),
                                                              exp[1].getInt( s ) );

                    default:
                        return null;
                }

            case 4:
                switch( type ) {

                    // gradientColourMap( colour1, colour2, size, stats )
                    case "gradientColourMap":
                    case "gradientColorMap":
                        return ( s, a ) -> RangedColorMap.gradient( ColourType.lookup( exp[0].getString( s ) ).getColor(),
                                                                    ColourType.lookup( exp[1].getString( s ) ).getColor(),
                                                                    exp[1].getInt( s ),
                                                                    exp[3].get( s ) );

                    default:
                        return null;
                }

            case 5:
                switch( type ) {

                    // gradientColourMap( colour1, colour2, size, min, max )
                    case "gradientColourMap":
                    case "gradientColorMap":
                        return ( s, a ) -> RangedColorMap.gradient( ColourType.lookup( exp[0].getString( s ) ).getColor(),
                                                                    ColourType.lookup( exp[1].getString( s ) ).getColor(),
                                                                    exp[1].getInt( s ),
                                                                    exp[3].getDouble( s ),
                                                                    exp[4].getDouble( s ) );

                    default:
                        return null;
                }

            default:
                return null;
        }
    }

}
