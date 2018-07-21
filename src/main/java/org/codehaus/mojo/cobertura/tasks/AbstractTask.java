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
package org.codehaus.mojo.cobertura.tasks;

import org.apache.commons.lang.SystemUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Base Abstract Class for all of the Tasks.
 *
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public abstract class AbstractTask
{
    /**
     * The shared command line args.
     */
    protected CommandLineArguments cmdLineArgs;

    private Log log;

    private String maxmem;

    private List<Artifact> pluginClasspathList;

    private String taskClass;

    private boolean quiet;

    /**
     * Initialize AbstractTask.
     *
     * @param taskClassname the classname for the task.
     */
    protected AbstractTask( String taskClassname )
    {
        taskClass = taskClassname;
        cmdLineArgs = new CommandLineArguments();
        maxmem = "64m";
    }

    /**
     * Setter for <code>quiet</code>.
     *
     * @param quiet The quiet to set.
     */
    public void setQuiet( boolean quiet )
    {
        this.quiet = quiet;
    }

    /**
     * Getter for <code>quiet</code>.
     *
     * @return Returns the quiet.
     */
    public boolean isQuiet()
    {
        return quiet;
    }

    /**
     * Using the <code>${project.compileClasspathElements}</code> and the <code>${plugin.artifacts}</code>, create
     * a classpath string that is suitable to be used from a forked cobertura process.
     *
     * @return the classpath string
     * @throws MojoExecutionException if the pluginArtifacts cannot be properly resolved to a full system path.
     */
    public String createClasspath()
        throws MojoExecutionException
    {

        StringBuffer cpBuffer = new StringBuffer();

        for ( Iterator<Artifact> it = pluginClasspathList.iterator(); it.hasNext(); )
        {
            Artifact artifact = it.next();

            try
            {
                cpBuffer.append( File.pathSeparator ).append( artifact.getFile().getCanonicalPath() );
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException(
                    "Error while creating the canonical path for '" + artifact.getFile() + "'.", e );
            }
        }

        return cpBuffer.toString();
    }

    private String getLogbackConfigFilePath()
    {
        String resourceName = "cobertura-plugin/logback-info.xml";
        
        if ( getLog().isDebugEnabled() )
        {
            resourceName = "cobertura-plugin/logback-debug.xml";
        }
        
        if ( quiet )
        {
            resourceName = "cobertura-plugin/logback-error.xml";
        }

        String path = null;
        try
        {
            final URL configUrl = this.getClass().getClassLoader().getResource( resourceName );
            
            File configFile = File.createTempFile( "logback", ".xml" );
            configFile.deleteOnExit();
            
            FileUtils.copyURLToFile( configUrl, configFile );
            
            path = configFile.toURI().toURL().toExternalForm();
        }
        catch ( MalformedURLException e )
        {
            // ignore
        }
        catch ( IOException e )
        {
            // ignore
        }
        
        return path;
    }

    /**
     * Run the task.
     *
     * @throws MojoExecutionException for a full-out execution problem.
     * @throws MojoFailureException   for an anticipated failure.
     */
    public abstract void execute()
        throws MojoExecutionException, MojoFailureException;

    /**
     * Run a jvm to execute something.
     *
     * @return the exit code.
     * @throws MojoExecutionException for an error launching the jvm.
     */
    protected int executeJava()
        throws MojoExecutionException
    {
        Commandline cl = new Commandline();
        File java = new File( SystemUtils.getJavaHome(), "bin/java" );
        cl.setExecutable( java.getAbsolutePath() );
        cl.addEnvironment( "CLASSPATH", createClasspath() );

        final String logbackConfig = getLogbackConfigFilePath();
        if ( logbackConfig != null )
        {
            cl.createArg().setValue( "-Dlogback.configurationFile=" + logbackConfig );
        }
        
        cl.createArg().setValue( "-Xmx" + maxmem );
        cl.createArg().setValue( taskClass );

        if ( cmdLineArgs.useCommandsFile() )
        {
            String commandsFile;
            try
            {
                commandsFile = cmdLineArgs.getCommandsFile();
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Unable to obtain CommandsFile location.", e );
            }
            if ( FileUtils.fileExists( commandsFile ) )
            {
                cl.createArg().setValue( "--commandsfile" );
                cl.createArg().setValue( commandsFile );
            }
            else
            {
                throw new MojoExecutionException( "CommandsFile doesn't exist: " + commandsFile );
            }
        }
        else
        {
            Iterator<String> it = cmdLineArgs.iterator();
            while ( it.hasNext() )
            {
                cl.createArg().setValue( it.next() );
            }
        }

        CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();

        CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

        if ( quiet )
        {
            CommandLineUtils.StringStreamConsumer nullConsumer = new CommandLineUtils.StringStreamConsumer()
            {
                public void consumeLine( String line )
                {
                    // swallow
                }
            };
            stdout = nullConsumer;
            stderr = nullConsumer;
        }

        getLog().debug( "Working Directory: " + cl.getWorkingDirectory() );
        try
        {
            String[] environmentVariables = cl.getEnvironmentVariables();
            for ( String environmentVariable : environmentVariables )
            {
                getLog().debug( "Environment variable: " + environmentVariable );
            }
        }
        catch( CommandLineException e ) {
            // Ignore
        }
        getLog().debug( "Executing command line:" );
        getLog().debug( cl.toString() );

        int exitCode;
        try
        {
            exitCode = CommandLineUtils.executeCommandLine( cl, stdout, stderr );
        }
        catch ( CommandLineException e )
        {
            throw new MojoExecutionException( "Unable to execute Cobertura.", e );
        }

        getLog().debug( "exit code: " + exitCode );

        String output = stdout.getOutput();

        if ( output.trim().length() > 0 )
        {
            getLog().debug( "--------------------" );
            getLog().debug( " Standard output from the Cobertura task:" );
            getLog().debug( "--------------------" );
            getLog().info( output );
            getLog().debug( "--------------------" );
        }

        String stream = stderr.getOutput();

        if ( stream.trim().length() > 0 )
        {
            getLog().debug( "--------------------" );
            getLog().debug( " Standard error from the Cobertura task:" );
            getLog().debug( "--------------------" );
            getLog().error( stderr.getOutput() );
            getLog().debug( "--------------------" );
        }

        return exitCode;
    }

    /**
     * Return the command line args.
     *
     * @return the command line args.
     */
    public CommandLineArguments getCmdLineArgs()
    {
        return cmdLineArgs;
    }

    /**
     * @return a log object.
     */
    public Log getLog()
    {
        if ( log == null )
        {
            log = new SystemStreamLog();
        }

        return log;
    }

    /**
     * @return the configured -Xmx option.
     */
    public String getMaxmem()
    {
        return maxmem;
    }

    /**
     * @return Returns the pluginClasspathList.
     */
    public List<Artifact> getPluginClasspathList()
    {
        return pluginClasspathList;
    }

    /**
     * Set the entire command line args.
     *
     * @param cmdLineArgs new args.
     */
    public void setCmdLineArgs( CommandLineArguments cmdLineArgs )
    {
        this.cmdLineArgs = cmdLineArgs;
    }

    /**
     * Set the logger.
     *
     * @param log the new logger.
     */
    public void setLog( Log log )
    {
        this.log = log;
    }

    /**
     * Set the -Xmx value for the jvm.
     *
     * @param maxmem the memory size.
     */
    public void setMaxmem( String maxmem )
    {
        this.maxmem = maxmem;
    }

    /**
     * @param pluginClasspathList The pluginClasspathList to set.
     */
    public void setPluginClasspathList( List<Artifact> pluginClasspathList )
    {
        this.pluginClasspathList = Collections.unmodifiableList( pluginClasspathList );
    }
}
