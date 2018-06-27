/*
 * #%L
 * Mojo's Maven plugin for Cobertura
 * %%
 * Copyright (C) 2005 - 2018 Codehaus
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

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.mojo.cobertura.tasks.CommandLineArguments;
import org.codehaus.mojo.cobertura.tasks.ReportTask;

/**
 * Generate (cobertura.xml) report from previously generated data (cobertura.ser) file.
 *
 * @author <a href="mailto:msiemczyk@live.ca">Maciek Siemczyk</a>
 * @goal generate-report
 */
public class CoberturaGenerateReportMojo extends AbstractCoberturaMojo
{
    /**
     * The output directory for the report.
     *
     * @parameter default-value="${project.build.directory}/cobertura"
     * @required
     */
    private File outputDirectory;
    
    /**
     * The encoding for the java source code files.
     *
     * @parameter expression="${project.build.sourceEncoding}" default-value="UTF-8".
     */
    private String encoding;

    /**
     * Build up a command line from the parameters and run Cobertura XML report generation against data file.
     *
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException
    {
        if ( skipMojo() )
        {
            return;
        }

        ArtifactHandler artifactHandler = getProject().getArtifact().getArtifactHandler();
        if ( !"java".equals( artifactHandler.getLanguage() ) )
        {
            getLog().info(
                "Not executing cobertura:generate-report as the project is not a Java classpath-capable package" );
            return;
        }
        
        if ( !getDataFile().exists() )
        {
            getLog().info( "Cannot generate report, instrumentation not performed - skipping." );
            return;
        }
            
        ReportTask task = new ReportTask();
        setTaskDefaults( task );

        task.setOutputFormat( "xml" );
        task.setDataFile( getDataFile() );
        task.setOutputDirectory( outputDirectory );
        task.setCompileSourceRoots( getCompileSourceRoots() );
        task.setSourceEncoding( encoding );

        CommandLineArguments commandLineArguments = new CommandLineArguments();
        commandLineArguments.setUseCommandsFile( true );
        
        task.setCmdLineArgs( commandLineArguments );

        try
        {
            task.execute();
        }
        catch ( MojoExecutionException e )
        {
            // throw new MavenReportException( "Error in Cobertura Report generation: " + e.getMessage(), e );
            // better don't break the build if report is not generated, also due to the sporadic MCOBERTURA-56
            getLog().error( "Error in Cobertura Report generation: " + e.getMessage(), e );
        }
    }

    @SuppressWarnings( "unchecked" )
    private List<String> getCompileSourceRoots()
    {
        return getProject().getExecutionProject().getCompileSourceRoots();
    }
}