package org.codehaus.mojo.cobertura;

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

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusTestCase;

/**
 * @author Edwin Punzalan
 */
public class CoberturaInstrumentMojoTest
    extends AbstractCoberturaTestCase
{
    private String dataFile;

    protected void setUp()
        throws Exception
    {
        super.setUp();

        dataFile = System.getProperty( "net.sourceforge.cobertura.datafile" );
    }

    protected void tearDown()
        throws Exception
    {
        if ( dataFile != null )
        {
            System.setProperty( "net.sourceforge.cobertura.datafile", dataFile );
        }

        super.tearDown();
    }

    public void testDefault()
        throws Exception
    {
        Mojo mojo =
            lookupMojo( "instrument", PlexusTestCase.getBasedir() +
                "/src/test/plugin-configs/instrument-plugin-config.xml" );

        setVariableValueToObject( mojo, "pluginClasspathList", getPluginClasspath() );

        mojo.execute();

        MavenProject project = (MavenProject) getVariableValueFromObject( mojo, "project" );

        File instrumentedDir = new File( project.getBuild().getOutputDirectory() );

        assertTrue( "Test instrumented class exists", new File( instrumentedDir, "Circle.class" ).exists() );
    }

    public void testDebugEnabled()
        throws Exception
    {
        Mojo mojo =
            lookupMojo( "instrument", PlexusTestCase.getBasedir() +
                "/src/test/plugin-configs/instrument-plugin-config.xml" );

        setVariableValueToObject( mojo, "pluginClasspathList", getPluginClasspath() );

        Log log = new SystemStreamLog()
        {
            public boolean isDebugEnabled()
            {
                return true;
            }
        };

        setVariableValueToObject( mojo, "log", log );

        mojo.execute();

        MavenProject project = (MavenProject) getVariableValueFromObject( mojo, "project" );

        File instrumentedDir = new File( project.getBuild().getOutputDirectory() );

        assertTrue( "Test instrumented class exists", new File( instrumentedDir, "Circle.class" ).exists() );
    }

    public void testInstrumentation()
        throws Exception
    {
        Mojo mojo =
            lookupMojo( "instrument", PlexusTestCase.getBasedir() + "/src/test/plugin-configs/" +
                "instrument-instrumentation-plugin-config.xml" );

        setVariableValueToObject( mojo, "pluginClasspathList", getPluginClasspath() );

        Log log = new SystemStreamLog()
        {
            public boolean isDebugEnabled()
            {
                return true;
            }
        };

        setVariableValueToObject( mojo, "log", log );

        mojo.execute();

        MavenProject project = (MavenProject) getVariableValueFromObject( mojo, "project" );

        File instrumentedDir = new File( project.getBuild().getOutputDirectory() );

        assertTrue( "Test instrumented class exists", new File( instrumentedDir, "Circle.class" ).exists() );
    }
}
