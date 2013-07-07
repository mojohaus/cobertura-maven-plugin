package org.codehaus.mojo.cobertura.configuration;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;

/**
 * Test class for {@link MaxHeapSizeUtil}.
 * 
 * @author Trampas Kirk
 */
public class MaxHeapSizeUtilTest
    extends TestCase
{
    /**
     * Test method for {@link MaxHeapSizeUtil#envHasMavenMaxMemSetting()}.
     * The scenario is we set the environment map to one that lacks a maven options setting.
     * The expected result is that it returns <code>false</code>.
     */
    public void testHasMaxMemSetting_noMavenOpts()
    {
        final Map noSettingsEnvMap = new HashMap();
        
        MaxHeapSizeUtil.getInstance().setEnvSettings( noSettingsEnvMap );
        
        boolean setting = MaxHeapSizeUtil.getInstance().envHasMavenMaxMemSetting();
        
        assertFalse( "No maven options, should be false.", setting );
    }
    
    /**
     * Test method for {@link MaxHeapSizeUtil#envHasMavenMaxMemSetting()}.
     * The scenario is we set the environment map to one that has a maven setting
     * but no max heap size setting.
     * The expected result is that it returns <code>false</code>.
     */
    public void testHasMaxMemSetting_noMaxMem()
    {
        final Map noMaxMemMap = new HashMap();
        
        noMaxMemMap.put( MaxHeapSizeUtil.MAVEN_OPTIONS, "-fakeArg" );
        
        MaxHeapSizeUtil.getInstance().setEnvSettings( noMaxMemMap );
        
        boolean setting = MaxHeapSizeUtil.getInstance().envHasMavenMaxMemSetting();
        
        assertFalse( "No max memory set in maven options, should be false.", setting );
    }
    
    /**
     * Test method for {@link MaxHeapSizeUtil#envHasMavenMaxMemSetting()}.
     * The scenario is we set the environment map to one that has a maven setting
     * but it is an invalid max heap size setting.
     * The expected result is that it returns <code>false</code> without error.
     */
    public void testHasMaxMemSetting_garbage()
    {
        final Map noMaxMemMap = new HashMap();
        
        noMaxMemMap.put( MaxHeapSizeUtil.MAVEN_OPTIONS, "-XmxFOO" );
        
        MaxHeapSizeUtil.getInstance().setEnvSettings( noMaxMemMap );
        
        boolean setting = MaxHeapSizeUtil.getInstance().envHasMavenMaxMemSetting();
        
        assertFalse( "Max memory was invalid, should be false.", setting );
    }
    
    /**
     * Test method for {@link MaxHeapSizeUtil#envHasMavenMaxMemSetting()}.
     * The scenario is we set the environment map to one that has a maven setting
     * but a heap size setting that lacks the required unit designation.
     * The expected result is that it returns <code>false</code>.
     */
    public void testHasMaxMemSetting_noUnits()
    {
        final Map noMaxMemMap = new HashMap();
        
        noMaxMemMap.put( MaxHeapSizeUtil.MAVEN_OPTIONS, "-Xmx42" );
        
        MaxHeapSizeUtil.getInstance().setEnvSettings( noMaxMemMap );
        
        boolean setting = MaxHeapSizeUtil.getInstance().envHasMavenMaxMemSetting();
        
        assertFalse( "Max memory setting was invalid, should be false.", setting );
    }
    
    /**
     * Test method for {@link MaxHeapSizeUtil#envHasMavenMaxMemSetting()}.
     * The scenario is we set 128m in the evironment map.
     * The expected result is that it returns <code>true</code>.
     */
    public void testHasMaxMemSetting_128m()
    {
        final String expectedMemSetting = "128m";
        
        Map fakeEnv = new HashMap();
        
        fakeEnv.put( MaxHeapSizeUtil.MAVEN_OPTIONS, MaxHeapSizeUtil.MAX_MEMORY_FLAG + expectedMemSetting );

        MaxHeapSizeUtil.getInstance().setEnvSettings( fakeEnv );

        boolean hasSetting = MaxHeapSizeUtil.getInstance().envHasMavenMaxMemSetting();
        
        assertTrue( "Max memory set to a valid value, should be true.", hasSetting );
    }
    
    /**
     * Test method for {@link MaxHeapSizeUtil#getMavenMaxMemSetting()}.
     * The scenario is we set 128m in the evironment map.
     * The expected result is that 128m is returned.
     */
    public void testGetMaxMemSetting_128m()
    {
        final String expectedMemSetting = "128m";
        
        Map fakeEnv = new HashMap();
        
        fakeEnv.put( MaxHeapSizeUtil.MAVEN_OPTIONS, MaxHeapSizeUtil.MAX_MEMORY_FLAG + expectedMemSetting );

        MaxHeapSizeUtil.getInstance().setEnvSettings( fakeEnv );

        String actualSetting = MaxHeapSizeUtil.getInstance().getMavenMaxMemSetting();
        
        assertEquals( expectedMemSetting, actualSetting );
    }

    /**
     * Test method for {@link MaxHeapSizeUtil#getMavenMaxMemSetting()}.
     * The scenario is we set the environment map to one that lacks a maven setting.
     * The expected result is that the method returns <code>null</code> without error.
     */
    public void testGetMaxMemSetting_none()
    {
        final Map noSettingsEnvMap = new HashMap();
        
        MaxHeapSizeUtil.getInstance().setEnvSettings( noSettingsEnvMap );
        
        String actualSetting = MaxHeapSizeUtil.getInstance().getMavenMaxMemSetting();
        
        assertNull( "No max memory set, should be null.", actualSetting );
    }
}
