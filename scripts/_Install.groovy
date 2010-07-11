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
 * Post installation script that create taskforms directory and append 
 *  activiti's configuration to grails-app/conf/Config.groovy.
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 */

ant.mkdir(dir:"${basedir}/src/taskforms")
/* 
* Required to move juel-2.1.0.jar to ${basedir}/lib 
* to solve java.lang.NoSuchMethodError: javax.el.ExpressionFactory.newInstance().
* Due to javax.el package conflict with tomcat-core.jar of tomcat plugin
*/
ant.move file:"${pluginBasedir}/lib/juel-2.1.0.jar", todir:"${basedir}/lib" 
// default activiti-cfg.jar, to be overwritten by user for application specific settings
// ant.move file:"${pluginBasedir}/lib/activiti-cfg.jar", todir:"${basedir}/lib" 
updateConfig()
ant.echo '''
************************************************************
* Your grails-app/conf/Config.groovy has been updated with *
* default configurations of Activiti;                      *
* please verify that the values are correct.               *
************************************************************
'''
private void updateConfig() {

	def configFile = new File(basedir, 'grails-app/conf/Config.groovy')
	if (configFile.exists()) {
		configFile.withWriterAppend {
			it.writeLine '\n// Added by the Grails Activiti plugin:'
			it.writeLine '''activiti {
				processEngineName = "activiti-engine-default"
			  databaseName = "h2" 
			  dbSchemaStrategy = org.activiti.DbSchemaStrategy.CHECK_VERSION
		    jdbcDriver = "org.h2.Driver"	
		    jdbcUrl = "jdbc:h2:tcp://localhost/activiti"
			  jdbcUsername = "sa"
			  jdbcPassword = ""
			  jobExecutorAutoActivation = false
}
environments {
    development {
        activiti {
			  processEngineName = "activiti-engine-dev"
        }
    }
    production {
        activiti {
			  processEngineName = "activiti-engine-prod"
			  jobExecutorAutoActivation = true
        }
    }
}				
			'''
		}
	}
}