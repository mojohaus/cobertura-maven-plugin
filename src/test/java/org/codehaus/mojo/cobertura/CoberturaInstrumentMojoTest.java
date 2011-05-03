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
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.context.Context;


/**
 * @author Edwin Punzalan
 */
public class CoberturaInstrumentMojoTest
    extends AbstractCoberturaTestCase
{
  private String dataFile;
  private final String DUMMY_ARTIFACT_ID = "myproject";
  private final String DUMMY_GROUP_ID = "com.mycompany.myproject";
  private final String DUMMY_VERSION = "1.0-SNAPSHOT";

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

  public void testAttachInstrumentation()
        throws Exception
    {
        Mojo mojo =
            lookupMojo( "instrument", PlexusTestCase.getBasedir() + "/src/test/plugin-configs/" +
                "instrument-instrumentation-plugin-config.xml" );

        MavenProject mavenProject = (MavenProject) getVariableValueFromObject( mojo, "project" );
        mavenProject.setArtifactId(DUMMY_ARTIFACT_ID);
        mavenProject.setGroupId(DUMMY_GROUP_ID);
        mavenProject.setVersion(DUMMY_VERSION);
        mavenProject.getArtifact().setArtifactId(DUMMY_ARTIFACT_ID);
        mavenProject.getArtifact().setGroupId(DUMMY_GROUP_ID);
        mavenProject.getArtifact().setVersion(DUMMY_VERSION);
        mavenProject.getArtifact().setVersionRange(VersionRange.createFromVersion(DUMMY_VERSION));

        setVariableValueToObject( mojo, "classifier", "cobertura" );

        setVariableValueToObject( mojo, "pluginClasspathList", getPluginClasspath() );

        Log log = new SystemStreamLog()
        {
            public boolean isDebugEnabled()
            {
                return true;
            }
        };


        setVariableValueToObject( mojo, "log", log );

        setVariableValueToObject( mojo, "attach", Boolean.TRUE );

        mojo.execute();

        MavenProject project = (MavenProject) getVariableValueFromObject( mojo, "project" );

        File instrumentedDir = new File( project.getBuild().getOutputDirectory() );

        assertEquals(1, project.getAttachedArtifacts().size());

        Artifact attachedSerArtifact = (Artifact)project.getAttachedArtifacts().get(0);

        assertEquals(DUMMY_GROUP_ID, attachedSerArtifact.getGroupId());
        assertEquals(DUMMY_ARTIFACT_ID, attachedSerArtifact.getArtifactId());
        assertEquals(DUMMY_VERSION, attachedSerArtifact.getVersion());

        assertTrue( "Test instrumented class exists", new File( instrumentedDir, "Circle.class" ).exists() );
    }

}
