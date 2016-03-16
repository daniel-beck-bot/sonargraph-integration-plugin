/*******************************************************************************
 * Jenkins Sonargraph Integration Plugin
 * Copyright (C) 2015-2016 hello2morrow GmbH
 * mailto: info AT hello2morrow DOT com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *******************************************************************************/
package com.hello2morrow.sonargraph.integration.jenkins.persistence;

import hudson.FilePath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextFileReader
{
    public String readLargeTextFile(final FilePath largeTextFilePath) throws IOException, InterruptedException
    {
        final StringBuilder completeTextFile = new StringBuilder();
        try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(largeTextFilePath.read())))
        {
            String currentLine;
            while ((currentLine = bfReader.readLine()) != null)
            {
                completeTextFile.append(currentLine);
            }
        }

        return completeTextFile.toString();
    }
}