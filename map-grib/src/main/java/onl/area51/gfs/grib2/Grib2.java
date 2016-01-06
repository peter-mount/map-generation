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
package onl.area51.gfs.grib2;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import ucar.grib.QuasiRegular;
import ucar.grib.grib2.Grib2BitMapSection;
import ucar.grib.grib2.Grib2DataRepresentationSection;
import ucar.grib.grib2.Grib2DataSection;
import ucar.grib.grib2.Grib2GridDefinitionSection;
import ucar.grib.grib2.Grib2IndicatorSection;
import ucar.grib.grib2.Grib2Input;
import ucar.grib.grib2.Grib2Pds;
import ucar.grib.grib2.Grib2Product;
import ucar.grib.grib2.Grib2ProductDefinitionSection;
import ucar.grib.grib2.Grib2Record;
import ucar.grib.grib2.Grib2Tables;
import ucar.grib.grib2.ParameterTable;

/**
 *
 * @author peter
 */
public class Grib2
{

    private final GribFile raf;
    private final Grib2Input input;

    public Grib2( GribFile raf )
            throws IOException
    {
        this.raf = raf;
        input = new Grib2Input( raf );
        input.scan( false, false );
//        raf.seek( 0);
//        input.scan( true, false );
    }

    public float[] getData( Grib2Record record )
            throws IOException
    {
        return getData( record, true );
    }

    public float[] getData( Grib2Record record, boolean expandQuasi )
            throws IOException
    {
        Grib2GridDefinitionSection gds = record.getGDS();
        Grib2ProductDefinitionSection pds = record.getPDS();
        Grib2DataRepresentationSection drs = record.getDRS();

        raf.seek( record.getPdsOffset() + pds.getPdsVars().getLength() + drs.getLength() );
        Grib2BitMapSection bms = new Grib2BitMapSection( true, raf, gds );

        if( bms.getBitmapIndicator() == 254 ) { //previously defined in the same GRIB2 record
            throw new UnsupportedOperationException( "Previous defs not supported" );
            // Grib2Data line 89
        }

        // Get the data
        Grib2DataSection ds = new Grib2DataSection( true, raf, gds, drs, bms );  // Section 7
        //System.out.println("DS offset=" + ds.getOffset() );

        // not a quasi grid or don't expand Quasi
        if( (gds.getGdsVars().getOlon() == 0) || !expandQuasi ) {
            return ds.getData();
        }
        else {
            QuasiRegular qr = new QuasiRegular( ds.getData(), (Object) gds );
            return qr.getData();
        }
    }

    public final int getEdition()
            throws IOException
    {
        return input.getEdition();
    }

    public final List<Grib2Product> getProducts()
    {
        return input.getProducts();
    }

    public Stream<Grib2Product> products()
    {
        return getProducts().stream();
    }

    public void forEachProduct( Consumer<Grib2Product> action )
    {
        getProducts().forEach( action );
    }

    public int getRecordCount()
    {
        return input.getRecords().size();
    }

    public Grib2Record getRecord( int index )
    {
        return input.getRecords().get( index );
    }

    public final List<Grib2Record> getRecords()
    {
        return input.getRecords();
    }

    public Stream<Grib2Record> records()
    {
        return getRecords().stream();
    }

    public void forEachRecord( Consumer<Grib2Record> action )
    {
        getRecords().forEach( action );
    }

    public void forEachRecord( BiConsumer<Integer, Grib2Record> action )
    {
        List<Grib2Record> r = getRecords();
        int s = r.size();
        for( int i = 0; i < s; i++ ) {
            action.accept( i, r.get( i ) );
        }
    }

    public final Map<String, Grib2GridDefinitionSection> getGDSs()
    {
        return input.getGDSs();
    }

    /**
     * Generate an index of the available records in this grib2 file.
     *
     * @return
     */
    public JsonArray jsonIndex()
    {
        JsonArrayBuilder a = Json.createArrayBuilder();
        forEachRecord( ( index, r ) -> a.add( toJson( index, r ) ) );
        return a.build();
    }

    /**
     * Converts a {@link Grib2Record} into a JSON Object
     *
     * @param index
     * @param r
     *
     * @return
     */
    public static JsonObjectBuilder toJson( int index, Grib2Record r )
    {
        Grib2IndicatorSection is = r.getIs();
        Grib2Pds pdsv = r.getPDS().getPdsVars();
        int productDefinition = pdsv.getProductDefinitionTemplate();
        int tgp = pdsv.getGenProcessType();
        return Json.createObjectBuilder()
                .add( "index", index )
                .add( "productDefId", productDefinition )
                .add( "productDef", Grib2Tables.codeTable4_0( productDefinition ) )
                .add( "paramCatId", pdsv.getParameterCategory() )
                .add( "paramCat", ParameterTable.getCategoryName( is.getDiscipline(), pdsv.getParameterCategory() ) )
                .add( "paramNameId", pdsv.getParameterNumber() )
                .add( "paramName", ParameterTable.getParameterName( is.getDiscipline(), pdsv.getParameterCategory(), pdsv.getParameterNumber() ) )
                .add( "paramUnit", ParameterTable.getParameterUnit( is.getDiscipline(), pdsv.getParameterCategory(), pdsv.getParameterNumber() ) )
                .add( "generatingProcessTypeId", tgp )
                .add( "generatingProcessType", Grib2Tables.codeTable4_3( tgp ) )
                .add( "forecastTime", pdsv.getForecastTime() )
                .add( "firstSurfaceTypeId", pdsv.getLevelType1() )
                .add( "firstSurfaceType", Grib2Tables.codeTable4_5( pdsv.getLevelType1() ) )
                .add( "firstSurfaceValue", pdsv.getLevelValue1() )
                .add( "secondSurfaceTypeId", pdsv.getLevelType2() )
                .add( "secondSurfaceType", Grib2Tables.codeTable4_5( pdsv.getLevelType2() ) )
                .add( "secondSurfaceValue", pdsv.getLevelValue2() );
    }

}
