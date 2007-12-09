package org.codehaus.mojo.cobertura.tasks;

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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.mojo.cobertura.configuration.ConfigInstrumentation;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

/**
 * The Instrument Task.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public class InstrumentTask
    extends AbstractTask
{
    private ConfigInstrumentation config = null;

    private File dataFile = null;

    private File destinationDir = null;

    /**
     * Create a new InstrumentTask.
     */
    public InstrumentTask()
    {
        super( "net.sourceforge.cobertura.instrument.Main" );
    }

    public void execute()
        throws MojoExecutionException
    {
        /* this task should always use the commands file approach */
        cmdLineArgs.setUseCommandsFile( true );

        if ( dataFile != null )
        {
            cmdLineArgs.addArg( "--datafile", dataFile.getAbsolutePath() );
        }

        if ( destinationDir != null )
        {
            cmdLineArgs.addArg( "--destination", destinationDir.getAbsolutePath() );
        }

        Iterator it = config.getIgnores().iterator();
        while ( it.hasNext() )
        {
            String ignore = (String) it.next();
            cmdLineArgs.addArg( "--ignore", ignore );
        }

        String includes = joinCludes( config.getIncludes() );
        String excludes = joinCludes( config.getExcludes() );
        String defaultExcludes = joinCludes( FileUtils.getDefaultExcludesAsList() );

        if ( StringUtils.isNotEmpty( excludes ) )
        {
            excludes += "," + defaultExcludes;
        }
        else
        {
            excludes = defaultExcludes;
        }

        try
        {
            if ( getLog().isDebugEnabled() )
            {
                getLog().debug( "Config : " + config );
                getLog().debug( "Basedir: " + config.getBasedir() );
                getLog().debug( "Include: " + includes );
                getLog().debug( "Exclude: " + excludes );
            }

            List filenames = FileUtils.getFileNames( config.getBasedir(), includes, excludes, false );

            if ( filenames.isEmpty() )
            {
                getLog().warn( "No files to instrument." );
                return;
            }

            cmdLineArgs.addArg( "--basedir", config.getBasedir().getAbsolutePath() );
            it = filenames.iterator();
            while ( it.hasNext() )
            {
                String filename = (String) it.next();
                if ( getLog().isDebugEnabled() )
                {
                    getLog().debug( "To Instrument: " + filename );
                }
                cmdLineArgs.addArg( filename );
            }
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Unable to obtain file list from includes/excludes.", e );
        }

        int returnCode = executeJava();

        // Check the return code and print a message
        if ( returnCode == 0 )
        {
            if ( !isQuiet() )
            {
                getLog().info( "Instrumentation was successful." );
            }
        }
        else
        {
            throw new MojoExecutionException( "Unable to instrument project." );
        }

    }

    public ConfigInstrumentation getConfig()
    {
        return config;
    }

    public File getDataFile()
    {
        return dataFile;
    }

    public File getDestinationDir()
    {
        return destinationDir;
    }

    public String joinCludes( List cludes )
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = cludes.iterator();
        while ( it.hasNext() )
        {
            String clude = (String) it.next();
            sb.append( clude );
            if ( it.hasNext() )
            {
                sb.append( "," );
            }
        }
        return sb.toString();
    }

    public void setConfig( ConfigInstrumentation config )
    {
        this.config = config;
    }

    public void setDataFile( File dataFile )
    {
        this.dataFile = dataFile;
    }

    public void setDestinationDir( File destinationDir )
    {
        this.destinationDir = destinationDir;
    }

}
