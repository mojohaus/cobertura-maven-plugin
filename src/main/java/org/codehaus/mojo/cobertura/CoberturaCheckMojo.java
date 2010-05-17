package org.codehaus.mojo.cobertura;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.mojo.cobertura.tasks.CheckTask;

/**
 * Check the Last Instrumentation Results.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @goal check
 * @execute phase="test" lifecycle="cobertura"
 * @phase verify
 */
public class CoberturaCheckMojo
    extends AbstractCoberturaMojo
{

    /**
     * Mojo main entry
     * @throws MojoExecutionException
     */
    public void execute()
        throws MojoExecutionException
    {
        if ( skipMojo() )
        {
            return;
        }
        
        if ( check == null )
        {
            throw new MojoExecutionException( "The Check configuration is missing." );
        }

        ArtifactHandler artifactHandler = project.getArtifact().getArtifactHandler();
        if ( !"java".equals( artifactHandler.getLanguage() ) )
        {
            getLog().info(
                "Not executing cobertura:instrument as the project is not a Java classpath-capable package" );
        }
        else
        {
            if ( !dataFile.exists() )
            {
                getLog().info( "Cannot perform check, instrumentation not performed - skipping." );
            }
            else
            {
                CheckTask task = new CheckTask();
                setTaskDefaults( task );
                task.setConfig( check );
                task.setDataFile( dataFile.getAbsolutePath() );

                task.execute();
            }
        }
    }
}
