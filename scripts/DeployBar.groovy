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
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.GrailsUtil

 /**
 *
 * Deployment script to deploy business archive (BAR).
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.alpha3
 * 
 */
includeTargets << grailsScript("Init")
includeTargets << grailsScript("Compile")

ant.property(environment:"env")
ant.property (file : "application.properties")
appName = ant.project.properties.'app.name'
appVersion = ant.project.properties.'app.version'

target(activitiPath: "Declare a path that includes the activiti-engine.jar and all its dependencies") {
	ant.path(id: "activiti.libs.incl.dependencies") {
		pathelement(location:"${basedir}/target/classes")
		fileset(dir:"${basedir}/lib")
	}
}

target(deploy: "Deploy Activiti's Business Archive (BAR) and JAR") {
	depends(createJar, activitiPath, createBar)
	// ant.property (name: "activitiClassPath", refid: "activiti.libs.incl.dependencies")
	// ant.echo (message: "ACTIVITI_CLASSPATH: ${ant.project.properties.activitiClassPath}")
	ant.copy file:"${activitiPluginDir}/src/activiti.properties", todir:"${basedir}/target/classes" 
	ant.taskdef (name: 'deployBar', classname : "org.activiti.impl.ant.DeployBarTask", classpathref: "activiti.libs.incl.dependencies")	
	ant.deployBar (file: "${basedir}/target/${appName}-${appVersion}.bar")
	ant.delete file:"${basedir}/target/classes/activiti.properties"
}

target(createBar: "Create JBpm Business Archive (BAR) that contains process files and process resources") {
	ant.jar (destfile: "${basedir}/target/${appName}-${appVersion}.bar") {
		fileset(dir:"${basedir}/grails-app/conf") {
			include(name: "**/*.bpmn*.xml")
		}		
		if (new File("${basedir}/src/taskforms").exists()) {  
			fileset(dir:"${basedir}/src/taskforms") {
				include(name: "**/*.form")
			}			
		}	
	}	
}

target(createJar: "Create Java Archive (JAR) that contains process executables (.class file)") {
	depends(clean, compile)
	ant.jar (destfile: "${basedir}/target/${appName}-${appVersion}.jar") {
		fileset(dir:"${basedir}/target/classes") {
			include(name: "**/*.class")
		}		
	}
}

private void printClassPath(classLoader) {
	def urlPaths = classLoader.getURLs()
    println "classLoader: $classLoader"
    println urlPaths*.toString()
    if (classLoader.parent) {
         printClassPath(classLoader.parent)
    }
} 

setDefaultTarget(deploy)