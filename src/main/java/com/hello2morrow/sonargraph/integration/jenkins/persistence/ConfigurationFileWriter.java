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

import java.io.File;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hello2morrow.sonargraph.integration.jenkins.foundation.SonargraphLogger;

public class ConfigurationFileWriter
{

    public enum MandatoryParameter
    {
        ACTIVATION_CODE("activationCode"),
        INSTALLATION_DIRECTORY("installationDirectory"),
        LANGUAGES("languages"),
        SYSTEM_DIRECTORY("systemDirectory"),
        REPORT_DIRECTORY("reportDirectory"),
        REPORT_FILENAME("reportFileName"),
        REPORT_TYPE("reportType"),
        REPORT_FORMAT("reportFormat"),
        QUALITY_MODEL_FILE("qualityModelFile"),
        VIRTUAL_MODEL("virtualModel"),
        LICENSE_FILE("licenseFile"),
        WORKSPACE_PROFILE("workspaceProfile"),
        SNAPSHOT_DIRECTORY("snapshotDirectory"),
        SNAPSHOT_FILE_NAME("snapshotFileName"),
        LOG_FILE("logFile"),
        LOG_LEVEL("logLevel");

        private final String m_presentationName;

        private MandatoryParameter(final String presentationName)
        {
            assert presentationName != null
                    && presentationName.length() > 0 : "Parameter 'presentationName' of method 'MandatoryParameter' must not be empty";
            m_presentationName = presentationName;
        }

        public String getPresentationName()
        {
            return m_presentationName;
        }
    }

    private final File m_file;

    public ConfigurationFileWriter(final File file)
    {
        assert file != null : "Parameter 'file' of method 'ConfigurationFileWriter' must not be null";

        m_file = file;
    }

    public void createConfigurationFile(EnumMap<MandatoryParameter, String> parameters, PrintStream logger)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element sonargraphBuild = doc.createElement("sonargraphBuild");
            doc.appendChild(sonargraphBuild);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.ACTIVATION_CODE);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.LANGUAGES);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.INSTALLATION_DIRECTORY);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.SYSTEM_DIRECTORY);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.VIRTUAL_MODEL);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.REPORT_DIRECTORY);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.REPORT_FILENAME);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.REPORT_TYPE);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.REPORT_FORMAT);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.LICENSE_FILE);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.WORKSPACE_PROFILE);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.SNAPSHOT_DIRECTORY);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.SNAPSHOT_FILE_NAME);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.LOG_LEVEL);
            setStartupAttribute(sonargraphBuild, parameters, MandatoryParameter.LOG_FILE);

            Element failSet = doc.createElement("failSet");
            sonargraphBuild.appendChild(failSet);
            failSet.setAttribute("failOnEmptyWorkspace", "false");

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(m_file);

            transformer.transform(source, result);

        }
        catch (ParserConfigurationException pce)
        {
            SonargraphLogger.logToConsoleOutput(logger, Level.SEVERE, "Failed to create configuration file '", pce);
        }
        catch (TransformerException tfe)
        {
            SonargraphLogger.logToConsoleOutput(logger, Level.SEVERE, "Failed to create configuration file '", tfe);
        }
    }

    private void setStartupAttribute(Element element, EnumMap<MandatoryParameter, String> params, MandatoryParameter param)
    {
        final String value = params.get(param);
        if (value != null)
        {
            element.setAttribute(param.getPresentationName(), value);
        }
    }
}
