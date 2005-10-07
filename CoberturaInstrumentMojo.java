package org.apache.maven.plugin.cobertura;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

import java.io.*;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @author <a href="will.gwaltney@sas.com">Will Gwaltney</a>
 * @goal instrument
 * @phase compile
 * @description instrument the code
 */
public class CoberturaInstrumentMojo extends AbstractMojo
{
	// properties from pom.xml that we're going to pass to 
    // Cobertura on the command line
    
    /**
     * @parameter expression="${project.plugins.plugin.configuration.classpath}"
     * @required
     */
    private String classpath;
    
    /**
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     */
    private String basedir = null;

    /**
     * @parameter expression="${project.plugins.plugin.configuration.destination}"
     */
	private String destination = null;

    /**
     * @parameter expression="${project.plugins.plugin.configuration.ignore}"
     */
	private String ignore = null;
    
    /**
     * @parameter expression="${project.plugins.plugin.configuration.datafile}"
     */    
    private String datafile = null;
    
    /**
     * @parameter expression="${project.plugins.plugin.configuration.reportdirectory}"
     * This mojo doesn't actually use this parameter, but it looks like it has to be
     * listed here anyway or else the POM file parser will scream.  BTW, it's used by
     * the Cobertura report mojo.
     */    
    private String reportdirectory = null;
    
	/**
	 * build up a command line from the parameters and run
     * Cobertura to instrument the code.
	 **/
    public void execute()
        throws MojoExecutionException
    {
        int returnCode = 0;
        StreamConsumerToList consumer = new StreamConsumerToList();
        
        getLog().info("raw classpath = " + classpath); // TEMPORARY!!!
        try {
            classpath = Utils.expandClasspath(classpath);
        } catch ( IOException e ) {
            throw new MojoExecutionException( "Error expanding the classpath", e );
        }
        getLog().info("expanded classpath = " + classpath); // TEMPORARY!!!
        
        // build up the command line and run Cobertura
        try {
            Commandline cl = new Commandline();
            cl.setExecutable("java");
            if (classpath.length() > 0) {
                cl.createArgument().setValue("-cp");
                cl.createArgument().setValue(classpath);
            }
            cl.createArgument().setValue("net.sourceforge.cobertura.instrument.Main");
            // cl.createArgument().setValue("--basedir");
            // cl.createArgument().setValue(basedir);
            
            if (destination != null) {
                cl.createArgument().setValue("--destination");
                cl.createArgument().setValue(destination);
            }
            
            if (ignore != null) {
                cl.createArgument().setValue("--ignore");
                cl.createArgument().setValue(ignore);
            }
            
            if (datafile != null) {
                cl.createArgument().setValue("--datafile");
                cl.createArgument().setValue(datafile);
            }

            // for now, instrument all the classes in the base directory tree
            cl.createArgument().setValue(basedir);
            
            getLog().info( "Command line = " + Commandline.toString( cl.getCommandline() ) );
            getLog().info( "Working directory = " + cl.getWorkingDirectory() );
            returnCode = CommandLineUtils.executeCommandLine(   cl, 
                                                                consumer, 
                                                                consumer);
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException("Error invoking Cobertura", e);
        }

        String results = consumer.getLinesAsString();
        if (returnCode == 0) {
            //success
            getLog().info("Cobertura instrumentation succeeded. Details:\n" + 
                    results);
        } else {
            //failure
            throw new MojoExecutionException("Cobertura instrumentation failed. Details:\n" + 
                    results);
        }
    }
    
}
