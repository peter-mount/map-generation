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
public class BasicRectangleTileReference
        implements RectangleTileReference
{

    final int z;

    private final int x1, y1, x2, y2;
    final double x1d;
    private final double y1d, x2d, y2d;

    public BasicRectangleTileReference( int z, int x1, int y1, int x2, int y2 )
    {
        this.z = z;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        x1d = x1;
        y1d = y1;
        x2d = x2;
        y2d = x2;
    }

    public BasicRectangleTileReference( int z, double x1d, double y1d, double x2d, double y2d )
    {
        this.z = z;
        this.x1 = (int) Math.floor( x1d );
        this.y1 = (int) Math.floor( y1d );
        this.x2 = (int) Math.floor( x2d );
        this.y2 = (int) Math.floor( y2d );
        this.x1d = x1d;
        this.y1d = y1d;
        this.x2d = x2d;
        this.y2d = y2d;
    }

    @Override
    public int getZ()
    {
        return z;
    }

    @Override
    public int getX()
    {
        return x1;
    }

    @Override
    public int getY()
    {
        return y1;
    }

    @Override
    public int getX2()
    {
        return x2;
    }

    @Override
    public int getY2()
    {
        return y2;
    }

    @Override
    public double getDecimalX()
    {
        return x1d;
    }

    @Override
    public double getDecimalY()
    {
        return y1d;
    }

    @Override
    public double getDecimalX2()
    {
        return x2d;
    }

    @Override
    public double getDecimalY2()
    {
        return y2d;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 53 * hash + this.z;
        hash = 53 * hash + this.x1;
        hash = 53 * hash + this.y1;
        hash = 53 * hash + this.x2;
        hash = 53 * hash + this.y2;
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.x1d ) ^ (Double.doubleToLongBits( this.x1d ) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.y1d ) ^ (Double.doubleToLongBits( this.y1d ) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.x2d ) ^ (Double.doubleToLongBits( this.x2d ) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits( this.y2d ) ^ (Double.doubleToLongBits( this.y2d ) >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final BasicRectangleTileReference other = (BasicRectangleTileReference) obj;
        return this.z == other.z
               && this.x1 == other.x1
               && this.y1 == other.y1
               && this.x2 == other.x2
               && this.y2 == other.y2
               && Double.doubleToLongBits( this.x1d ) == Double.doubleToLongBits( other.x1d )
               && Double.doubleToLongBits( this.y1d ) == Double.doubleToLongBits( other.y1d )
               && Double.doubleToLongBits( this.x2d ) == Double.doubleToLongBits( other.x2d )
               && Double.doubleToLongBits( this.y2d ) == Double.doubleToLongBits( other.y2d );
    }

    @Override
    public String toString()
    {
        return "RectangleTileReference[" + z + "," + x1d + "," + y1d + "," + x2d + "," + y2d + ']';
    }

}
