/*
 * Copyright 2015 peter.
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
package onl.area51.mapgen.util.tile;

/**
 * Implementation of {@link TileReference}
 * <p>
 * @author peter
 */
public class BasicTileReference
        implements TileReference
{

    private final int z, x, y;
    private final double xd, yd;

    public BasicTileReference( int z, int x, int y )
    {
        this.z = z;
        this.x = x;
        this.y = y;
        xd = x;
        yd = y;
    }

    public BasicTileReference( int z, double xd, double yd )
    {
        this.z = z;
        this.x = (int) Math.floor( xd );
        this.y = (int) Math.floor( yd );
        this.xd = xd;
        this.yd = yd;
    }

    @Override
    public int getZ()
    {
        return z;
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public double getDecimalX()
    {
        return xd;
    }

    @Override
    public double getDecimalY()
    {
        return yd;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 53 * hash + this.z;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.xd ) ^ (Double.doubleToLongBits( this.xd ) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.yd ) ^ (Double.doubleToLongBits( this.yd ) >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final BasicTileReference other = (BasicTileReference) obj;
        return this.z == other.z
               && this.x == other.x
               && this.y == other.y
               && Double.doubleToLongBits( this.xd ) == Double.doubleToLongBits( other.xd )
               && Double.doubleToLongBits( this.yd ) == Double.doubleToLongBits( other.yd );
    }

    @Override
    public String toString()
    {
        return "TileReference[" + z + "," + xd + "," + yd + ']';
    }

}
