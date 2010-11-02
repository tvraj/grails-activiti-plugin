/* Copyright 2006-2010 the original author or authors.
 *
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
 */

 /**
 * This event script improved the TestApp.groovy and DeployBar.groovy script 
 * by generating activiti.properties from activiti configurations in Config.groovy.
 * User of this plugin no longer need to maintain activiti.properties file separately. 
  * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
  *
 * @since 5.0.alpha3
  * 
 */
import grails.util.BuildSettingsHolder as build
import groovy.xml.MarkupBuilder
 
includeTargets << grailsScript("_GrailsPackage")

CONFIG_FILE = "activiti.cfg.xml"

eventTestPhasesStart = {
	ant.echo "eventTestPhasesStart invoked."
	ensureAllGeneratedFilesDeleted()	  
	createActivitiConfigFile(build.settings.resourcesDir.toString())
}

eventTestPhasesEnd = {
  ant.echo "eventTestPhasesEnd invoked."
	ant.delete file:"${build.settings.resourcesDir}/${CONFIG_FILE}" 
}

eventDeployBarStart = { 
	ant.echo "eventDeployBarStart invoked."
	ensureAllGeneratedFilesDeleted()
  createActivitiConfigFile("${activitiPluginDir}/grails-app/conf")
}
 
eventDeployBarEnd = { 
	ant.echo "eventDeployBarEnd invoked."
	ant.delete file:"${activitiPluginDir}/grails-app/conf/${CONFIG_FILE}" 
}

private void ensureAllGeneratedFilesDeleted() {
	if (new File("${build.settings.resourcesDir}/${CONFIG_FILE}").exists()) {
		  ant.delete file:"${build.settings.resourcesDir}/${CONFIG_FILE}" 
	}
	if (new File("${activitiPluginDir}/grails-app/conf/${CONFIG_FILE}").exists()) {
		  ant.delete file:"${activitiPluginDir}/grails-app/conf/${CONFIG_FILE}" 
	}	
}

private void createActivitiConfigFile(String activitiConfigFilePath) {
	createConfig()
	def activitiConfigFile = new File(activitiConfigFilePath, CONFIG_FILE)
	def writer = activitiConfigFile.newWriter()
	new MarkupBuilder(writer)."activiti-cfg"("process-engine-name":config.activiti.processEngineName) {
		database(type:config.activiti.databaseType, "schema-strategy": config.activiti.dbSchemaStrategy?:"create-drop") {
       jdbc(url:config.dataSource.url,
          driver:config.dataSource.driverClassName,
          username:config.dataSource.username,
          password:config.dataSource.password)
		}
		"job-executor"(activate:config.activiti.jobExecutorActivate)
		if (config.activiti.mailServerUsername) {
		mail(server:config.activiti.mailServerHost, 
			   port:config.activiti.mailServerPort,
				 username:config.activiti.mailServerUsername,
				 password:config.activiti.mailServerPassword,
				"default-from":config.activiti.mailServerDefaultFromAddress)
		} else {
		mail(server:config.activiti.mailServerHost, 
			   port:config.activiti.mailServerPort,
				"default-from":config.activiti.mailServerDefaultFromAddress)		
		}
	}
	writer.flush()
	ant.echo "Content of generated ${activitiConfigFile.absolutePath} file:"
  println activitiConfigFile.text
}	

eventCreateWarStart = {warname, stagingDir ->
  if (grailsEnv == "production") {
      ant.echo "Remove unnecessary JAR files..."
	  ["subethasmtp-smtp-1.2.jar", 
		  "subethasmtp-wiser-1.2.jar", 
		  "geronimo-jms_1.1_spec-1.0.1.jar",
		  "geronimo-jpa_3.0_spec-1.0.jar",
		  "geronimo-jta_1.1_spec-1.1.jar",
		  "mockito-core-1.8.2.jar",
		  "objenesis-1.0.jar",
		  "openjpa-1.2.2.jar",
		  "persistence-api-1.0.jar",
		  "serp-1.13.1.jar"
		 ].each { jar ->
      ant.delete file:"${stagingDir}/WEB-INF/lib/$jar"
      }
    }
}
