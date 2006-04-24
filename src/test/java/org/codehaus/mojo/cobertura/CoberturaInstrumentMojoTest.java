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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.cobertura.stubs.ArtifactStub;
import org.codehaus.plexus.PlexusTestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Edwin Punzalan
 */
public class CoberturaInstrumentMojoTest
    extends AbstractMojoTestCase
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
        Mojo mojo = lookupMojo( "instrument",
                                PlexusTestCase.getBasedir() + "/src/test/plugin-configs/instrument-plugin-config.xml" );

        setVariableValueToObject( mojo, "pluginClasspathList", getPluginClasspath() );

        mojo.execute();

        MavenProject project = (MavenProject) getVariableValueFromObject( mojo, "project" );

        File instrumentedDir = new File( project.getBuild().getOutputDirectory() );

        assertTrue( "Test instrumented class exists", new File( instrumentedDir, "Circle.class" ).exists() );
//        assertTrue( "Test instrumented test class exists", new File( instrumentedDir, "CircleTest.class" ).exists() );
    }

    private List getPluginClasspath()
        throws Exception
    {
        String localRepository = System.getProperty( "localRepository" );

        List pluginClasspath = new ArrayList();

        Artifact artifact = new ArtifactStub();
        artifact.setGroupId( "cobertura" );
        artifact.setArtifactId( "cobertura" );
        artifact.setFile( new File( localRepository + "/cobertura/cobertura/1.7/cobertura-1.7.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "log4j" );
        artifact.setArtifactId( "log4j" );
        artifact.setFile( new File( localRepository + "/log4j/log4j/1.2.9/log4j-1.2.9.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "oro" );
        artifact.setArtifactId( "oro" );
        artifact.setFile( new File( localRepository + "/oro/oro/2.0.8/oro-2.0.8.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "asm" );
        artifact.setArtifactId( "asm" );
        artifact.setFile( new File( localRepository + "/asm/asm/2.1/asm-2.1.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "cobertura" );
        artifact.setArtifactId( "cobertura-runtime" );
        artifact.setVersion( "1.7" );
        artifact.setFile( new File( localRepository + "/cobertura/cobertura-runtime/1.7/cobertura-runtime-1.7.pom" ) );
        pluginClasspath.add( artifact );

        return pluginClasspath;
    }
}
