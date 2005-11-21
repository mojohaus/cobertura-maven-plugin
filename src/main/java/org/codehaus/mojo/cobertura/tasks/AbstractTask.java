package org.codehaus.mojo.cobertura.tasks;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.wagon.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Base Abstract Class for all of the Tasks.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public abstract class AbstractTask
{
    protected CommandLineArguments cmdLineArgs;

    private List compileClasspathList;

    private Log log;

    private String maxmem;

    private List pluginClasspathList;

    private String taskClass;
    
    private static File log4jconfigFile = null;

    /**
     * Initialize AbstractTask.
     * 
     * @param taskClassname the classname for the task.
     */
    public AbstractTask( String taskClassname )
    {
        this.taskClass = taskClassname;
        this.cmdLineArgs = new CommandLineArguments();
        this.maxmem = "64m";
        
        if(log4jconfigFile == null) {
            try
            {
                log4jconfigFile = File.createTempFile("log4j", "config.properties");
                URL log4jurl = this.getClass().getClassLoader().getResource("cobertura-plugin/log4j-debug.properties");
                FileUtils.copyURLToFile(log4jurl, log4jconfigFile);
            }
            catch ( IOException e )
            {
                log4jconfigFile = null;
            }
        }
    }

    /**
     * Using the <code>${project.compileClasspathElements}</code> and the
     * <code>${plugin.artifacts}</code>, create a classpath string that is
     * suitable to be used from a forked cobertura process.
     * 
     * @return the classpath string
     * @throws MojoExecutionException if the pluginArtifacts cannot be properly
     *             resolved to a full system path.
     */
    public String createClasspath()
        throws MojoExecutionException
    {

        StringBuffer cpBuffer = new StringBuffer();

        for ( Iterator it = compileClasspathList.iterator(); it.hasNext(); )
        {
            cpBuffer.append( (String) it.next() );

            if ( it.hasNext() )
            {
                cpBuffer.append( File.pathSeparator );
            }
        }

        for ( Iterator it = pluginClasspathList.iterator(); it.hasNext(); )
        {
            Artifact artifact = (Artifact) it.next();

            try
            {
                cpBuffer.append( File.pathSeparator ).append( artifact.getFile().getCanonicalPath() );
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Error while creating the canonical path for '" + artifact.getFile()
                    + "'.", e );
            }
        }

        return cpBuffer.toString();
    }

    public abstract void execute()
        throws MojoExecutionException;

    protected int executeJava()
        throws MojoExecutionException
    {
        Commandline cl = new Commandline();
        cl.setExecutable( "java" );
        cl.createArgument().setValue( "-cp" );
        cl.createArgument().setValue( createClasspath() );
        
        if(log4jconfigFile != null) {
            try
            {
                cl.createArgument().setValue("-Dlog4j.configuration=" + log4jconfigFile.toURL().toExternalForm());
            }
            catch ( MalformedURLException e )
            {
                // ignore
            }
        }
        
        cl.createArgument().setValue( "-Xmx" + maxmem );

        cl.createArgument().setValue( this.taskClass );
        
        if ( this.cmdLineArgs.useCommandsFile() )
        {
            cl.createArgument().setValue( "--commandsfile" );
            try
            {
                String commandsFile = this.cmdLineArgs.getCommandsFile();
                cl.createArgument().setValue( commandsFile );
                FileUtils.copyFile( new File( commandsFile ), new File( commandsFile + ".bak" ) );
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Unable to obtain CommandsFile location.", e );
            }
        }
        else
        {
            Iterator it = this.cmdLineArgs.iterator();
            while ( it.hasNext() )
            {
                cl.createArgument().setValue( it.next().toString() );
            }
        }

        CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();

        CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

        // TODO: drop these to debug level.
        getLog().info( "Working Directory: " + cl.getWorkingDirectory() );
        getLog().info( "Executing command line:" );
        getLog().info( cl.toString() );

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

        getLog().debug( "--------------------" );
        getLog().debug( " Standard output from the Cobertura task:" );
        getLog().debug( "--------------------" );
        getLog().info( stdout.getOutput() );
        getLog().debug( "--------------------" );

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

    public CommandLineArguments getCmdLineArgs()
    {
        return cmdLineArgs;
    }

    /**
     * @return Returns the compileClasspathList.
     */
    public List getCompileClasspathList()
    {
        return compileClasspathList;
    }

    public Log getLog()
    {
        if ( log == null )
        {
            log = new SystemStreamLog();
        }

        return log;
    }

    public String getMaxmem()
    {
        return maxmem;
    }

    /**
     * @return Returns the pluginClasspathList.
     */
    public List getPluginClasspathList()
    {
        return pluginClasspathList;
    }

    public void setCmdLineArgs( CommandLineArguments cmdLineArgs )
    {
        this.cmdLineArgs = cmdLineArgs;
    }

    /**
     * @param compileClasspathList The compileClasspathList to set.
     */
    public void setCompileClasspathList( List compileClasspathList )
    {
        this.compileClasspathList = compileClasspathList;
    }

    public void setLog( Log log )
    {
        this.log = log;
    }

    public void setMaxmem( String maxmem )
    {
        this.maxmem = maxmem;
    }

    /**
     * @param pluginClasspathList The pluginClasspathList to set.
     */
    public void setPluginClasspathList( List pluginClasspathList )
    {
        this.pluginClasspathList = pluginClasspathList;
    }
}
