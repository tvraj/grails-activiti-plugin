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
 
includeTargets << grailsScript("_GrailsPackage")

eventTestPhasesStart = {
		ant.echo "eventTestPhasesStart invoked."
		ensureAllGeneratedFilesDeleted()	  
	  createActivitiPropertiesFile()
}

eventTestPhasesEnd = {
    ant.echo "eventTestPhasesEnd invoked."
	  ant.delete file:"${activitiPluginDir}/grails-app/conf/activiti.properties" 
}

eventDeployBarStart = { 
    ant.echo "eventDeployBarStart invoked."
	  ensureAllGeneratedFilesDeleted()
    createActivitiPropertiesFile()
    	ant.jar (destfile: "${activitiPluginDir}/lib/activiti-cfg.jar") {
			fileset(dir:"${activitiPluginDir}/grails-app/conf") {
				include(name: "activiti.properties")
			}		
		}
		ant.delete file:"${activitiPluginDir}/grails-app/conf/activiti.properties" 
}
 
eventDeployBarEnd = { 
	 ant.echo "eventDeployBarEnd invoked."
	 ant.delete file:"${activitiPluginDir}/lib/activiti-cfg.jar" 
}

private void ensureAllGeneratedFilesDeleted() {
	if (new File("${activitiPluginDir}/grails-app/conf/activiti.properties").exists()) {
		ant.delete file:"${activitiPluginDir}/grails-app/conf/activiti.properties" 
	}
	if (new File("${activitiPluginDir}/lib/activiti-cfg.jar").exists()) {
		ant.delete file:"${activitiPluginDir}/lib/activiti-cfg.jar" 
	}	
}

private void createActivitiPropertiesFile() {
		createConfig()
		def activitiPropertiesFile = new File("${activitiPluginDir}/grails-app/conf", "activiti.properties")
		activitiPropertiesFile.withWriter {
			it.writeLine """database=${config.activiti.databaseName}
jdbc.driver=${config.activiti.jdbcDriver}
jdbc.url=${config.activiti.jdbcUrl}
jdbc.username=${config.activiti.jdbcUsername}
jdbc.password=${config.activiti.jdbcPassword}
db.schema.strategy=${config.activiti.dbSchemaStrategy==org.activiti.DbSchemaStrategy.CREATE_DROP?"create-drop":"check-version"}
job.executor.auto.activate=${config.activiti.jobExecutorAutoActivation}
"""
	  }
	  ant.echo "Content of generated activiti.properties file:"
	  ant.echo activitiPropertiesFile.text
	  
}	
