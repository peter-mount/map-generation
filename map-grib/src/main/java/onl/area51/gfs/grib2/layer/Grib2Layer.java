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
package onl.area51.gfs.grib2.layer;

import java.awt.Color;
import java.io.IOException;
import java.util.function.Consumer;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.gfs.grib2.Grib2MetaData;
import onl.area51.mapgen.contour.Contour;
import onl.area51.mapgen.contour.ContourLayer;
import onl.area51.mapgen.grid.Grid;
import onl.area51.mapgen.layer.DefaultTiledLayer;
import onl.area51.mapgen.layer.Layer;
import onl.area51.mapgen.renderer.Renderer;
import onl.area51.mapgen.util.ColorMap;
import ucar.grib.grib2.Grib2Record;

/**
 *
 * @author peter
 */
public class Grib2Layer
        extends DefaultTiledLayer
{

    public Grib2Layer( Grib2MetaData meta, Consumer<Renderer> c )
            throws IOException
    {
        this( meta, true, c );
    }

    public Grib2Layer( Grib2MetaData meta, boolean enabled, Consumer<Renderer> c )
            throws IOException
    {
        super( getName( meta ), enabled, c );
    }

    private static String getName( Grib2MetaData meta )
    {
        return String.format( meta.getLevelType1() == null ? "%s %s" : meta.getLevelType2() == null ? "%s %s (%s %.1f)" : "%s %s (%s %.1f,%s %.1f)",
                              meta.getName(),
                              meta.getUnit(),
                              meta.getLevelType1(), meta.getLevelValue1(),
                              meta.getLevelType2(), meta.getLevelValue2() );
    }

    public static Layer textLayer( Grib2 file, Grib2Record record )
            throws IOException
    {
        Grib2MetaData meta = new Grib2MetaData( record );
        return new Grib2Layer( meta, new Grib2TextRenderer( meta, file ) );
    }

    /**
     * A line contour
     * @param colour Colour of lines
     * @param size number of entries in colour map
     * @param file
     * @param record
     * @return
     * @throws IOException 
     */
    public static Layer contourLineLayer( Color colour, int size, Grib2 file, Grib2Record record )
            throws IOException
    {
        return contourLineLayer( ColorMap.single( colour, size ), file, record );
    }

    public static Layer contourLineLayer( ColorMap map, Grib2 file, Grib2Record record )
            throws IOException
    {
        Grib2MetaData meta = new Grib2MetaData( record );
        Grid grid = file.getGrid( meta );
        Contour contour = Contour.basic( map );
        contour.setData( grid );
        return new ContourLayer( contour, getName( meta ) );
    }
}
