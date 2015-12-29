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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;
import onl.area51.mapgen.renderer.Renderer;

/**
 * Manages a set of {@link Layer}'s
 * <p>
 * @author peter
 */
public class LinkedLayers
        implements Layers
{

    private final LinkedList<Layer> layers = new LinkedList<>();

    @Override
    public int indexOf( Layer e )
    {
        return layers.indexOf( e );
    }

    
    @Override
    public void add( Layer layer )
    {
        layers.addLast( layer );
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

    /**
     * Move a layer up in the list
     * <p>
     * @param layer
     */
    @Override
    public void moveUp( Layer layer )
    {
        final int i = layers.indexOf( layer );
        if( i < 0 ) {
            throw new NoSuchElementException( layer.toString() );
        }
        if( i > 0 ) {
            layers.remove( layer );
            layers.add( i - 1, layer );
        }
    }

    /**
     * Move a layer down in the list
     * <p>
     * @param layer
     */
    @Override
    public void moveDown( Layer layer )
    {
        final int s = layers.size();
        final int i = layers.indexOf( layer );
        if( i < 0 ) {
            throw new NoSuchElementException( layer.toString() );
        }
        if( i < layers.size() ) {
            layers.remove( layer );
            layers.add( i + 1, layer );
        }
    }

    /**
     * Applies the supplied {@link Renderer} to this layer set.
     * <p>
     * Note: This will apply them in reverse order as layers at the "Bottom" or end of the list are below the others so need rendering first.
     * <p>
     * @param t
     */
    @Override
    public void accept( Renderer t )
    {
        final Iterator<Layer> i = layers.descendingIterator();
        while( i.hasNext() ) {
            i.next().accept( t );
        }
    }

}
