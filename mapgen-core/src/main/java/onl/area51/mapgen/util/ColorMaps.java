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
package onl.area51.mapgen.util;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static data for {@link ColorMap} use only. Use the relevant methods in that interface to access these.
 *
 * @author peter
 */
final class ColorMaps
{

    static final Color RAINFALL[] = {new Color( 0, 94, 0 ),
                                     new Color( 0, 135, 0 ),
                                     new Color( 0, 175, 0 ),
                                     new Color( 0, 212, 0 ),
                                     new Color( 130, 120, 0 ),
                                     new Color( 136, 66, 0 ),
                                     new Color( 142, 12, 0 ),
                                     new Color( 255, 25, 0 ),
                                     new Color( 255, 130, 3 ),
                                     new Color( 255, 239, 0 ),
                                     Color.WHITE};

    static final Color TEMP[] = {
        new Color( 130, 130, 130 ),//-18
        new Color( 86, 84, 116 ),//-16
        new Color( 89, 68, 127 ),//-14
        new Color( 90, 0, 127 ),//-12
        new Color( 50, 0, 127 ),//-10
        new Color( 0, 0, 130 ),//-8
        new Color( 0, 51, 180 ),//-6
        new Color( 0, 0, 255 ),//-4
        new Color( 0, 126, 255 ),//-2
        new Color( 0, 190, 255 ),// 0
        new Color( 0, 255, 255 ),// 2
        new Color( 0, 247, 198 ),// 4
        new Color( 24, 215, 140 ),//6
        new Color( 0, 170, 100 ),// 8
        new Color( 43, 170, 43 ),// 10
        new Color( 43, 200, 43 ),// 12
        new Color( 0, 255, 0 ),// 14
        new Color( 204, 255, 0 ),// 16
        new Color( 255, 255, 0 ),// 18
        new Color( 237, 237, 126 ),// 20
        new Color( 237, 237, 126 ),// 22
        new Color( 220, 174, 73 ),// 24
        new Color( 255, 170, 0 ),// 26
        new Color( 255, 85, 0 ),// 28
        new Color( 255, 0, 0 ),// 30
        new Color( 200, 0, 0 ),// 32
        new Color( 120, 0, 0 )
    };

    private static final Map<Integer, Color> GREY_SCALE = new ConcurrentHashMap<>();

    /**
     * Returns a grey Color
     * @param index from 0..255
     * @return Color
     */
    static Color getGrey( int index )
    {
        return GREY_SCALE.computeIfAbsent( Math.max( 0, Math.min( index, 255 ) ), c -> new Color( c, c, c ) );
    }
}
