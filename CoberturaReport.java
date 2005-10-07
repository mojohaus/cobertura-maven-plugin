package org.apache.maven.plugin.cobertura;

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
import java.io.IOException;
import java.util.Locale;

import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.codehaus.doxia.site.renderer.SiteRenderer;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @goal report
 *
 * @author <a href="will.gwaltney@sas.com">Will Gwaltney</a>
 */
public class CoberturaReport
    extends AbstractMavenReport
{
    /**
     * @parameter expression="${project.plugins.plugin.configuration.classpath}"
     * @required
     */
    private String classpath;
    
    /**
     * @parameter expression="${project.plugins.plugin.configuration.reportFormat}"
     */
    private String format;

    /**
     * @parameter expression="${project.plugins.plugin.configuration.datafile}"
     */    
    private String datafile;

    /**
     * @parameter expression="${project.plugins.plugin.configuration.reportdirectory}"
     * @required
     */
    private String reportdirectory;

    
    /**
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     * @readonly
     */
    private String source;

    /**
     * @parameter expression="${project.build.sourceDirectory}/site"
     * @required
     */
    private String outputDirectory;

    /**
     * @parameter expression="${component.org.codehaus.doxia.site.renderer.SiteRenderer}"
     * @required
     * @readonly
     */
    private SiteRenderer siteRenderer;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${project.plugins.plugin.configuration.ignore}"
     * This mojo doesn't actually use this parameter, but it looks like it has to be
     * listed here anyway or else the POM file parser will scream.  BTW, it's used by
     * the Cobertura instrument mojo.
     */
    private String ignore = null;
    
    /**
     * @see org.apache.maven.reporting.MavenReport#getName()
     */
    public String getName(Locale locale)
    {
        return "CoberturaReport";
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getDescription()
     */
    public String getDescription(Locale locale)
    {
        return "Cobertura coverage report.";
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
        executeReport( locale);
    }

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
     */
    protected void executeReport( Locale locale )
        throws MavenReportException
    {
        try
        {
            File outputDir = new File( getReportOutputDirectory().getAbsolutePath() + "/cobertura" );
            outputDir.mkdirs();
        }
        catch ( Exception e )
        {
            throw new MavenReportException("Error invoking Cobertura", e);
        }

        int returnCode = 0;
        StreamConsumerToList consumer = new StreamConsumerToList();
        
        getLog().info("raw classpath = " + classpath); // TEMPORARY!!!
        try {
            classpath = Utils.expandClasspath(classpath);
        } catch ( IOException e ) {
            throw new MavenReportException( "Error expanding the classpath", e );
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
            cl.createArgument().setValue("net.sourceforge.cobertura.reporting.Main");
            
            cl.createArgument().setValue("--source");
            cl.createArgument().setValue(source);
            
            cl.createArgument().setValue("--destination");
            cl.createArgument().setValue(reportdirectory);
            
            if (datafile != null) {
                cl.createArgument().setValue("--datafile");
                cl.createArgument().setValue(datafile);
            }

            if (format != null) {
                cl.createArgument().setValue("--format");
                cl.createArgument().setValue(format);
            }

            getLog().info(Commandline.toString(cl.getCommandline()));
            returnCode = CommandLineUtils.executeCommandLine(   cl, 
                                                                consumer, 
                                                                consumer);
        }
        catch ( Exception e )
        {
            throw new MavenReportException("Error invoking Cobertura", e);
        }

        String results = consumer.getLinesAsString();
        if (returnCode == 0) {
            //success
            getLog().info("Cobertura instrumentation succeeded. Details:\n" + 
                    results);
        } else {
            //failure
            throw new MavenReportException("Cobertura instrumentation failed. Details:\n" + 
                    results);
        }
    }

    /**
     * @see org.apache.maven.reporting.MavenReport#getOutputName()
     */
    public String getOutputName()
    {
        return "cobertura/index";
    }
}
