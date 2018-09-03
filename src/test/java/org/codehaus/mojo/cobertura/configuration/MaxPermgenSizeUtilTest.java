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

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link MaxPermgenSizeUtil}.
 *
 * @author Trampas Kirk
 */
public class MaxPermgenSizeUtilTest
    extends TestCase
{
    /**
     * Test method for {@link MaxPermgenSizeUtil#envHasMavenMaxPermgenSetting()}.
     * The scenario is we set the environment map to one that lacks a maven options setting.
     * The expected result is that it returns <code>true</code>.
     */
    public void testHasMaxPermgenSetting_noMavenOpts()
    {
        Map<String, String> noSettingsEnvMap = new HashMap<String, String>();

        MaxPermgenSizeUtil.getInstance().setEnvSettings( noSettingsEnvMap );

        boolean setting = MaxPermgenSizeUtil.getInstance().envHasMavenMaxPermgenSetting();

        assertFalse( "No maven options, should be false.", setting );
    }

    /**
     * Test method for {@link MaxPermgenSizeUtil#envHasMavenMaxPermgenSetting()}.
     * The scenario is we set the environment map to one that has a maven setting
     * but no PermGen size setting.
     * The expected result is that it returns <code>true</code>.
     */
    public void testHasMaxPermgenSetting_noMaxPermgen()
    {
        Map<String, String> noMaxPermgenMap = new HashMap<String, String>();

        noMaxPermgenMap.put( MaxPermgenSizeUtil.MAVEN_OPTIONS, "-fakeArg" );

        MaxPermgenSizeUtil.getInstance().setEnvSettings( noMaxPermgenMap );

        boolean setting = MaxPermgenSizeUtil.getInstance().envHasMavenMaxPermgenSetting();

        assertFalse( "No max PermGen set in maven options, should be false.", setting );
    }

    /**
     * Test method for {@link MaxPermgenSizeUtil#envHasMavenMaxPermgenSetting()}.
     * The scenario is we set the environment map to one that has a maven setting
     * but it is an invalid max PermGen size setting.
     * The expected result is that it returns <code>false</code> without error.
     */
    public void testHasMaxPermgenSetting_garbage()
    {
        Map<String, String> noMaxPermgenMap = new HashMap<String, String>();

        noMaxPermgenMap.put( MaxPermgenSizeUtil.MAVEN_OPTIONS, "-XX:MaxPermSize=FOO" );

        MaxPermgenSizeUtil.getInstance().setEnvSettings( noMaxPermgenMap );

        boolean setting = MaxPermgenSizeUtil.getInstance().envHasMavenMaxPermgenSetting();

        assertFalse( "Max PermGen was invalid, should be false.", setting );
    }

    /**
     * Test method for {@link MaxPermgenSizeUtil#envHasMavenMaxPermgenSetting()}.
     * The scenario is we set the environment map to one that has a maven setting
     * but a heap size setting that lacks the required unit designation.
     * The expected result is that it returns <code>false</code>.
     */
    public void testHasMaxPermgenSetting_noUnits()
    {
        Map<String, String> noMaxPermgenMap = new HashMap<String, String>();

        noMaxPermgenMap.put( MaxPermgenSizeUtil.MAVEN_OPTIONS, "-XX:MaxPermSize=42" );

        MaxPermgenSizeUtil.getInstance().setEnvSettings( noMaxPermgenMap );

        boolean setting = MaxPermgenSizeUtil.getInstance().envHasMavenMaxPermgenSetting();

        assertFalse( "Max PermGen setting was invalid, should be false.", setting );
    }

    /**
     * Test method for {@link MaxPermgenSizeUtil#envHasMavenMaxPermgenSetting()}.
     * The scenario is we set 128m in the evironment map.
     * The expected result is that it returns <code>true</code>.
     */
    public void testHasMaxPermgenSetting_128m()
    {
        Map<String, String> fakeEnv = new HashMap<String, String>();
        StringBuilder envValue = new StringBuilder();
        envValue.append(" -Xms256m");
        envValue.append(" -Xmx128m");
        envValue.append(" -XX:MaxPermSize=128m"); 
        envValue.append(" -XX:+UseCompressedOops"); 
        fakeEnv.put( MaxPermgenSizeUtil.MAVEN_OPTIONS, envValue.toString());

        MaxPermgenSizeUtil.getInstance().setEnvSettings( fakeEnv );

        boolean hasSetting = MaxPermgenSizeUtil.getInstance().envHasMavenMaxPermgenSetting();

        assertTrue( "Max PermGen set to a valid value, should be true.", hasSetting );
    }

    /**
     * Test method for {@link MaxPermgenSizeUtil#getMavenMaxPermgenSetting()}.
     * The scenario is we set 128m in the evironment map.
     * The expected result is that 128m is returned.
     */
    public void testGetMaxPermgenSetting_128m()
    {
        final String expectedMemSetting = "128m";

        Map<String, String> fakeEnv = new HashMap<String, String>();
        StringBuilder envValue = new StringBuilder();
        envValue.append(" -Xms256m");
        envValue.append(" -Xmx128m");
        envValue.append(" -XX:MaxPermSize=128m"); 
        envValue.append(" -XX:+UseCompressedOops"); 
        fakeEnv.put( MaxPermgenSizeUtil.MAVEN_OPTIONS, envValue.toString());

        MaxPermgenSizeUtil.getInstance().setEnvSettings( fakeEnv );

        String actualSetting = MaxPermgenSizeUtil.getInstance().getMavenMaxPermgenSetting();

        assertEquals( expectedMemSetting, actualSetting );
    }

    /**
     * Test method for {@link MaxPermgenSizeUtil#getMavenMaxPermgenSetting()}.
     * The scenario is we set the environment map to one that lacks a maven setting.
     * The expected result is that the method returns <code>null</code> without error.
     */
    public void testGetMaxPermgenSetting_none()
    {
        Map<String, String> noSettingsEnvMap = new HashMap<String, String>();

        MaxPermgenSizeUtil.getInstance().setEnvSettings( noSettingsEnvMap );

        String actualSetting = MaxPermgenSizeUtil.getInstance().getMavenMaxPermgenSetting();

        assertNull( "No max permgen set, should be null.", actualSetting );
    }
}
