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
 *
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 */

ant.mkdir(dir:"${basedir}/src/taskforms")
/* 
* Required to move juel-2.2.1.jar to ${basedir}/lib 
* to solve java.lang.NoSuchMethodError: javax.el.ExpressionFactory.newInstance().
* Due to javax.el package conflict with tomcat-core.jar of tomcat plugin.
* ant.move and ant.delete not working in windows environment
*/
// ant.copy file:"${pluginBasedir}/lib/juel-2.2.1.jar", todir:"${basedir}/lib", overwrite:true

// Backup existing files
ant.move file:"${basedir}/grails-app/views/index.gsp", tofile:"${basedir}/grails-app/views/index.bak"
ant.move file:"${basedir}/grails-app/views/layouts/main.gsp", tofile:"${basedir}/grails-app/views/layouts/main.bak"

// Copy plugin related files
ant.copy file:"${pluginBasedir}/grails-app/views/index.gsp", todir:"${basedir}/grails-app/views"
ant.copy file:"${pluginBasedir}/grails-app/views/layouts/main.gsp", todir:"${basedir}/grails-app/views/layouts"
ant.copy file:"${pluginBasedir}/web-app/images/grails_activiti_logo.png", todir:"${basedir}/web-app/images"
ant.copy file:"${pluginBasedir}/web-app/images/grails_activiti_favicon.ico", todir:"${basedir}/web-app/images"

updateConfig()

private void updateConfig() {
	def configFile = new File(basedir, 'grails-app/conf/Config.groovy')
	if (configFile.exists() && configFile.text.indexOf("activiti") == -1) {
		configFile.withWriterAppend {
			it.writeLine '\n// Added by the Grails Activiti plugin:'
			it.writeLine '''activiti {
    processEngineName = "activiti-engine-default"
	  dataBaseName = "h2" 
	  deploymentName = appName
	  deploymentResources = ["file:grails-app/conf/**/*.bpmn*.xml", "file:src/taskforms/**/*.form"]
	  jobExecutorAutoActivate = false
	  mailServerHost = "smtp.yourserver.com"
	  mailServerPort = "25"
	  mailServerUserName = ""
	  mailServerPassword = ""
	  mailServerDefaultFromAddress = "username@yourserver.com"
}

environments {
    development {
        activiti {
			  processEngineName = "activiti-engine-dev"
			  dbSchemaStrategy = "create-drop" // "create-drop" or "check-version"	  
        }
    }
    test {
        activiti {
			  processEngineName = "activiti-engine-test"
			  dbSchemaStrategy = "create-drop"
        }
    }	
    production {
        activiti {
			  processEngineName = "activiti-engine-prod"
			  dbSchemaStrategy = "check-version"
			  jobExecutorAutoActivate = true
        }
    }
}	
'''

ant.echo '''
************************************************************
* Your grails-app/conf/Config.groovy has been updated with *
* default configurations of Activiti;                      *
* please verify that the values are correct.               *
************************************************************
'''
		}
	}
}