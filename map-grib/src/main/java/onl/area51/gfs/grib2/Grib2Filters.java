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

import java.util.function.Predicate;
import ucar.grib.grib2.Grib2Pds;
import ucar.grib.grib2.Grib2Record;

/**
 *
 * @author peter
 */
public class Grib2Filters
{

    /**
     * Filter by product
     *
     * @param prodId
     * @param catId
     * @param nameId
     *
     * @return
     */
    public static Predicate<Grib2Record> filterProduct( int prodId, int catId, int nameId )
    {
        return r -> {
            Grib2Pds p = r.getPDS().getPdsVars();
            return p.getProductDefinitionTemplate() == prodId
                   && p.getParameterCategory() == catId
                   && p.getParameterNumber() == nameId;
        };
    }

    /**
     * Filter by level type 1
     *
     * @param levelType1
     *
     * @return
     */
    public static Predicate<Grib2Record> filterLevel1( int levelType1 )
    {
        return r -> r.getPDS().getPdsVars().getLevelType1() == levelType1;
    }

    /**
     * Filter by level type 1
     *
     * @param levelValue1
     *
     * @return
     */
    public static Predicate<Grib2Record> filterLevel1( double levelValue1 )
    {
        return r -> r.getPDS().getPdsVars().getLevelValue1() == levelValue1;
    }

    /**
     * Filter by level type 1 and value
     *
     * @param levelType1
     * @param levelValue1
     *
     * @return
     */
    public static Predicate<Grib2Record> filterLevel1( int levelType1, double levelValue1 )
    {
        return r -> {
            Grib2Pds p = r.getPDS().getPdsVars();
            return p.getLevelType1() == levelType1 && p.getLevelValue1() == levelValue1;
        };
    }

}
