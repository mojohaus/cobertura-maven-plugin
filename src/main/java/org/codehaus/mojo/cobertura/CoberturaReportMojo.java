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
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler;
import net.sourceforge.cobertura.coveragedata.ProjectData;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.codehaus.mojo.cobertura.tasks.CommandLineArguments;
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
     * Generate aggregate reports in multi-module projects.
     * 
     * @parameter expression="${cobertura.aggregate}" default-value="false"
     */
    private boolean aggregate;
    
    /**
     * <i>Maven Internal</i>: The Doxia Site Renderer.
     * 
     * @component
     */
    private Renderer siteRenderer;

    /**
     * List of maven project of the current build
     *
     * @parameter expression="${reactorProjects}"
     * @required
     * @readonly
     */
    protected List reactorProjects;
    
    /**
     * <i>Maven Internal</i>: Project to interact with.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    private Map projectChildren;
    private String relDataFileName;
    private String relAggregateOutputDir;
    
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
    protected Renderer getSiteRenderer()
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
        if ( canGenerateSimpleReport() )
        {
            executeReport(dataFile, outputDirectory, getCompileSourceRoots());
        }
        
        if(canGenerateAggregateReports()) {
            executeAggregateReport(locale);
        }
    }

    /**
     * Generates aggregate cobertura reports for all multi-module projects.
     */
    private void executeAggregateReport( Locale locale )
        throws MavenReportException
        {
        for(Iterator iter = reactorProjects.iterator(); iter.hasNext(); ) {
            MavenProject proj = (MavenProject)iter.next();
            if(!isMultiModule(proj)) {
                continue;
            }
            executeAggregateReport(locale, proj);
        }
    }

    /**
     * Generates an aggregate cobertura report for the given project.
     */
    private void executeAggregateReport( Locale locale, MavenProject curProject )
        throws MavenReportException
    {
        List children = new ArrayList();
        getAllChildren(curProject, children);

        if(children.isEmpty()) {
            return;
        }

        List serFiles = getOutputFiles(children);
        if(serFiles.isEmpty()) {
            getLog().info("Not executing aggregate cobertura:report for " + curProject.getName() +
                          " as no child cobertura data files could not be found" );
            return;
        }

        getLog().info("Executing aggregate cobertura:report for " + curProject.getName());
        
        ProjectData aggProjectData = new ProjectData();
        for (Iterator iter = serFiles.iterator(); iter.hasNext(); ) {
            File serFile = (File)iter.next();
            ProjectData data = CoverageDataFileHandler.loadCoverageData(serFile);
            aggProjectData.merge(data);
        }

        File aggSerFile = new File(curProject.getBasedir(), relDataFileName);
        aggSerFile.getAbsoluteFile().getParentFile().mkdirs();
        getLog().info("Saving aggregate cobertura information in " + aggSerFile.getAbsolutePath());
        CoverageDataFileHandler.saveCoverageData(aggProjectData, aggSerFile);

        // get all compile source roots
        List aggCompileSourceRoots = new ArrayList();
        for(Iterator iter = children.iterator(); iter.hasNext(); ) {
            MavenProject child = (MavenProject)iter.next();
            aggCompileSourceRoots.addAll(child.getCompileSourceRoots());
        }
        
        File reportDir = new File(curProject.getBasedir(), relAggregateOutputDir);
        reportDir.mkdirs();
        executeReport(aggSerFile, reportDir, aggCompileSourceRoots);
    }

    /**
     * Executes the cobertura report task for the given dataFile, outputDirectory, and compileSourceRoots.
     */
    private void executeReport(File curDataFile, File curOutputDirectory, List curCompileSourceRoots)
        throws MavenReportException
    {
        ReportTask task = new ReportTask();

        // task defaults
        task.setLog( getLog() );
        task.setPluginClasspathList( pluginClasspathList );
        task.setQuiet( quiet );

        // task specifics
        task.setMaxmem( maxmem );
        task.setDataFile( curDataFile );
        task.setOutputDirectory( curOutputDirectory );
        task.setCompileSourceRoots( curCompileSourceRoots );
        task.setSourceEncoding( encoding );

        CommandLineArguments cmdLineArgs; 
        cmdLineArgs = new CommandLineArguments();
        cmdLineArgs.setUseCommandsFile(true);
        task.setCmdLineArgs(cmdLineArgs);
        
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
        return "index";
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
        if(canGenerateSimpleReport()) {
            return true;
        }
        if(canGenerateAggregateReports()) {
            return true;
        }

        if(aggregate && isMultiModule(project)) {
            // unfortunately, we don't know before hand whether we can generate an aggregate report for a
            // multi-module.  if we return false here, then we won't get a link in the main reports list.  so we'll
            // just be optimistic
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not we can generate a simple (non-aggregate) report for this project.
     */
    private boolean canGenerateSimpleReport()
    {
        /*
         * Don't have to check for source directories or java code or the like for report generation. Checks for source
         * directories or java project classpath existence should only occur in the Instrument Mojo.
         */
        if ( dataFile == null || !dataFile.exists() )
        {
            getLog().info("Not executing cobertura:report as the cobertura data file (" + dataFile
                               + ") could not be found" );
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns whether or not we can generate any aggregate reports at this time.
     */
    private boolean canGenerateAggregateReports()
    {
        // we only generate aggregate reports after the last project runs
        if(aggregate && isLastProject(project, reactorProjects)) {
            buildAggregateInfo();

            if(!getOutputFiles(reactorProjects).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the compileSourceRoots for the currently executing project.
     */
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

    /**
     * Check whether the element is the last element of the list
     *
     * @param project          element to check
     * @param mavenProjectList list of maven project
     * @return true if project is the last element of mavenProjectList  list
     */
    private boolean isLastProject(MavenProject project, List mavenProjectList)
    {
        return project.equals(mavenProjectList.get(mavenProjectList.size() - 1));
}

    /**
     * Test if the project has pom packaging
     *
     * @param mavenProject Project to test
     * @return True if it has a pom packaging
     */
    private boolean isMultiModule(MavenProject mavenProject)
    {
        return mavenProject.getPackaging().equals("pom");
    }

    /**
     * Generates various information needed for building aggregate reports.
     */
    private void buildAggregateInfo()
    {
        if(projectChildren != null) {
            // already did this work
            return;
        }

        // build parent-child map
        projectChildren = new HashMap();
        for(Iterator iter = reactorProjects.iterator(); iter.hasNext(); ) {
            MavenProject proj = (MavenProject)iter.next();
            List depList = (List)projectChildren.get(proj.getParent());
            if(depList == null) {
                depList = new ArrayList();
                projectChildren.put(proj.getParent(), depList);
            }
            depList.add(proj);
        }

        // attempt to determine where data files and output dir are
        relDataFileName = relativize(project.getBasedir(), dataFile);
        if(relDataFileName == null) {
            getLog().warn("Could not determine relative data file name, defaulting to 'cobertura/cobertura.ser'");
            relDataFileName = "cobertura/cobertura.ser";
        }
        relAggregateOutputDir = relativize(project.getBasedir(), outputDirectory);
        if(relAggregateOutputDir == null) {
            getLog().warn("Could not determine relative output dir name, defaulting to 'cobertura'");
            relAggregateOutputDir = "cobertura";
        }
    }

    /**
     * Returns all the recursive, non-pom children of the given project.
     */
    private void getAllChildren(MavenProject parentProject, List allChildren)
    {
        List children = (List)projectChildren.get(parentProject);
        if(children == null) {
            return;
        }

        for(Iterator iter = children.iterator(); iter.hasNext(); ) {
            MavenProject child = (MavenProject)iter.next();
            if(isMultiModule(child)) {
                getAllChildren(child, allChildren);
            } else {
                allChildren.add(child);
            }
        }
    }

    /**
     * Returns any existing cobertura data files from the given list of projects.
     */
    private List getOutputFiles(List projects)
    {
        List files = new ArrayList();
        for(Iterator iter = projects.iterator(); iter.hasNext(); ) {
            MavenProject proj = (MavenProject)iter.next();
            if(isMultiModule(proj)) {
                continue;
            }
            File outputFile = new File(proj.getBasedir(), relDataFileName);
            if(outputFile.exists()) {
                files.add(outputFile);
            }
        }
        return files;
    }

    /**
     * Attempts to make the given childFile relative to the given parentFile.
     */
    private String relativize(File parentFile, File childFile)
    {
        try {
            URI parentURI = parentFile.getCanonicalFile().toURI().normalize();
            URI childURI = childFile.getCanonicalFile().toURI().normalize();

            URI relativeURI = parentURI.relativize(childURI);
            if(relativeURI.isAbsolute()) {
                // child is not relative to parent
                return null;
            }
            String relativePath = relativeURI.getPath();
            if(File.separatorChar != '/') {
                relativePath = relativePath.replace('/', File.separatorChar);
            }
            return relativePath;
        } catch(Exception e) {
            getLog().warn("Failed relativizing " + childFile + " to " + parentFile, e);
        }
        return null;
    }
    
}
