/* Copyright 2010 the original author or authors.
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
package org.grails.activiti

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
class ActivitiConstants {
	static List DEFAULT_DEPLOYMENT_RESOURCES = ["file:./grails-app/conf/**/*.bpmn*.xml",
	"file:./grails-app/conf/**/*.png", 
	"file:./src/taskforms/**/*.form"]
	static String DEFAULT_PROCESS_ENGINE_NAME = "grails-activiti-noconfig"
	static String DEFAULT_DATABASE_NAME = "h2-noconfig"
	static String DEFAULT_DB_SCHEMA_STRATEGY = "create-drop"
	static String DEFAULT_DEPLOYMENT_NAME = "deploymentName not defined."
	static Boolean DEFAULT_JOB_EXECUTOR_AUTO_ACTIVATE = false
	static String DEFAULT_MAIL_SERVER_HOST = "mailServerHost not defined."
	static String DEFAULT_MAIL_SERVER_PORT = "mailServerPort not defined."
	static String DEFAULT_MAIL_SERVER_USERNAME = "mailServerUserName not defined."
	static String DEFAULT_MAIL_SERVER_PASSWORD = "mailServerPassword not defined."
	static String DEFAULT_MAIL_SERVER_FROM_ADDRESS = "mailServerDefaultFromAddress not defined."
	static String PLUGIN_AUTO_DEPLOYMENT_NAME = "ActivitiPluginAutoDeploy"
	static String DEFAULT_SESSION_USERNAME_KEY = "username"
}
