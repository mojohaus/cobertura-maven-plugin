package org.codehaus.mojo.cobertura.tasks;

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

import java.util.Iterator;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.mojo.cobertura.configuration.ConfigCheck;
import org.codehaus.plexus.util.StringUtils;

/**
 * The Check Task.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public class CheckTask
    extends AbstractTask
{
    private ConfigCheck config;

    private String dataFile;

    public CheckTask()
    {
        super( "net.sourceforge.cobertura.check.Main" );
    }

    public void execute()
        throws MojoExecutionException
    {
        if ( dataFile != null )
        {
            cmdLineArgs.addArg( "--datafile", dataFile );
        }

        if ( StringUtils.isNotEmpty( config.getBranchRate() ) )
        {
            cmdLineArgs.addArg( "--branch", config.getBranchRate() );
        }

        if ( StringUtils.isNotEmpty( config.getLineRate() ) )
        {
            cmdLineArgs.addArg( "--line", config.getLineRate() );
        }

        if ( StringUtils.isNotEmpty( config.getTotalBranchRate() ) )
        {
            cmdLineArgs.addArg( "--totalbranch", config.getTotalBranchRate() );
        }

        if ( StringUtils.isNotEmpty( config.getTotalLineRate() ) )
        {
            cmdLineArgs.addArg( "--totalline", config.getTotalLineRate() );
        }

        if ( StringUtils.isNotEmpty( config.getPackageBranchRate() ) )
        {
            cmdLineArgs.addArg( "--packagebranch", config.getPackageBranchRate() );
        }

        if ( StringUtils.isNotEmpty( config.getPackageLineRate() ) )
        {
            cmdLineArgs.addArg( "--packageline", config.getPackageLineRate() );
        }

        Iterator iter = config.getRegexes().iterator();
        while ( iter.hasNext() )
        {
            cmdLineArgs.addArg( "--regex", iter.next().toString() );
        }

        int returnCode = executeJava();

        // Check the return code and print a message
        if ( returnCode == 0 )
        {
            getLog().info( "All checks passed." );
        }
        else
        {
            if ( config.isHaltOnFailure() )
            {
                throw new MojoExecutionException( "Coverage check failed. See messages above." );
            }
            else
            {
                getLog().error( "Coverage check failed. See messages above." );
            }
        }
    }

    public ConfigCheck getConfig()
    {
        return config;
    }

    /**
     * @return Returns the dataFile.
     */
    public String getDataFile()
    {
        return dataFile;
    }

    public void setConfig( ConfigCheck config )
    {
        this.config = config;
    }

    /**
     * @param dataFile The dataFile to set.
     */
    public void setDataFile( String dataFile )
    {
        this.dataFile = dataFile;
    }

}
