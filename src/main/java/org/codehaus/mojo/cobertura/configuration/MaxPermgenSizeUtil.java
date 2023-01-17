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
package org.codehaus.mojo.cobertura.configuration;


import java.util.Map;

/**
 * Class for checking and retrieving the maven environment variable for the
 * maximum heap size setting.
 *
 * @author Per Joran Lund
 * @since 2.8
 */
public class MaxPermgenSizeUtil
{

    /**
     * The maven options environment variable. Case sensitive.
     */
    public static final String MAVEN_OPTIONS = "MAVEN_OPTS";

    /**
     * The max permgen size JVM parameter. Case sensitive.
     */
    public static final String MAX_PERMGEN_FLAG = "-XX:MaxPermSize=";

    /**
     * The number of characters from the flag char start to the actual setting.
     */
    private static final int NUMBER_OF_PERMGEN_FLAG_CHARS = MAX_PERMGEN_FLAG.length();

    /**
     * The instance of the <code>MemSettingUtil</code> singleton.
     */
    private static MaxPermgenSizeUtil maxPermgenSizeUtil;

    /**
     * Singleton getter.
     *
     * @return the <code>MemSettingUtil</code> instance.
     */
    public static MaxPermgenSizeUtil getInstance()
    {
        if ( maxPermgenSizeUtil == null )
        {
            maxPermgenSizeUtil = new MaxPermgenSizeUtil();
        }
        return maxPermgenSizeUtil;
    }

    /**
     * Private constructor for singleton purposes.
     */
    private MaxPermgenSizeUtil()
    {
        // do nothing
    }

    /**
     * The environment settings.
     */
    private Map<String, String> envSettings;

    /**
     * Gets envSettings map, typically a reference to the <code>System.getEnv()</code> settings.
     * A lazy loaded property is used to make unit tests easy.
     *
     * @return the <code>System.getEnv()</code> settings.
     */
    private Map<String, String> getEnvSettings()
    {
        if ( envSettings == null )
        {
            envSettings = System.getenv();
        }
        return envSettings;
    }

    /**
     * Sets the <code>System.getEnv()</code> settings.
     *
     * @param envSettings the settings map to use
     */
    public void setEnvSettings( Map<String, String> envSettings )
    {
        this.envSettings = envSettings;
    }

    /**
     * Gets the maximum PermGen size JVM argument from the maven options environment variable.
     * Returns only the numeric and unit portion.  For example, given a maven options setting
     * of "-XX:MaxPermSize=64m" this method will return "64m". Returns <code>null</code> if the maven
     * environment variable isn't set, the JVM PermGen size argument is not present, or the JVM
     * PermGen size argument is  somehow invalid.
     *
     * @return the maximum PermGen size JVM argument from the maven options environment variable
     * @see #envHasMavenMaxMemSetting()
     */
    public String getMavenMaxPermgenSetting()
    {
        boolean hasMavenOptions = getEnvSettings().containsKey( MAVEN_OPTIONS );
        if ( !hasMavenOptions )
        {
            return null;
        }

        String mavenOpts = (String) getEnvSettings().get( MAVEN_OPTIONS );
        boolean hasMaxPermgenSetting = mavenOpts.contains( MAX_PERMGEN_FLAG );

        if ( !hasMaxPermgenSetting )
        {
            return null;
        }

        String mavenOptionsEnvironmentSetting = (String) getEnvSettings().get( MAVEN_OPTIONS );
        int startIndex = mavenOptionsEnvironmentSetting.indexOf( MAX_PERMGEN_FLAG ) + NUMBER_OF_PERMGEN_FLAG_CHARS;
        int endIndex = mavenOptionsEnvironmentSetting.indexOf( ' ', startIndex );

        if ( endIndex == -1 )
        {
            endIndex = mavenOptionsEnvironmentSetting.length();
        }

        String maxPermgenSetting = mavenOptionsEnvironmentSetting.substring( startIndex, endIndex ).trim();
        if ( !maxPermgenSetting.matches( "\\d+[mgMG]" ) )
        {
            return null;
        }
        return maxPermgenSetting;
    }

    /**
     * Returns <code>true</code> if the current environment has the max heap size set in the maven
     * options, otherwise <code>false</code>.
     *
     * @return <code>true</code> if the current environment has the max heap size set in the maven
     * options, otherwise <code>false</code>.
     * @see #getMavenMaxPermgenSetting()
     */
    public boolean envHasMavenMaxPermgenSetting()
    {
        if ( getMavenMaxPermgenSetting() != null )
        {
            return true;
        }
        return false;
    }
}
