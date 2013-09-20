/*******************************************************************************
 * Copyright (c) 2013
 * Institute of Computer Aided Automation, Automation Systems Group, TU Wien.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * This file is part of the IoTSyS project.
 ******************************************************************************/

package at.ac.tuwien.auto.iotsys.gateway.connectors.weatherforecast;

import java.util.logging.Logger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import at.ac.tuwien.auto.iotsys.commons.Connector;

public class WeatherForecastConnector implements Connector {
	
	private static final Logger log = Logger.getLogger(WeatherForecastConnector.class.getName());
	
	private HttpURLConnection httpConnection = null;
	private DocumentBuilder docBuilder = null;
	
	public WeatherForecastConnector() throws FactoryConfigurationError, ParserConfigurationException {
		this.httpConnection = null;
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		this.docBuilder = docBuilderFactory.newDocumentBuilder();
	}
	
	public void connect() {
		// nothing to do
	}
	
	public void disconnect() {
		// nothing to do
	}
	
	public Document getWeatherForecastAsXML(String serviceURL) throws IOException, MalformedURLException, SAXException
	{ 
        log.info("Retrieving weather forecast from " + serviceURL + ".");
        
        Document result = null;
        
        if (docBuilder != null)
        {
			connectToURL(serviceURL);
			
			if (httpConnection.getResponseCode() == 200)						
				result = docBuilder.parse(httpConnection.getInputStream());
			
	        disconnectFromURL();
        }
      
        return result;
	}
		
	private void connectToURL(String serviceURL) throws IOException, MalformedURLException
	{
		log.info("Connecting to weather service.");
		
		httpConnection = (HttpURLConnection) (new URL(serviceURL)).openConnection();
		httpConnection.setRequestMethod("GET");
		httpConnection.setConnectTimeout(10000); // in milliseconds
		httpConnection.setDoInput(true); // use connection for input
				
		httpConnection.connect();
	}

	private void disconnectFromURL()
	{
		log.info("Disconnecting from weather service.");

		if (httpConnection != null)
			httpConnection.disconnect();
		
		httpConnection = null;
	}
}