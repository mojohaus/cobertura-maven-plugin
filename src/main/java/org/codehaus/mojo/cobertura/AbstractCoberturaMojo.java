package org.codehaus.mojo.cobertura;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.cobertura.configuration.ConfigCheck;
import org.codehaus.mojo.cobertura.configuration.ConfigInstrumentation;
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
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * Maximum memory to pass JVM of Cobertura processes.
     *
     * @parameter expression="${cobertura.maxmem}"
     */
    private String maxmem = "64m";

    /**
     * <p>The Datafile Location.</p>
     * <p/>
     * <p/>
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
     * <i>Maven Internal</i>: List of artifacts for the plugin.
     *
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    protected List pluginClasspathList;

    public abstract void execute()
        throws MojoExecutionException, MojoFailureException;

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
    }
}
