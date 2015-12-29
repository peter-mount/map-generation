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
package onl.area51.mapgen.swing;

import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.swing.AbstractListModel;
import onl.area51.mapgen.renderer.layer.Layer;
import onl.area51.mapgen.renderer.layer.Layers;
import onl.area51.mapgen.renderer.layer.LinkedLayers;
import onl.area51.mapgen.renderer.Renderer;

/**
 * An implementation of both {@link Layers} and {@link javax.swing.ListModel} which wraps around an underlying {@link Layers} instance.
 * <p>
 * @Layers} which can be passed to a {@link javax.swing.JList} component.
 * @author peter
 */
public class LayerListModel
        extends AbstractListModel<Layer>
        implements Layers
{

    private Layers layers;

    public LayerListModel()
    {
        this( new LinkedLayers() );
    }

    public LayerListModel( Layers layers )
    {
        this.layers = layers;
    }

    public Layers getLayers()
    {
        return layers;
    }

    public void setLayers( Layers layers )
    {
        if( !this.layers.equals( layers ) ) {
            final int original = this.layers.size();
            this.layers = layers;
            fireContentsChanged( this, 0, Math.max( original, layers.size() ) );
        }
    }

    @Override
    public int indexOf( Layer e )
    {
        return layers.indexOf( e );
    }

    @Override
    public int getSize()
    {
        return layers.size();
    }

    @Override
    public Layer getElementAt( int index )
    {
        return layers.get( index );
    }

    @Override
    public void add( Layer layer )
    {
        layers.addFirst( layer );
        fireIntervalAdded( this, 0, 0 );
    }

    @Override
    public void remove( Layer layer )
    {
        int i = layers.indexOf( layer );
        if( i > -1 ) {
            layers.remove( layer );
            fireIntervalRemoved( this, i, i );
        }
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
        int s = getSize();
        layers.clear();
        fireContentsChanged( this, 0, s - 1 );
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
        fireIntervalAdded( this, index, index );
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
        int s = layers.indexOf( layer );
        if( s > 0 ) {
            layers.moveUp( layer );
            fireContentsChanged( this, s - 1, s );
        }
    }

    @Override
    public void moveDown( Layer layer )
    {
        int s = layers.indexOf( layer );
        if( (s + 1) < layers.size() ) {
            layers.moveDown( layer );
            fireContentsChanged( this, s, s + 1 );
        }
    }

    @Override
    public void accept( Renderer t )
    {
        layers.accept( t );
    }

}
