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

import java.awt.geom.Rectangle2D;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import ucar.grib.grib2.Grib2GDSVariables;
import ucar.grib.grib2.Grib2IdentificationSection;
import ucar.grib.grib2.Grib2IndicatorSection;
import ucar.grib.grib2.Grib2Pds;
import ucar.grib.grib2.Grib2Record;
import ucar.grib.grib2.Grib2Tables;
import ucar.grib.grib2.ParameterTable;

/**
 * Metadata for a Grib2Record
 *
 * @author peter
 */
public class Grib2MetaData
{

    private final int id;
    private final String name;
    private final String category;
    private final String unit;
    private final LocalDateTime referenceTime;
    private final LocalDateTime forecastTime;
    private final Rectangle2D.Double bounds;
    private final int columns;
    private final int rows;
    private final String levelType1;
    private final Double levelValue1;
    private final String levelType2;
    private final Double levelValue2;
    private final Grib2Record record;

    private static final ZoneId UTC = ZoneId.of( "Z" );

    public Grib2MetaData( Grib2Record record )
    {
        this.record = record;

        Grib2IndicatorSection is = record.getIs();
        Grib2IdentificationSection id = record.getId();
        Grib2GDSVariables gdsv = record.getGDS().getGdsVars();
        Grib2Pds pdsv = record.getPDS().getPdsVars();

        this.id = pdsv.getParameterNumber();
        this.category = ParameterTable.getCategoryName( is.getDiscipline(), pdsv.getParameterCategory() );
        this.name = ParameterTable.getParameterName( is.getDiscipline(), pdsv.getParameterCategory(), pdsv.getParameterNumber() );
        this.unit = ParameterTable.getParameterUnit( is.getDiscipline(), pdsv.getParameterCategory(), pdsv.getParameterNumber() );
        this.columns = gdsv.getNx();
        this.rows = gdsv.getNy();

        this.referenceTime = LocalDateTime.ofInstant( Instant.ofEpochMilli( pdsv.getReferenceTime() ), UTC );
        this.forecastTime = this.referenceTime.plusHours( pdsv.getForecastTime() );

        String level1Type = pdsv.getLevelType1() + " " + Grib2Tables.codeTable4_5( pdsv.getLevelType1() );
        if( pdsv.getLevelType1() != 255 ) {
            levelType1 = Grib2Tables.codeTable4_5( pdsv.getLevelType1() );
            levelValue1 = pdsv.getLevelValue1();
        }
        else {
            levelType1 = null;
            levelValue1 = 0.0;
        }
        if( pdsv.getLevelType2() != 255 ) {
            levelType2 = Grib2Tables.codeTable4_5( pdsv.getLevelType2() );
            levelValue2 = pdsv.getLevelValue2();
        }
        else {
            levelType2 = null;
            levelValue2 = 0.0;
        }

        this.bounds = new Rectangle2D.Double( gdsv.getLo1(), gdsv.getLa1(), gdsv.getLo2(), gdsv.getLa2() );
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public String getUnit()
    {
        return unit;
    }

    public LocalDateTime getReferenceTime()
    {
        return referenceTime;
    }

    public LocalDateTime getForecastTime()
    {
        return forecastTime;
    }

    public Rectangle2D.Double getBounds()
    {
        return bounds;
    }

    public int getColumns()
    {
        return columns;
    }

    public int getRows()
    {
        return rows;
    }

    public String getLevelType1()
    {
        return levelType1;
    }

    public Double getLevelValue1()
    {
        return levelValue1;
    }

    public String getLevelType2()
    {
        return levelType2;
    }

    public Double getLevelValue2()
    {
        return levelValue2;
    }

    public static ZoneId getUTC()
    {
        return UTC;
    }

    public Grib2Record getRecord()
    {
        return record;
    }

}
