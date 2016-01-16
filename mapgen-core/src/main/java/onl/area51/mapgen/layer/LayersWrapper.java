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
package onl.area51.mapgen.layer;

import java.util.function.Consumer;
import java.util.stream.Stream;
import onl.area51.mapgen.renderer.Renderer;

/**
 *
 * @author peter
 */
public class LayersWrapper implements Layers
{
    private final Layers layers;

    public LayersWrapper( Layers layers )
    {
        this.layers = layers;
    }

    protected final Layers getLayers()
    {
        return layers;
    }

    @Override
    public void add( Layer layer )
    {
        layers.add( layer );
    }

    @Override
    public void remove( Layer layer )
    {
        layers.remove( layer );
    }

    @Override
    public void forEach( Consumer<Layer> c )
    {
        layers.forEach( c );
    }

    @Override
    public Stream<Layer> stream()
    {
        return layers.stream();
    }

    @Override
    public int indexOf( Layer e )
    {
        return layers.indexOf( e );
    }

    @Override
    public Layer getFirst()
    {
        return layers.getFirst();
    }

    @Override
    public Layer getLast()
    {
        return layers.getLast();
    }

    @Override
    public void addFirst( Layer e )
    {
        layers.addFirst( e );
    }

    @Override
    public void addLast( Layer e )
    {
        layers.addLast( e );
    }

    @Override
    public void clear()
    {
        layers.clear();
    }

    @Override
    public Layer get( int index )
    {
        return layers.get( index );
    }

    @Override
    public void add( int index, Layer element )
    {
        layers.add( index, element );
    }

    @Override
    public int size()
    {
        return layers.size();
    }

    @Override
    public boolean isEmpty()
    {
        return layers.isEmpty();
    }

    @Override
    public void moveUp( Layer layer )
    {
        layers.moveUp( layer );
    }

    @Override
    public void moveDown( Layer layer )
    {
        layers.moveDown( layer );
    }

    @Override
    public void accept( Renderer t )
    {
        layers.accept( t );
    }
    
}
