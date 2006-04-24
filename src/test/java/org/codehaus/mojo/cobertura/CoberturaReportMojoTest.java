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

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.reporting.MavenReport;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.mojo.cobertura.stubs.ArtifactStub;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * @author Edwin Punzalan
 */
public class CoberturaReportMojoTest
    extends AbstractMojoTestCase
{
    public void testReport()
        throws Exception
    {
        Mojo mojo = lookupMojo( "cobertura",
                                PlexusTestCase.getBasedir() + "/src/test/plugin-configs/report-plugin-config.xml" );

        setMojoPluginClasspath( mojo );

        MavenReport reportMojo = (MavenReport) mojo;

        assertTrue( "Should be able to generate a report", reportMojo.canGenerateReport() );

        mojo.execute();
    }

    private void setMojoPluginClasspath( Mojo mojo )
        throws Exception
    {
        String localRepository = System.getProperty( "localRepository" );

        List pluginClasspath = new ArrayList();

        Artifact artifact = new ArtifactStub();
        artifact.setFile( new File( localRepository + "/cobertura/cobertura/1.7/cobertura-1.7.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setFile( new File( localRepository + "/log4j/log4j/1.2.9/log4j-1.2.9.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setFile( new File( PlexusTestCase.getBasedir() + "/target/classes" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setFile( new File( localRepository + "/log4j/log4j/1.2.9/log4j-1.2.9.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setFile( new File( localRepository + "/javancss/javancss/21.41/javancss-21.41.jar" ) );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setFile( new File( localRepository + "/javancss/ccl/21.41/ccl-21.41.jar" ) );
        pluginClasspath.add( artifact );

        setVariableValueToObject( mojo, "pluginClasspathList", pluginClasspath );
    }
}
