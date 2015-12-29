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
package onl.area51.mapgen.renderer.layer;

import java.util.Objects;

/**
 * Implementation of the common {@link Layer} methods
 * <p>
 * @author peter
 */
public abstract class AbstractLayer
        implements Layer
{

    private String name;
    private boolean enabled = false;

    public AbstractLayer()
    {
        this( null, false );
    }

    public AbstractLayer( String name )
    {
        this( name, false );
    }

    public AbstractLayer( String name, boolean enabled )
    {
        setName( name );
        setEnabled( enabled );
    }

    @Override
    public final String getName()
    {
        return name;
    }

    public final void setName( String name )
    {
        this.name = name == null || name.isEmpty() ? DEFAULT_NAME : name;
    }

    @Override
    public final boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public final void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

    @Override
    public final int hashCode()
    {
        return Objects.hashCode( this.name );
    }

    @Override
    public final boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final AbstractLayer other = (AbstractLayer) obj;
        return Objects.equals( this.name, other.name );
    }

}
