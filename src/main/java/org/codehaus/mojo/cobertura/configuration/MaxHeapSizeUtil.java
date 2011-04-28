package org.codehaus.mojo.cobertura.configuration;


import java.util.Map;

/**
 * Class for checking and retrieving the maven environment variable for the
 * maximum heap size setting.
 * @author Trampas Kirk
 * @since 2.3
 */
public class MaxHeapSizeUtil
{

    /**
     * The maven options environment variable. Case sensitive.
     */
    public static final String MAVEN_OPTIONS = "MAVEN_OPTS";

    /**
     * The max heap size JVM parameter. Case sensitive.
     */
    public static final String MAX_MEMORY_FLAG = "-Xmx";

    /**
     * The number of characters from the flag char start to the actual setting.
     */
    private static final int NUMBER_OF_FLAG_CHARS = MAX_MEMORY_FLAG.length();

    /**
     * The instance of the <code>MemSettingUtil</code> singleton.
     */
    private static MaxHeapSizeUtil maxHeapSizeUtil;

    /**
     * Singleton getter.
     * @return the <code>MemSettingUtil</code> instance.
     */
    public static MaxHeapSizeUtil getInstance()
    {
        if ( maxHeapSizeUtil == null )
        {
            maxHeapSizeUtil = new MaxHeapSizeUtil();
        }
        return maxHeapSizeUtil;
    }

    /**
     * Private constructor for singleton purposes.
     */
    private MaxHeapSizeUtil()
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
     * Gets the maximum heap size JVM argument from the maven options environment variable.
     * Returns only the numeric and unit portion.  For example, given a maven options setting
     * of "-Xmx64m" this method will return "64m". Returns <code>null</code> if the maven
     * environment variable isn't set, the JVM heap size argument is not present, or the JVM 
     * heap size argument is  somehow invalid.
     * 
     * @return the maximum heap size JVM argument from the maven options environment variable
     * @see #envHasMavenMaxMemSetting()
     */
    public String getMavenMaxMemSetting()
    {
        boolean hasMavenOptions = getEnvSettings().containsKey( MAVEN_OPTIONS );
        if ( !hasMavenOptions )
        {
            return null;
        }
        
        String mavenOpts = ( String ) getEnvSettings().get( MAVEN_OPTIONS );
        boolean hasMaxMemSetting = mavenOpts.contains( MAX_MEMORY_FLAG );
        
        if ( !hasMaxMemSetting )
        {
            return null;
        }
        
        String mavenOptionsEnvironmentSetting = ( String ) getEnvSettings().get( MAVEN_OPTIONS );
        int startIndex = mavenOptionsEnvironmentSetting.indexOf( MAX_MEMORY_FLAG ) + NUMBER_OF_FLAG_CHARS;
        int endIndex = mavenOptionsEnvironmentSetting.indexOf( ' ', startIndex );
        
        if ( endIndex == -1 )
        {
            endIndex = mavenOptionsEnvironmentSetting.length();
        }
        
        String maxMemSetting = mavenOptionsEnvironmentSetting.substring( startIndex, endIndex ).trim();
        
        if ( !maxMemSetting.matches( "\\d+[mgMG]" ) )
        {
            return null;
        }
        return maxMemSetting;
    }

    /**
     * Returns <code>true</code> if the current environment has the max heap size set in the maven
     * options, otherwise <code>false</code>.
     * 
     * @return <code>true</code> if the current environment has the max heap size set in the maven
     *         options, otherwise <code>false</code>.
     * @see #getMavenMaxMemSetting() 
     */
    public boolean envHasMavenMaxMemSetting()
    {
        if ( getMavenMaxMemSetting() != null )
        {
            return true;
        }
        return false;
    }
}
