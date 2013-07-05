package org.codehaus.mojo.cobertura.configuration;

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

    private List<String> excludes;

    private List<String> ignores;

    private List<String> includes;
    
    private String maxmem;

	private boolean ignoreTrivial;

    private List<String> ignoreMethodAnnotations;	
    /**
     * Construct a new ConfigInstrumentation object.
     */
    public ConfigInstrumentation()
    {
        this.includes = new ArrayList<String>();
        this.excludes = new ArrayList<String>();
        this.ignores = new ArrayList<String>();
        this.ignoreTrivial = false;
        this.ignoreMethodAnnotations = new ArrayList<String>();
        
        this.basedir = new File( System.getProperty( "user.dir" ) );
        
        if ( MaxHeapSizeUtil.getInstance().envHasMavenMaxMemSetting() )
        {
            maxmem = MaxHeapSizeUtil.getInstance().getMavenMaxMemSetting();
        }
        else
        {
            this.maxmem = "64m";
        }
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
     * Add an IgnoreMethodAnnotation to the underlying list.
     * 
     * @param ignoreMethodAnnotation the ignore string.
     */
    public void addIgnoreMethodAnnotation( String ignoreMethodAnnotation )
    {
        this.ignoreMethodAnnotations.add( ignoreMethodAnnotation );
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
    public List<String> getExcludes()
    {
        return excludes;
    }

    /**
     * Get the Ignore List.
     * 
     * @return the ignore list.
     */
    public List<String> getIgnores()
    {
        return ignores;
    }

    /**
     * Get the Include List.
     * 
     * @return the include list.
     */
    public List<String> getIncludes()
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

    /**
     * Get the maxmem setting.
     * 
     * @return the maxmem setting.
     */
    public String getMaxmem() 
    {
        return maxmem;
    }

    /** Sets the max memory for the JVM used to run the <code>InstrumentationTask</code> task.
     * The format is "<value><size>". Ex: "64m" where 64 is the value, and "m" denotes megabytes.
     * @param maxmem the value to which maxmem will be set
     */
    public void setMaxmem( String maxmem )
    {
        this.maxmem = maxmem;
    }

    /**
     * Get the ignoreTrivial setting.
     * 
     * @return the ignoreTrivial setting.
     */
	public boolean getIgnoreTrivial() 
	{
		return ignoreTrivial;
	}
	
	/**
	 * IgnoreTrivial switch that tells Cobertura to ignore the following in the coverage report: 
	 * Getter methods that simply read a class field; Setter methods that set a class field; 
	 * Constructors that only set class fields and call a super class constructor.
	 * 
	 * @param ignoreTrivial enable ignoreTrivial
	 */
	public void setIgnoreTrivial(boolean ignoreTrivial) {
		this.ignoreTrivial = ignoreTrivial;
	}
	
    /**
     * Get the ignoreMethodAnnotations setting.
     * 
     * @return the ignoreMethodAnnotations setting.
     */
	public List<String> getIgnoreMethodAnnotations() {
		return ignoreMethodAnnotations;
	}

	/**
	 * IgnoreMethodAnnotation switch used to specify an annotation that, when present on a method, 
	 * will cause Cobertura to ignore the method in the coverage report.
	 * 
	 * @param ignoreMethodAnnotations 
	 */
	public void setIgnoreMethodAnnotations(List<String> ignoreMethodAnnotations) {
		this.ignoreMethodAnnotations = ignoreMethodAnnotations;
	}

	

	/**
     * {@inheritDoc}
     */
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
            Iterator<String> it = this.includes.iterator();
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
            Iterator<String> it = this.excludes.iterator();
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
            Iterator<String> it = this.ignores.iterator();
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
        
        if ( 0 != getMaxmem().length() ) 
        {
            sb.append( " maxmem=\"" );
            sb.append( getMaxmem() );
            sb.append( "\"" );
        }
        
        sb.append("ignoreTrivial=\"");
        sb.append(getIgnoreTrivial());
        sb.append( "\"" );

        if ( !this.ignoreMethodAnnotations.isEmpty() ) 
        {
            sb.append( " ignoreMethodAnnotations=\"" );
            Iterator<String> it = this.ignoreMethodAnnotations.iterator();
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
