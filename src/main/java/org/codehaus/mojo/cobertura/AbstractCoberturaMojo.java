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

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.cobertura.configuration.ConfigCheck;
import org.codehaus.mojo.cobertura.configuration.ConfigInstrumentation;
import org.codehaus.mojo.cobertura.tasks.AbstractTask;

/**
 * Abstract Base for Cobertura Mojos
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @configurator override
 */
public abstract class AbstractCoberturaMojo
    extends AbstractMojo
{
    protected static final String ORIGINAL_CLASS_DIRECTORY = "cobertura.original.build.outputDirectory";

    /**
     * @parameter expression="${project}"
     * @required
     */
    protected MavenProject project;
    
    /**
     * @parameter expression="${cobertura.maxmem}"
     * 
     */
    private String maxmem = "64m";

    /**
     * @parameter expression="${basedir}/cobertura.ser"
     * @required
     */
    protected File dataFile;

    /**
     * @parameter expression="${check}"
     */
    protected ConfigCheck check;

    /**
     * @parameter expression="${instrumentation}"
     */
    protected ConfigInstrumentation instrumentation;

    /**
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     */
    protected List compileClasspathList;

    /**
     * @parameter expression="${plugin.artifacts}"
     * @required
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
        task.setCompileClasspathList( compileClasspathList );
        task.setPluginClasspathList( pluginClasspathList );

        task.setMaxmem( maxmem );
    }
}
