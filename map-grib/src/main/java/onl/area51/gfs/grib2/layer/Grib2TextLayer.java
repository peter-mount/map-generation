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

import java.io.IOException;
import java.util.function.Consumer;
import onl.area51.gfs.grib2.Grib2;
import onl.area51.mapgen.renderer.Renderer;
import ucar.grib.grib2.Grib2GDSVariables;
import ucar.grib.grib2.Grib2GridDefinitionSection;
import ucar.grib.grib2.Grib2Record;

/**
 *
 * @author peter
 */
public class Grib2TextLayer
        implements Consumer<Renderer>
{

    Grib2GDSVariables grid;
    private final float[] data;

    public Grib2TextLayer( Grib2 file, Grib2Record record ) throws IOException
    {
        grid = record.getGDS().getGdsVars();
        data = file.getData( record );
    }

    @Override
    public void accept( Renderer t )
    {
    }

}
