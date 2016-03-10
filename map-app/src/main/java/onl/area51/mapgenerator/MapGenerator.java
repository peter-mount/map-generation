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
package onl.area51.mapgenerator;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import onl.area51.job.cluster.JobCluster;
import uk.trainwatch.kernel.CommandArguments;

/**
 * Handles the booting of the MapGen cluster node
 *
 * @author peter
 */
@ApplicationScoped
public class MapGenerator
{

    private static final Logger LOG = Logger.getLogger( MapGenerator.class.getName() );

    @Inject
    private JobCluster jobCluster;

    /**
     * Instantiate this bean on startup.
     *
     * @param args
     */
    public void boot( @Observes CommandArguments args )
    {
        // Nothing to do here, it's presence ensures the bean is instantiated by CDI
        jobCluster.isValidName( null );
    }

    @PostConstruct
    void start()
    {
    }

    @PreDestroy
    void stop()
    {
    }
}
