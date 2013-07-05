package org.codehaus.mojo.cobertura.tasks;

/*
 * Copyright 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    /**
     * {@inheritDoc}
     */
    public void execute()
        throws MojoExecutionException
    {
        /* this task should always use the commands file approach */
        cmdLineArgs.setUseCommandsFile( true );

        if ( StringUtils.isNotEmpty( config.getMaxmem() ) )
        {
            this.setMaxmem( config.getMaxmem() );
        }
        
        if ( dataFile != null )
        {
            cmdLineArgs.addArg( "--datafile", dataFile.getAbsolutePath() );
        }

        if ( destinationDir != null )
        {
            cmdLineArgs.addArg( "--destination", destinationDir.getAbsolutePath() );
        }

        if (config.getIgnoreTrivial()) 
        {
        	cmdLineArgs.addArg( "--ignoreTrivial");
        }
        
        for ( String ignoreMethodAnnotation : config.getIgnoreMethodAnnotations() )
        {
            cmdLineArgs.addArg( "--ignoreMethodAnnotation", ignoreMethodAnnotation );
        }

        for ( String ignore : config.getIgnores() )
        {
            cmdLineArgs.addArg( "--ignore", ignore );
        }

        String includes = joinCludes( config.getIncludes() );
        String excludes = joinCludes( config.getExcludes() );
        @SuppressWarnings( "unchecked" )
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
                getLog().debug( "Max Mem: " + config.getMaxmem() );
            }

            @SuppressWarnings( "unchecked" )
            List<String> filenames = FileUtils.getFileNames( config.getBasedir(), includes, excludes, false );

            if ( filenames.isEmpty() )
            {
                getLog().warn( "No files to instrument." );
                return;
            }

            cmdLineArgs.addArg( "--basedir", config.getBasedir().getAbsolutePath() );
            for ( String filename : filenames )
            {
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

    /**
     * @return the config.
     */
    public ConfigInstrumentation getConfig()
    {
        return config;
    }

    /**
     * @return the data file
     */
    public File getDataFile()
    {
        return dataFile;
    }

    /**
     * @return the destination directory.
     */
    public File getDestinationDir()
    {
        return destinationDir;
    }

    /**
     * Construct a ,-separated string from a list of strings.
     * @param cludes the list of strings.
     * @return the ,-separated string.
     */
    public String joinCludes( List<String> cludes )
    {
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = cludes.iterator();
        while ( it.hasNext() )
        {
            String clude = it.next();
            sb.append( clude );
            if ( it.hasNext() )
            {
                sb.append( "," );
            }
        }
        return sb.toString();
    }

    /**
     * Set the configuration
     * @param config the configuration
     */
    public void setConfig( ConfigInstrumentation config )
    {
        this.config = config;
    }

    /**
     * Set the data file
     * @param dataFile the data file
     */
    public void setDataFile( File dataFile )
    {
        this.dataFile = dataFile;
    }

    /**
     * Set the destination directory
     * @param destinationDir the directory
     */
    public void setDestinationDir( File destinationDir )
    {
        this.destinationDir = destinationDir;
    }

    @Override
    public String createClasspath() throws MojoExecutionException 
    {
        return this.config.getBasedir().getAbsolutePath() + super.createClasspath();
    }

    
}
