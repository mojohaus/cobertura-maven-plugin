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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.codehaus.doxia.site.renderer.SiteRenderer;
import org.codehaus.mojo.cobertura.tasks.ReportTask;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Instruments, Tests, and Generates a Cobertura Report.
 *
 * @author <a href="will.gwaltney@sas.com">Will Gwaltney</a>
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @goal cobertura
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
     * @deprecated
     */
    private String format;

    /**
     * The format of the report.
     * (can be 'html' and/or 'xml'. defaults to 'html')
     *
     * @parameter
     */
    private String[] formats = new String[]{"html"};

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
     * @readonly TODO Please link a Cobertura issue URL so other developers understand
     * what the problem is and can fix this once the underlying Cobertura bug is fixed.
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
     * The output directory for the report.
     *
     * @parameter default-value="${project.reporting.outputDirectory}/cobertura"
     * @required
     */
    private File outputDirectory;

    /**
     * <i>Maven Internal</i>: The Doxia Site Renderer.
     *
     * @component
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
        return outputDirectory.getAbsolutePath();
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

    private void executeReportTask( ReportTask task, String format )
        throws MavenReportException
    {
        task.setOutputFormat( format );

        // execute task
        try
        {
            task.execute();
        }
        catch ( MojoExecutionException e )
        {
            throw new MavenReportException( "Error in Cobertura Report generation: " + e.getMessage(), e );
        }
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
     */
    protected void executeReport( Locale locale )
        throws MavenReportException
    {
        if ( !canGenerateReport() )
        {
            return;
        }

        ReportTask task = new ReportTask();

        // task defaults
        task.setLog( getLog() );
        task.setPluginClasspathList( pluginClasspathList );

        // task specifics
        task.setMaxmem( maxmem );
        task.setDataFile( dataFile );
        task.setOutputDirectory( outputDirectory );
        task.setCompileSourceRoots( getCompileSourceRoots() );

        if ( format != null )
        {
            formats = new String[] { format };
        }

        for ( int i = 0; i < formats.length; i++ )
        {
            executeReportTask( task, formats[i] );
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

    public boolean canGenerateReport()
    {
        // Don't have to check for source directories or java code or the like for report generation.
        // Checks for source directories or java project classpath existance should only occur in the
        // Instrument Mojo.
        if ( dataFile == null || !dataFile.exists() )
        {
            getLog().info( "Not executing cobertura:report as the cobertura data file (" + dataFile
                               + ") could not be found" );
            return false;
        }
        else
        {
            return true;
        }
    }

    private List getCompileSourceRoots()
    {
        return project.getExecutionProject().getCompileSourceRoots();
    }
}
