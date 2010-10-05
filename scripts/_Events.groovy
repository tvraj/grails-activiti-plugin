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
 
includeTargets << grailsScript("_GrailsPackage")

eventTestPhasesStart = {
	ant.echo "eventTestPhasesStart invoked."
	ensureAllGeneratedFilesDeleted()	  
	createActivitiPropertiesFile(build.settings.resourcesDir.toString())
}

eventTestPhasesEnd = {
    ant.echo "eventTestPhasesEnd invoked."
	ant.delete file:"${build.settings.resourcesDir}/activiti.properties" 
}

eventDeployBarStart = { 
	ant.echo "eventDeployBarStart invoked."
	ensureAllGeneratedFilesDeleted()
    createActivitiPropertiesFile("${activitiPluginDir}/grails-app/conf")
}
 
eventDeployBarEnd = { 
	ant.echo "eventDeployBarEnd invoked."
	ant.delete file:"${activitiPluginDir}/grails-app/conf/activiti.properties" 
}

private void ensureAllGeneratedFilesDeleted() {
	if (new File("${build.settings.resourcesDir}/activiti.properties").exists()) {
		  ant.delete file:"${build.settings.resourcesDir}/activiti.properties" 
	}
	if (new File("${activitiPluginDir}/grails-app/conf/activiti.properties").exists()) {
		  ant.delete file:"${activitiPluginDir}/grails-app/conf/activiti.properties" 
	}	
}

private void createActivitiPropertiesFile(String activitiPropertiesFilePath) {
	createConfig()
	def activitiPropertiesFile = new File(activitiPropertiesFilePath, "activiti.properties")
	activitiPropertiesFile.withWriter {
		it.writeLine """database=${config.activiti.dataBaseName}
jdbc.driver=${config.dataSource.driverClassName}
jdbc.url=${config.dataSource.url}
jdbc.username=${config.dataSource.username}
jdbc.password=${config.dataSource.password}
db.schema.strategy=${config.activiti.dbSchemaStrategy?:"create-drop"}
job.executor.auto.activate=${config.activiti.jobExecutorAutoActivate}
mail.smtp.host=${config.activiti.mailServerHost}
mail.smtp.port=${config.activiti.mailServerPort}
mail.smtp.user=${config.activiti.mailServerUserName}
mail.smtp.password=${config.activiti.mailServerPassword}
mail.default.from=${config.activiti.mailServerDefaultFromAddress}
"""
	  }
	 ant.echo "Content of generated activiti.properties file:"
	 ant.echo activitiPropertiesFile.text
}	

eventCreateWarStart = {warname, stagingDir ->
  if (grailsEnv == "production") {
      ant.echo "Remove unnecessary JAR files..."
      ant.delete file:"${stagingDir}/WEB-INF/lib/subethasmtp-smtp-1.2.jar"
		  ant.delete file:"${stagingDir}/WEB-INF/lib/subethasmtp-wiser-1.2.jar"
    }
}
