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
package org.codehaus.mojo.cobertura;

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
     * 
     * @parameter expression="${cobertura.datafile}"
     *            default-value="${project.build.directory}/cobertura/cobertura.ser"
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
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    protected List pluginClasspathList;

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
}
