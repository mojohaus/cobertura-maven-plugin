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

import java.util.HashSet;
import java.util.Set;

/**
 * The Configuration for Check Settings.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public class ConfigCheck
{
    private String branchRate;

    private boolean haltOnFailure = true;

    private String lineRate;

    private Set<Regex> regexes = new HashSet<Regex>();

    private String totalBranchRate;

    private String totalLineRate;

    private String packageBranchRate;

    private String packageLineRate;

    /**
     * The maximum heap size to use for the check phase.
     */
    private String maxmem;

    /**
     * add a regex to the set used to scan the reports.
     * @param regex the regex to add.
     */
    public void addRegex( Regex regex )
    {
        this.regexes.add( regex );
    }

    /**
     * Create a regex object for eventual inclusion.
     * @return the regex
     */
    public Regex createRegex()
    {
        Regex regex = new Regex();
        this.regexes.add( regex );
        return regex;
    }

    /**
     * @return Returns the branchRate.
     */
    public String getBranchRate()
    {
        return branchRate;
    }

    /**
     * @return Returns the lineRate.
     */
    public String getLineRate()
    {
        return lineRate;
    }

    /**
     * @return Returns the regexes.
     */
    public Set<Regex> getRegexes()
    {
        return regexes;
    }

    /**
     * @return Returns the totalBranchRate.
     */
    public String getTotalBranchRate()
    {
        return totalBranchRate;
    }

    /**
     * @return Returns the totalLineRate.
     */
    public String getTotalLineRate()
    {
        return totalLineRate;
    }

    /**
     * @return Returns the haltOnFailure.
     */
    public boolean isHaltOnFailure()
    {
        return haltOnFailure;
    }

    /**
     * @param branchRate The branchRate to set.
     */
    public void setBranchRate( String branchRate )
    {
        this.branchRate = branchRate;
    }

    /**
     * @param haltOnFailure The haltOnFailure to set.
     */
    public void setHaltOnFailure( boolean haltOnFailure )
    {
        this.haltOnFailure = haltOnFailure;
    }

    /**
     * @param lineRate The lineRate to set.
     */
    public void setLineRate( String lineRate )
    {
        this.lineRate = lineRate;
    }

    /**
     * @param regexes The regexes to set.
     */
    public void setRegexes( Set<Regex> regexes )
    {
        this.regexes = new HashSet<Regex>( regexes );
    }

    /**
     * @param totalBranchRate The totalBranchRate to set.
     */
    public void setTotalBranchRate( String totalBranchRate )
    {
        this.totalBranchRate = totalBranchRate;
    }

    /**
     * @param totalLineRate The totalLineRate to set.
     */
    public void setTotalLineRate( String totalLineRate )
    {
        this.totalLineRate = totalLineRate;
    }

    /**
     * @return the package branch rate.
     */
    public String getPackageBranchRate()
    {
        return packageBranchRate;
    }

    /**
     * @return the package line rate.
     */
    public String getPackageLineRate()
    {
        return packageLineRate;
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

    /**
     * Sets the max memory for the JVM used to run the <code>CheckTask</code> task.
     * 
     * The format is "<value><size>". Ex: "64m" where 64 is the value, and "m" denotes megabytes.
     * @param maxmem the value to which maxmem will be set
     */
    public void setMaxmem( String maxmem )
    {
        this.maxmem = maxmem;
    }
}