/*
 * #%L
 * Mojo's Maven plugin for Cobertura
 * %%
 * Copyright (C) 2005 - 2013 Codehaus
 * %%
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
 * #L%
 */
package org.codehaus.mojo.cobertura.tasks;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import junit.framework.TestCase;
import org.codehaus.mojo.cobertura.configuration.ConfigInstrumentation;

import java.io.File;

/**
 * @author Edwin Punzalan
 */
public class InstrumentTaskTest
    extends TestCase
{
    private InstrumentTask task;

    protected void setUp()
        throws Exception
    {
        task = new InstrumentTask();
    }

    public void testDataFile()
    {
        File dataFile = new File( "path/to/file" );

        task.setDataFile( dataFile );

        assertEquals( dataFile, task.getDataFile() );
    }

    public void testConfig()
    {
        ConfigInstrumentation config = new ConfigInstrumentation();

        task.setConfig( config );

        assertEquals( config, task.getConfig() );
    }

    public void testDestinationDir()
    {
        File destinationDir = new File( "path/to/file" );

        task.setDestinationDir( destinationDir );

        assertEquals( destinationDir, task.getDestinationDir() );
    }

    public void testMaxmem()
    {
        final String expectedValue128m = "128m";

        task.setMaxmem( expectedValue128m );

        assertEquals( expectedValue128m, task.getMaxmem() );
    }

    public void testMaxPermgen()
    {
        final String expectedValue128m = "128m";

        task.setMaxPermgen( expectedValue128m );

        assertEquals( expectedValue128m, task.getMaxPermgen() );
    }
}
