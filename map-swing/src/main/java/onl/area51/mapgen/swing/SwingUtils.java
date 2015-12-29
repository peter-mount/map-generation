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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author peter
 */
public class SwingUtils
{
    
    private static final Executor executor = Executors.newCachedThreadPool();
    private static JFrame frame;

    public static synchronized void setFrame( JFrame frame )
    {
        SwingUtils.frame = frame;
    }

    public static synchronized JFrame getFrame()
    {
        return frame;
    }
    
    public static void execute( Runnable command )
    {
        executor.execute( command );
    }

    public static void executeTask( Task c )
    {
        if( c != null ) {
            executor.execute( () -> {
                try {
                    c.run();
                }
                catch( Exception e ) {
                    ErrorDialog.show( e );
                }
            } );
        }
    }

    /**
     * Delegates to {@link SwingUtilities#invokeLater(java.lang.Runnable) } after an enforced 50ms delay. This is to allow for other tasks
     * to be executed first - for example moving the view port to a preset map
     * <p>
     * @param r task to run
     */
    public static void invokeLater( Runnable r )
    {
        invokeLater( 50, r );
    }

    /**
     * Delegates to {@link SwingUtilities#invokeLater(java.lang.Runnable) } after an enforced 50ms delay. This is to allow for other tasks
     * to be executed first - for example moving the view port to a preset map
     * <p>
     * @param delay delay. Min of 50, if less than 50 then 50 will be used
     * @param r     task to run
     */
    public static void invokeLater( long delay, Runnable r )
    {
        executeTask( () -> {
            Thread.sleep( Math.max( 50L, delay ) );
            SwingUtilities.invokeLater( r );
        } );
    }

    @FunctionalInterface
    public static interface Task
    {

        void run()
                throws Exception;
    }
}
