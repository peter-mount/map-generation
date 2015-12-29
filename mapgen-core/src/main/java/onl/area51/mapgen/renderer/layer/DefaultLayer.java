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

import java.util.function.Consumer;
import onl.area51.mapgen.renderer.Renderer;

/**
 * A {@link Layer} implementation that delegates rendering to a {@link Consumer}.
 * <p>
 * @author peter
 */
public class DefaultLayer
        extends AbstractLayer
{

    private final Consumer<Renderer> action;

    public DefaultLayer( Consumer<Renderer> action )
    {
        super();
        this.action = action;
    }

    public DefaultLayer( String name, Consumer<Renderer> action )
    {
        super( name );
        this.action = action;
    }

    public DefaultLayer( String name, boolean enabled, Consumer<Renderer> action )
    {
        super( name, enabled );
        this.action = action;
    }

    @Override
    public void accept( Renderer r )
    {
        if( isEnabled() ) {
            action.accept( r );
        }
    }

}
