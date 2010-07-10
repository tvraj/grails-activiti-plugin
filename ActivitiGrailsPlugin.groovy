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
 
import org.activiti.GrailsDbProcessEngineBuilder
import org.activiti.IdentityService
import org.activiti.ManagementService
import org.activiti.ProcessEngine
import org.activiti.ProcessService
import org.activiti.TaskService
import org.activiti.DbSchemaStrategy
import grails.util.Environment
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

 /**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 */
class ActivitiGrailsPlugin {
    // the plugin version
    def version = "5.0.alpha3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Lim Chee Kin"
    def authorEmail = "limcheekin@vobject.com"
    def title = "Grails Activiti Plugin"
    def description = '''\\
 Grails Activiti Plugin is created to integrate Activiti BPM Suite and workflow system to Grails Framework. 
 With the Grails Activiti Plugin, workflow application can be created at your fingertips! 

 Project Site and Documentation: http://code.google.com/p/grails-activiti-plugin/
 Support: http://code.google.com/p/grails-activiti-plugin/issues/list 
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/activiti"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
    if (Environment.current == Environment.DEVELOPMENT ||
				Environment.current == Environment.PRODUCTION) {
      processEngineBuilder(GrailsDbProcessEngineBuilder) { 
			  processEngineName = CH.config.activiti.processEngineName?:"grails-activiti"
			  databaseName = CH.config.activiti.databaseName?:"h2-in-memory" // h2
			  dbSchemaStrategy = CH.config.activiti.dbSchemaStrategy?:DbSchemaStrategy.CREATE_DROP
		    jdbcDriver = CH.config.activiti.jdbcDriver?:"org.h2.Driver"	
		    jdbcUrl = CH.config.activiti.jdbcUrl?:"jdbc:h2:mem:activiti" // jdbc:h2:tcp://localhost/activiti
			  jdbcUsername = CH.config.activiti.jdbcUsername?:"sa"
			  jdbcPassword = CH.config.activiti.jdbcPassword?:""
			  jobExecutorAutoActivation = CH.config.activiti.jobExecutorAutoActivation?:false
			}
    	processEngine(processEngineBuilder:"buildProcessEngine")
    	processService(processEngine:"getProcessService") 
      taskService(processEngine:"getTaskService") 
      managementService(processEngine:"getManagementService") 
      identityService(processEngine:"getIdentityService")
    	}
            
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
