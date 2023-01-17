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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.cobertura.configuration.MaxHeapSizeUtil;
import org.codehaus.mojo.cobertura.configuration.MaxPermgenSizeUtil;
import org.codehaus.mojo.cobertura.tasks.AbstractTask;

import java.io.File;
import java.util.List;

/**
 * Abstract Base for Cobertura Mojos.
 *
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public abstract class AbstractCoberturaMojo
    extends AbstractMojo
{
    /**
     * <i>Maven Internal</i>: Project to interact with.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Maximum memory to pass to the JVM for Cobertura processes.
     *
     * @parameter expression="${cobertura.maxmem}" default-value="64m"
     */
    // Duplicate def to please MojoTestCase.
    // Use only default-value once tests have been refactored to IT's
    private String maxmem = "64m";

    private String maxpermgen = "";

    /**
     * <p>
     * The Datafile Location.
     * </p>
     *
     * @parameter expression="${cobertura.datafile}" default-value="${project.build.directory}/cobertura/cobertura.ser"
     * @required
     * @readonly
     */
    private File dataFile;

    /**
     * @parameter default-value="${mojoExecution}"
     * @required
     * @readonly
     */
    private MojoExecution mojoExecution;

    /**
     * Only output Cobertura errors, avoid info messages.
     *
     * @parameter expression="${quiet}" default-value="false"
     */
    private boolean quiet;

    /**
     * <i>Maven Internal</i>: List of artifacts for the plugin.
     *
     * @parameter default-value="${plugin.artifacts}"
     * @required
     * @readonly
     */
    protected List<Artifact> pluginClasspathList;

    /**
     * When <code>true</code>, skip the execution.
     *
     * @parameter expression="${cobertura.skip}"
     * default-value="false"
     * @since 2.5
     */
    private boolean skip;

    /**
     * Usually most of our Cobertura mojos will not get executed on parent poms.
     * Setting this parameter to <code>true</code> will force
     * the execution of this mojo, even if it would usually get skipped in this case.
     *
     * @parameter expression="${cobertura.force}"
     * default-value=false
     * @required
     * @since 2.5
     */
    private boolean forceMojoExecution;


    /**
     * Setup the Task defaults.
     *
     * @param task the task to setup.
     */
    protected void setTaskDefaults( AbstractTask task )
    {
        task.setLog( getLog() );
        task.setPluginClasspathList( pluginClasspathList );

        if ( MaxHeapSizeUtil.getInstance().envHasMavenMaxMemSetting() )
        {
            maxmem = MaxHeapSizeUtil.getInstance().getMavenMaxMemSetting();
        }
        else
        {
            task.setMaxmem( maxmem );
        }
        if ( MaxPermgenSizeUtil.getInstance().envHasMavenMaxPermgenSetting() )
        {
            maxpermgen = MaxPermgenSizeUtil.getInstance().getMavenMaxPermgenSetting();
        }
        else
        {
            task.setMaxPermgen( maxpermgen );
        }
        task.setQuiet( quiet );
    }

    /**
     * <p>Determine if the mojo execution should get skipped.</p>
     * This is the case if:
     * <ul>
     * <li>{@link #skip} is <code>true</code></li>
     * <li>if the mojo gets executed on a project with packaging type 'pom' and
     * {@link #forceMojoExecution} is <code>false</code></li>
     * </ul>
     *
     * @return <code>true</code> if the mojo execution should be skipped.
     */
    protected boolean skipMojo()
    {
        if ( skip )
        {
            getLog().info( "Skipping cobertura execution" );
            return true;
        }

        if ( !forceMojoExecution && "pom".equals( this.project.getPackaging() ) )
        {
            getLog().info( "Skipping cobertura mojo for project with packaging type 'pom'" );
            return true;
        }

        return false;
    }

    /**
     * Get the data file which is or will be generated by Cobertura, never <code>null</code>.
     *
     * @return the data file
     */
    protected File getDataFile()
    {
        return CoberturaMojoUtils.getDataFile( new File( this.project.getBuild().getDirectory() ), mojoExecution );
    }

    /**
     * @return the project
     */
    protected final MavenProject getProject()
    {
        return project;
    }

}
