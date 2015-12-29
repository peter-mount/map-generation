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

/**
 *
 * @author peter
 */
public class ReadOnlyLayers
        extends LayersWrapper
{

    public ReadOnlyLayers( Layers layers )
    {
        super( layers );
    }

    @Override
    public final void moveDown( Layer layer )
    {
    }

    @Override
    public final void moveUp( Layer layer )
    {
    }

    @Override
    public final void add( int index, Layer element )
    {
    }

    @Override
    public final void add( Layer layer )
    {
    }

    @Override
    public final void remove( Layer layer )
    {
    }

    @Override
    public final void clear()
    {
    }

    @Override
    public final void addLast( Layer e )
    {
    }

    @Override
    public final void addFirst( Layer e )
    {
    }

}
