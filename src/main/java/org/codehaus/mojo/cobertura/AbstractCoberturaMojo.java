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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.cobertura.configuration.ConfigCheck;
import org.codehaus.mojo.cobertura.configuration.ConfigInstrumentation;
import org.codehaus.mojo.cobertura.tasks.AbstractTask;

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
    protected MavenProject project;

    /**
     * Maximum memory to pass JVM as -Xmx of Cobertura processes. 
     * 
     * @parameter expression="${cobertura.maxmem}" default-value="64m"
     */
    // Duplicate def to please MojoTestCase.
    // Use only default-value once tests have been refactored to IT's
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
     * The <a href="usage.html#Check">Check Configuration</a>.
     * 
     * @parameter expression="${check}"
     */
    protected ConfigCheck check;

    /**
     * The <a href="usage.html#Instrumentation">Instrumentation Configuration</a>.
     * 
     * @parameter expression="${instrumentation}"
     */
    protected ConfigInstrumentation instrumentation;

    /**
     * Only output coberura errors, avoid info messages.
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
    protected List pluginClasspathList;

    /**
     * When <code>true</code>, skip the execution.
     * @since 2.5
     * @parameter expression="${cobertura.skip}"
     *            default-value="false"
     */
    private boolean skip;

    /**
     * Usually most of out cobertura mojos will not get executed on parent poms.
     * Setting this parameter to <code>true</code> will force
     * the execution of this mojo, even if it would usually get skipped in this case.
     *
     * @since 2.5
     * @parameter expression="${cobertura.force}"
     *            default-value=false
     * @required
     */
    private boolean forceMojoExecution;


    /**
     * Setup the Task defaults.
     * 
     * @param task the task to setup.
     */
    public void setTaskDefaults( AbstractTask task )
    {
        task.setLog( getLog() );
        task.setPluginClasspathList( pluginClasspathList );
        task.setMaxmem( maxmem );
        task.setQuiet( quiet );
    }

    /**
     * <p>Determine if the mojo execution should get skipped.</p>
     * This is the case if:
     * <ul>
     *   <li>{@link #skip} is <code>true</code></li>
     *   <li>if the mojo gets executed on a project with packaging type 'pom' and
     *       {@link #forceMojoExecution} is <code>false</code></li>
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

        if ( !forceMojoExecution && project != null && "pom".equals( project.getPackaging() ) )
        {
            getLog().info( "Skipping cobertura mojo for project with packaging type 'pom'" );
            return true;
        }

        return false;
    }

}
