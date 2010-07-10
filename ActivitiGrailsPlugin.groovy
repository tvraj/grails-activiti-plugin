import org.activiti.GrailsDbProcessEngineBuilder
import org.activiti.IdentityService
import org.activiti.ManagementService
import org.activiti.ProcessEngine
import org.activiti.ProcessService
import org.activiti.TaskService
import org.activiti.DbSchemaStrategy
import grails.util.Environment
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class ActivitiGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Your name"
    def authorEmail = ""
    def title = "Plugin summary/headline"
    def description = '''\\
Brief description of the plugin.
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
