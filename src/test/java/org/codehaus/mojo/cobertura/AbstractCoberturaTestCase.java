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
package org.codehaus.mojo.cobertura;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.mojo.cobertura.stubs.ArtifactStub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Base TestCase for all Cobertura Tests.
 *
 * @author Joakim Erdfelt
 */
public abstract class AbstractCoberturaTestCase
    extends AbstractMojoTestCase
{
    protected void assertArtifactExists( Artifact artifact )
    {
        if ( artifact == null )
        {
            fail( "Artifact is null." );
        }

        if ( artifact.getFile() == null )
        {
            fail( "Artifact.file is not defined for " + artifact );
        }

        if ( !artifact.getFile().exists() )
        {
            fail( "Artifact " + artifact.getFile().getAbsolutePath() + " file does not exist." );
        }
    }

    protected List getPluginClasspath()
        throws Exception
    {
        String localRepository = System.getProperty( "localRepository" );

        assertNotNull( "System.property(localRepository) should not be null.", localRepository );

        List pluginClasspath = new ArrayList();

        // Artifact artifact;
        // artifact = new ArtifactStub();
        // artifact.setFile( new File( PlexusTestCase.getBasedir() + "/target/classes" ) );
        // assertArtifactExists( artifact );
        // pluginClasspath.add( artifact );

        // Specify dependencies, that must match whatever is in the Cobertura version being used
        // @todo We need to find a way to not have to repeat this info here

        String asmVersion = "5.0.1";
        String coberturaVersion = "2.1.1";

        pluginClasspath.add( createArtifact( "net.sourceforge.cobertura", "cobertura", coberturaVersion, "jar", localRepository ) );

        pluginClasspath.add( createArtifact( "ch.qos.logback", "logback-classic", "1.0.13", "jar", localRepository ) );
        pluginClasspath.add( createArtifact( "ch.qos.logback", "logback-core", "1.0.13", "jar", localRepository ) );

        pluginClasspath.add( createArtifact( "org.apache.ant", "ant", "1.8.3", "jar", localRepository ) );
        pluginClasspath.add( createArtifact( "org.apache.ant", "ant-launcher", "1.8.3", "jar", localRepository ) );

        pluginClasspath.add( createArtifact( "org.apache.commons", "commons-lang3", "3.3.2", "jar", localRepository ) );

        pluginClasspath.add( createArtifact( "org.ow2.asm", "asm", asmVersion, "jar", localRepository ) );
        pluginClasspath.add( createArtifact( "org.ow2.asm", "asm-analysis", asmVersion, "jar", localRepository ) );
        pluginClasspath.add( createArtifact( "org.ow2.asm", "asm-commons", asmVersion, "jar", localRepository ) );
        pluginClasspath.add( createArtifact( "org.ow2.asm", "asm-tree", asmVersion, "jar", localRepository ) );
        pluginClasspath.add( createArtifact( "org.ow2.asm", "asm-util", asmVersion, "jar", localRepository ) );

        pluginClasspath.add( createArtifact( "oro", "oro", "2.0.8", "jar", localRepository ) );

        pluginClasspath.add( createArtifact( "org.slf4j", "slf4j-api", "1.7.5", "jar", localRepository ) );

        pluginClasspath.add( createArtifact( "net.sourceforge.cobertura", "cobertura-runtime", coberturaVersion, "pom", localRepository ) );

        return pluginClasspath;
    }

    private Artifact createArtifact( String groupId, String artifactId, String version, String type, String localRepository ) {
        Artifact artifact;
        artifact = new ArtifactStub();
        artifact.setGroupId( groupId );
        artifact.setArtifactId( artifactId );
        artifact.setVersion( version );
        artifact.setFile( new File( localRepository + "/" + groupId.replace( ".", "/" ) + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + "." + type ) );
        assertArtifactExists( artifact );
        return artifact;
    }
}
