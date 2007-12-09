package org.codehaus.mojo.cobertura.configuration;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
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

import java.util.Set;

import junit.framework.TestCase;
import net.sourceforge.cobertura.ant.Regex;

/**
 * @author Edwin Punzalan
 */
public class ConfigCheckTest
    extends TestCase
{
    private ConfigCheck check;

    protected void setUp()
    {
        check = new ConfigCheck();
    }

    public void testAddRegex()
    {
        Regex regex = new Regex();

        check.addRegex( regex );

        Set regexes = check.getRegexes();

        assertEquals( 1, regexes.size() );

        assertTrue( regexes.contains( regex ) );
    }

    public void testCreateRegex()
    {
        Regex regex = check.createRegex();

        Set regexes = check.getRegexes();

        assertEquals( 1, regexes.size() );

        assertTrue( regexes.contains( regex ) );
    }

    public void testBranchRate()
    {
        String rate = "50";

        check.setBranchRate( rate );

        assertEquals( rate, check.getBranchRate() );
    }

    public void testLineRate()
    {
        String rate = "50";

        check.setLineRate( rate );

        assertEquals( rate, check.getLineRate() );
    }

    public void testTotalBranchRate()
    {
        String rate = "50";

        check.setTotalBranchRate( rate );

        assertEquals( rate, check.getTotalBranchRate() );
    }

    public void testTotalLineRate()
    {
        String rate = "50";

        check.setTotalLineRate( rate );

        assertEquals( rate, check.getTotalLineRate() );
    }

    public void testHaltOnFailure()
    {
        check.setHaltOnFailure( true );

        assertTrue( check.isHaltOnFailure() );

        check.setHaltOnFailure( false );

        assertFalse( check.isHaltOnFailure() );
    }
}
