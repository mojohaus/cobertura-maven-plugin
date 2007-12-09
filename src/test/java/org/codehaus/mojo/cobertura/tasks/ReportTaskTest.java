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

import java.io.File;

import junit.framework.TestCase;

/**
 * @author Edwin Punzalan
 */
public class ReportTaskTest
    extends TestCase
{
    private ReportTask task;

    protected void setUp()
        throws Exception
    {
        task = new ReportTask();
    }

    public void testDataFile()
    {
        File dataFile = new File( "path/to/file" );

        task.setDataFile( dataFile );

        assertEquals( dataFile, task.getDataFile() );
    }

    public void testOutputDir()
    {
        File outputDir = new File( "path/to/file" );

        task.setOutputDirectory( outputDir );

        assertEquals( outputDir, task.getOutputDirectory() );
    }

    public void testOutputFormat()
    {
        String format = "format";

        task.setOutputFormat( format );

        assertEquals( format, task.getOutputFormat() );
    }
}
