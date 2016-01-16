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
package onl.area51.mapgen.contour;

import onl.area51.mapgen.layer.AbstractLayer;
import onl.area51.mapgen.renderer.Renderer;

/**
 * A map layer containing Contour's
 *
 * @author peter
 */
public class ContourLayer
        extends AbstractLayer
{

    private Contour contour;

    public ContourLayer()
    {
        this( null, DEFAULT_NAME, true );
    }

    public ContourLayer( String name )
    {
        this( null, name, true );
    }

    public ContourLayer( String name, boolean enabled )
    {
        this( null, name, enabled );
    }

    public ContourLayer( Contour contour )
    {
        this( contour, DEFAULT_NAME, true );
    }

    public ContourLayer( Contour contour, String name )
    {
        this( contour, name, true );
    }

    public ContourLayer( Contour contour, String name, boolean enabled )
    {
        super( name, enabled );
        this.contour = contour;
    }

    public Contour getContour()
    {
        return contour;
    }

    public void setContour( Contour contour )
    {
        this.contour = contour;
    }

    @Override
    public void accept( Renderer t )
    {
        if( isEnabled() && contour != null ) {
            t.render( r -> {
                r.draw( contour::draw );
            } );
        }
    }

}
