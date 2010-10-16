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

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
 * @goal cobertura
 * @execute phase="test" lifecycle="cobertura"
 */
public class CoberturaReportMojo
    extends AbstractMavenReport
{
    /**
     * The format of the report. (supports 'html' or 'xml'. defaults to 'html')
     * 
     * @parameter expression="${cobertura.report.format}"
     * @deprecated
     */
    private String format;

    /**
     * The format of the report. (can be 'html' and/or 'xml'. defaults to 'html')
     * 
     * @parameter
     */
    private String[] formats = new String[] { "html" };

    /**
     * The encoding for the java source code files.
     * 
     * @parameter expression="${project.build.sourceEncoding}" default-value="UTF-8".
     * @since 2.4
     */
    private String encoding;

    /**
     * Maximum memory to pass to JVM of Cobertura processes.
     * 
     * @parameter expression="${cobertura.maxmem}"
     */
    private String maxmem = "64m";

    /**
     * <p>
     * The Datafile Location.
     * </p>
     * 
     * @parameter expression="${cobertura.datafile}" default-value="${project.build.directory}/cobertura/cobertura.ser"
     * @required
     * @readonly
     */
    protected File dataFile;

    /**
     * <i>Maven Internal</i>: List of artifacts for the plugin.
     * 
     * @parameter default-value="${plugin.artifacts}"
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
     * Only output cobertura errors, avoid info messages.
     * 
     * @parameter expression="${quiet}" default-value="false"
     * @since 2.1
     */
    private boolean quiet;

    /**
     * <i>Maven Internal</i>: The Doxia Site Renderer.
     * 
     * @component
     */
    private SiteRenderer siteRenderer;

    /**
     * <i>Maven Internal</i>: Project to interact with.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @see org.apache.maven.reporting.MavenReport#getName(java.util.Locale)
     * @param locale for the message bundle
     * @return localized cobertura name
     */
    public String getName( Locale locale )
    {
        return getBundle( locale ).getString( "report.cobertura.name" );
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getDescription(java.util.Locale)
     * @param locale for the message bundle
     * @return localized description
     */
    public String getDescription( Locale locale )
    {
        return getBundle( locale ).getString( "report.cobertura.description" );
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getOutputDirectory()
     * @return the output directory
     */
    protected String getOutputDirectory()
    {
        return outputDirectory.getAbsolutePath();
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getProject()
     * @return current MavenProject
     */
    protected MavenProject getProject()
    {
        return project;
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getSiteRenderer()
     * @return SiteRenderer
     */
    protected SiteRenderer getSiteRenderer()
    {
        return siteRenderer;
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#generate(org.codehaus.doxia.sink.Sink, java.util.Locale)
     * @param locale for the message bundle
     */
    public void generate( Sink sink, Locale locale )
        throws MavenReportException
    {
        executeReport( locale );
    }

    /**
     * perform the actual reporting
     * @param task
     * @param format
     * @throws MavenReportException
     */
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
            // throw new MavenReportException( "Error in Cobertura Report generation: " + e.getMessage(), e );
            // better don't break the build if report is not generated, also due to the sporadic MCOBERTURA-56
            getLog().error( "Error in Cobertura Report generation: " + e.getMessage(), e );
        }
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
     * @param locale not used
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
        task.setQuiet( quiet );

        // task specifics
        task.setMaxmem( maxmem );
        task.setDataFile( dataFile );
        task.setOutputDirectory( outputDirectory );
        task.setCompileSourceRoots( getCompileSourceRoots() );
        task.setSourceEncoding( encoding );


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

    /**
     * @return always return <code>true</code> for this report
     */
    public boolean isExternalReport()
    {
        return true;
    }

    /**
     * @return <code>true</code> if there is a datafile to create a report for.
     */
    public boolean canGenerateReport()
    {
        /*
         * Don't have to check for source directories or java code or the like for report generation. Checks for source
         * directories or java project classpath existence should only occur in the Instrument Mojo.
         */
        if ( dataFile == null || !dataFile.exists() )
        {
            getLog().info(
                           "Not executing cobertura:report as the cobertura data file (" + dataFile
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

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#setReportOutputDirectory(java.io.File)
     */
    public void setReportOutputDirectory( File reportOutputDirectory )
    {
        if ( ( reportOutputDirectory != null ) && ( !reportOutputDirectory.getAbsolutePath().endsWith( "cobertura" ) ) )
        {
            this.outputDirectory = new File( reportOutputDirectory, "cobertura" );
        }
        else
        {
            this.outputDirectory = reportOutputDirectory;
        }
    }

    /**
     * Gets the resource bundle for the report text.
     * 
     * @param locale The locale for the report, must not be <code>null</code>.
     * @return The resource bundle for the requested locale.
     */
    private ResourceBundle getBundle( Locale locale )
    {
        return ResourceBundle.getBundle( "cobertura-report", locale, getClass().getClassLoader() );
    }

}
