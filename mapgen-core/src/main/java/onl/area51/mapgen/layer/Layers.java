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
 * Manages a set of {@link Layer}'s
 * <p>
 * @author peter
 */
public interface Layers
        extends Consumer<Renderer>
{

    void add( Layer layer );

    void remove( Layer layer );

    void forEach( Consumer<Layer> c );

    Stream<Layer> stream();

    int indexOf( Layer e );

    Layer getFirst();

    Layer getLast();

    void addFirst( Layer e );

    void addLast( Layer e );

    void clear();

    Layer get( int index );

    void add( int index, Layer element );

    int size();

    boolean isEmpty();

    /**
     * Move a layer up in the list
     * <p>
     * @param layer
     */
    void moveUp( Layer layer );

    /**
     * Move a layer down in the list
     * <p>
     * @param layer
     */
    void moveDown( Layer layer );

    /**
     * Applies the supplied {@link Renderer} to this layer set.
     * <p>
     * Note: This will apply them in reverse order as layers at the "Bottom" or end of the list are below the others so need rendering first.
     * <p>
     * @param t
     */
    @Override
    void accept( Renderer t );
}
