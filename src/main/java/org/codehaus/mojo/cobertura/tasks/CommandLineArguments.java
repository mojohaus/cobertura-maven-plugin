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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cobertura.util.CommandLineBuilder;

/**
 * CommandLineArguments allows for arbitrarily long command line argument lists.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public class CommandLineArguments
{
    private List args;

    private boolean useCommandsFile;

    public CommandLineArguments()
    {
        this.args = new ArrayList();
        this.useCommandsFile = false;
    }

    public void addArg( String arg )
    {
        this.args.add( arg );
    }

    public void addArg( String arg1, String arg2 )
    {
        this.args.add( arg1 );
        this.args.add( arg2 );
    }

    public List getArgs()
    {
        return this.args;
    }

    /**
     * Generate the Commands file and return the filename to it.
     * 
     * @return the commands filename.
     * @throws IOException
     */
    public String getCommandsFile()
        throws IOException
    {
        CommandLineBuilder builder = new CommandLineBuilder();
        Iterator it = this.args.iterator();
        while ( it.hasNext() )
        {
            builder.addArg( it.next().toString() );
        }
        builder.saveArgs();
        return builder.getCommandLineFile();
    }

    public Iterator iterator()
    {
        return this.args.iterator();
    }

    /**
     * @param useCommandsFile The useCommandsFile to set.
     */
    public void setUseCommandsFile( boolean useCommandsFile )
    {
        this.useCommandsFile = useCommandsFile;
    }

    /**
     * @return Returns the useCommandsFile.
     */
    public boolean useCommandsFile()
    {
        return useCommandsFile;
    }
}
