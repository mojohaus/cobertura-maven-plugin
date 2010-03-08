package org.codehaus.mojo.cobertura;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.mojo.cobertura.stubs.ArtifactStub;

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

        Artifact artifact;

        // artifact = new ArtifactStub();
        // artifact.setFile( new File( PlexusTestCase.getBasedir() + "/target/classes" ) );
        // assertArtifactExists( artifact );
        // pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "net.sourceforge.cobertura" );
        artifact.setArtifactId( "cobertura" );
        artifact.setVersion( "1.9.4.1" );
        artifact.setFile( new File( localRepository + "/net/sourceforge/cobertura/cobertura/1.9.4.1/cobertura-1.9.4.1.jar" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "log4j" );
        artifact.setArtifactId( "log4j" );
        artifact.setVersion( "1.2.9" );
        artifact.setFile( new File( localRepository + "/log4j/log4j/1.2.9/log4j-1.2.9.jar" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "org.apache.ant" );
        artifact.setArtifactId( "ant" );
        artifact.setVersion( "1.7.0" );
        artifact.setFile( new File( localRepository + "/org/apache/ant/ant/1.7.0/ant-1.7.0.jar" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "org.apache.ant" );
        artifact.setArtifactId( "ant-launcher" );
        artifact.setVersion( "1.7.0" );
        artifact.setFile( new File( localRepository + "/org/apache/ant/ant-launcher/1.7.0/ant-launcher-1.7.0.jar" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "oro" );
        artifact.setArtifactId( "oro" );
        artifact.setVersion( "2.0.8" );
        artifact.setFile( new File( localRepository + "/oro/oro/2.0.8/oro-2.0.8.jar" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "asm" );
        artifact.setArtifactId( "asm" );
        artifact.setVersion( "3.0" );
        artifact.setFile( new File( localRepository + "/asm/asm/3.0/asm-3.0.jar" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "asm" );
        artifact.setArtifactId( "asm-tree" );
        artifact.setVersion( "3.0" );
        artifact.setFile( new File( localRepository + "/asm/asm-tree/3.0/asm-tree-3.0.jar" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        artifact = new ArtifactStub();
        artifact.setGroupId( "net.sourceforge.cobertura" );
        artifact.setArtifactId( "cobertura-runtime" );
        artifact.setVersion( "1.9.4.1" );
        artifact.setFile( new File( localRepository +
            "/net/sourceforge/cobertura/cobertura-runtime/1.9.4.1/cobertura-runtime-1.9.4.1.pom" ) );
        assertArtifactExists( artifact );
        pluginClasspath.add( artifact );

        return pluginClasspath;
    }

}
