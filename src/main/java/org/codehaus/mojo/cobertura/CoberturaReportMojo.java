package org.codehaus.mojo.cobertura;

/*
 * Copyright 2004-2005 The Apache Software Foundation.
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
import java.util.List;
import java.util.Locale;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.codehaus.doxia.site.renderer.SiteRenderer;
import org.codehaus.mojo.cobertura.tasks.ReportTask;

/**
 * Instruments, Tests, and Generates a Cobertura Report.
 * 
 * @author <a href="will.gwaltney@sas.com">Will Gwaltney</a>
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * 
 * @goal cobertura
 * @configurator override
 * @execute phase="test" lifecycle="cobertura"
 */
public class CoberturaReportMojo
    extends AbstractMavenReport
{
    /**
     * The format of the report.
     * (supports 'html' or 'xml'. defaults to 'html')
     * 
     * @parameter expression="${cobertura.report.format}"
     */
    private String format;

    /**
     * Maximum memory to pass JVM of Cobertura processes.
     * 
     * @parameter expression="${cobertura.maxmem}"
     */
    private String maxmem = "64m";

    /**
     * <p>The Datafile Location.</p>
     * 
     * <p>
     * Due to a bug in Cobertura v1.6, this location cannot be changed.
     * </p>
     * 
     * @parameter expression="${basedir}/cobertura.ser"
     * @required
     * @readonly
     */
    protected File dataFile;

    /**
     * <i>Maven Internal</i>: List of artifacts for the plugin.
     * 
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    protected List pluginClasspathList;

    /**
     * The source directory for generating report from.
     * 
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     * @readonly
     */
    private File sourceDirectory;

    /**
     * The output directory for the report.
     * 
     * @parameter expression="${project.reporting.outputDirectory}/cobertura"
     * @required
     */
    private String outputDirectory;

    /**
     * <i>Maven Internal</i>: The Doxia Site Renderer.
     * 
     * @parameter expression="${component.org.codehaus.doxia.site.renderer.SiteRenderer}"
     * @required
     * @readonly
     */
    private SiteRenderer siteRenderer;

    /**
     * <i>Maven Internal</i>: Project to interact with.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @see org.apache.maven.reporting.MavenReport#getName(java.util.Locale)
     */
    public String getName( Locale locale )
    {
        return "Cobertura Test Coverage";
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getDescription(java.util.Locale)
     */
    public String getDescription( Locale locale )
    {
        return "Cobertura Test Coverage Report.";
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getOutputDirectory()
     */
    protected String getOutputDirectory()
    {
        return outputDirectory;
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getProject()
     */
    protected MavenProject getProject()
    {
        return project;
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getSiteRenderer()
     */
    protected SiteRenderer getSiteRenderer()
    {
        return siteRenderer;
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#generate(org.codehaus.doxia.sink.Sink, java.util.Locale)
     */
    public void generate( Sink sink, Locale locale )
        throws MavenReportException
    {
        executeReport( locale );
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
     */
    protected void executeReport( Locale locale )
        throws MavenReportException
    {
        ReportTask task = new ReportTask();
        // task defaults
        task.setLog( getLog() );
        task.setPluginClasspathList( pluginClasspathList );

        // task specifics
        task.setMaxmem( maxmem );
        task.setDataFile( dataFile );
        task.setOutputDirectory( new File( outputDirectory ) );
        task.setSourceDirectory( sourceDirectory );
        task.setOutputFormat( format );

        // execute task
        try
        {
            task.execute();
        }
        catch ( MojoExecutionException e )
        {
            throw new MavenReportException( "Error in Cobertura Report generation.", e );
        }
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getOutputName()
     */
    public String getOutputName()
    {
        return "cobertura/index";
    }

    public boolean isExternalReport()
    {
        return true;
    }
}
