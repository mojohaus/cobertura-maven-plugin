package org.codehaus.mojo.cobertura.configuration;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Configuration for the Instrumentation.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public class ConfigInstrumentation
{
    private File basedir;

    private List excludes;

    private List ignores;

    private List includes;

    /**
     * Construct a new ConfigInstrumentation object.
     */
    public ConfigInstrumentation()
    {
        this.includes = new ArrayList();
        this.excludes = new ArrayList();
        this.ignores = new ArrayList();
        this.basedir = new File( System.getProperty( "user.dir" ) );
    }

    /**
     * Add an Exclude to the underlying list.
     * 
     * @param exclude the exlude string.
     */
    public void addExclude( String exclude )
    {
        this.excludes.add( exclude );
    }

    /**
     * Add an Ignore to the underlying list.
     * 
     * @param ignore the ignore string.
     */
    public void addIgnore( String ignore )
    {
        this.ignores.add( ignore );
    }

    /**
     * Add an Include ot the underlying list.
     * 
     * @param include the include string.
     */
    public void addInclude( String include )
    {
        this.includes.add( include );
    }

    /**
     * @return Returns the basedir.
     */
    public File getBasedir()
    {
        return basedir;
    }

    /**
     * Get the Exclude List.
     * 
     * @return the exlude list.
     */
    public List getExcludes()
    {
        return excludes;
    }

    /**
     * Get the Ignore List.
     * 
     * @return the ignore list.
     */
    public List getIgnores()
    {
        return ignores;
    }

    /**
     * Get the Include List.
     * 
     * @return the include list.
     */
    public List getIncludes()
    {
        return includes;
    }

    /**
     * @param basedir The basedir to set.
     */
    public void setBasedir( File basedir )
    {
        this.basedir = basedir;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "<ConfigInstrumentation" );

        sb.append( " basedir=\"" );
        if ( this.basedir != null )
        {
            sb.append( basedir.getAbsolutePath() );
        }
        sb.append( "\"" );

        if ( !this.includes.isEmpty() )
        {
            sb.append( " includes=\"" );
            Iterator it = this.includes.iterator();
            while ( it.hasNext() )
            {
                String include = (String) it.next();
                sb.append( include );

                if ( it.hasNext() )
                {
                    sb.append( " " );
                }
            }
            sb.append( "\"" );
        }

        if ( !this.excludes.isEmpty() )
        {
            sb.append( " excludes=\"" );
            Iterator it = this.excludes.iterator();
            while ( it.hasNext() )
            {
                String exclude = (String) it.next();
                sb.append( exclude );
                if ( it.hasNext() )
                {
                    sb.append( " " );
                }
            }
            sb.append( "\"" );
        }

        if ( !this.ignores.isEmpty() )
        {
            sb.append( " ignores=\"" );
            Iterator it = this.ignores.iterator();
            while ( it.hasNext() )
            {
                String ignore = (String) it.next();
                sb.append( ignore );
                if ( it.hasNext() )
                {
                    sb.append( " " );
                }
            }
            sb.append( "\"" );
        }

        return sb.append( " />" ).toString();
    }
}
