package org.codehaus.mojo.cobertura;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.mojo.cobertura.configuration.ConfigInstrumentation;
import org.codehaus.mojo.cobertura.tasks.InstrumentTask;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * Instrument the compiled classes.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @goal instrument
 * @requiresDependencyResolution compile
 */
public class CoberturaInstrumentMojo
    extends AbstractCoberturaMojo
{
    /**
     * Artifact factory.
     * 
     * @component
     */
    private ArtifactFactory factory;

    /**
     * Specifies whether or not to attach the cobertura artifact to the project
     *
     * @parameter expression="${cobertura.attach}" default-value="false"
     */
    private boolean attach;

    /**
     * Specifies the classifier to use for the attached ser artifact
     *
     * @parameter expression="${cobertura.classifier}" default-value="cobertura"
     */
    private String classifier;

    /**
     * Used for attaching the source jar to the project.
     *
     * @component
     */
    private MavenProjectHelper projectHelper;

    /**
     * The <a href="usage.html#Instrumentation">Instrumentation Configuration</a>.
     * 
     * @parameter expression="${instrumentation}"
     */
    private ConfigInstrumentation instrumentation;  

    /**
     * build up a command line from the parameters and run Cobertura to instrument the code.
     * @throws MojoExecutionException
     */
    public void execute()
        throws MojoExecutionException
    {
        if ( skipMojo() )
        {
            return;
        }
        
        ArtifactHandler artifactHandler = getProject().getArtifact().getArtifactHandler();
        if ( !"java".equals( artifactHandler.getLanguage() ) )
        {
            getLog().info(
                "Not executing cobertura:instrument as the project is not a Java classpath-capable package" );
        }
        else
        {
            File instrumentedDirectory =
                new File( getProject().getBuild().getDirectory(), "generated-classes/cobertura" );

            if ( !instrumentedDirectory.exists() )
            {
                instrumentedDirectory.mkdirs();
            }

            // ensure that instrumentation config is set here, not via maven plugin api @required attribute, as this is
            // not a required object from the pom configuration's point of view.
            if ( instrumentation == null )
            {
                instrumentation = new ConfigInstrumentation();
            }

            /* ensure that the default includes is set */
            if ( instrumentation.getIncludes().isEmpty() )
            {
                instrumentation.addInclude( "**/*.class" );
            }

            File outputDirectory = new File( getProject().getBuild().getOutputDirectory() );
            if ( !outputDirectory.exists() )
            {
                outputDirectory.mkdirs();
            }

            // Copy all of the classes into the instrumentation basedir.
            try
            {
                FileUtils.copyDirectoryStructure( outputDirectory, instrumentedDirectory );
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Unable to prepare instrumentation directory.", e );
            }

            instrumentation.setBasedir( instrumentedDirectory );

            // Cobertura requires an existing dir
            if ( !getDataFile().getParentFile().exists() )
            {
                getDataFile().getParentFile().mkdirs();
            }

            // Execute the instrumentation task.
            InstrumentTask task = new InstrumentTask();
            setTaskDefaults( task );
            List<Artifact> classpath = new ArrayList<Artifact>();
            /* need project class path */
            classpath.addAll(pluginClasspathList);
            classpath.addAll(getProject().getArtifacts());
            task.setPluginClasspathList(classpath);
            task.setConfig( instrumentation );
            task.setDestinationDir( instrumentedDirectory );
            task.setDataFile( getDataFile() );

            task.execute();

            addCoberturaDependenciesToTestClasspath();

            // Old, Broken way
            System.setProperty( "net.sourceforge.cobertura.datafile", getDataFile().getPath() );

            /*
             * New, Fixed way. See
             * https://sourceforge.net/tracker/index.php?func=detail&aid=1543280&group_id=130558&atid=720017 for patch
             * to Cobertura 1.8 that fixes the datafile location.
             */
            Properties props = new Properties();
            props.setProperty( "net.sourceforge.cobertura.datafile", getDataFile().getPath() );

            File coberturaPropertiesFile = new File( instrumentedDirectory, "cobertura.properties" );
            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream( coberturaPropertiesFile );
                props.store( fos, "Generated by maven-cobertura-plugin for project " + getProject().getId() );
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Unable to write cobertura.properties file.", e );
            }
            finally
            {
                IOUtil.close( fos );
            }

            // Set the instrumented classes to be the new output directory (for other plugins to pick up)
            getProject().getBuild().setOutputDirectory( instrumentedDirectory.getPath() );
            System.setProperty( "project.build.outputDirectory", instrumentedDirectory.getPath() );
            attachCoberturaArtifactIfAppropriate();
        }
    }
    
    private void attachCoberturaArtifactIfAppropriate()
    {
        if ( attach )
        {
            if ( getDataFile().exists() )
            {
                projectHelper.attachArtifact( getProject(), "ser", classifier, getDataFile() );
            }
            else
            {
                getLog().info( "No cobertura ser file exists to include in the attached artifacts list." );
            }
        }
        else
        {
            getLog().info( "NOT adding cobertura ser file to attached artifacts list." );
        }
    }

    /**
     * We need to tweak our test classpath for cobertura.
     * @throws MojoExecutionException
     */
    private void addCoberturaDependenciesToTestClasspath()
        throws MojoExecutionException
    {
        Map<String, Artifact> pluginArtifactMap = ArtifactUtils.artifactMapByVersionlessId( pluginClasspathList );
        Artifact coberturaArtifact = pluginArtifactMap.get( "net.sourceforge.cobertura:cobertura-runtime" );

        if ( coberturaArtifact == null )
        {
            getLog().error( "pluginArtifactMap: " + pluginArtifactMap );

            throw new MojoExecutionException( "Couldn't find 'cobertura' artifact in plugin dependencies" );
        }

        coberturaArtifact = artifactScopeToProvided( coberturaArtifact );

        if ( this.getProject().getDependencyArtifacts() != null )
        {
            Set<Artifact> set = new LinkedHashSet<Artifact>( this.getProject().getDependencyArtifacts() );
            set.add( coberturaArtifact );
            this.getProject().setDependencyArtifacts( set );
        }
    }

    /**
     * Use provided instead of just test, so it's available on both compile and test classpath (MCOBERTURA-26) 
     * 
     * @param artifact
     * @return re-scoped artifact
     */
    private Artifact artifactScopeToProvided( Artifact artifact )
    {
        return factory.createArtifact( artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                                       Artifact.SCOPE_PROVIDED, artifact.getType() );
    }
}