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

import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import grails.util.GrailsNameUtils

  /**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta2
 */
abstract class ActivitiController {
	  def activitiService

		/* def start = {
			activitiService.with {
				params.username = session.username
				ProcessInstance pi = startProcess(params)
				Task task = getUnassignedTask(session.username, pi.id)
				claimTask(task.id, session.username)
				redirect uri:getTaskForm(task.id)			 
			}
		}		
		
		def startTask = { 
				activitiService.with {					
					claimTask(params.taskId, session.username)
					redirect uri:getTaskForm(params.taskId)		
				}
		}				
				
		def getForm = {
			 redirect uri: activitiService.getTaskForm(params.taskId)
		}			
		
		*/
		
		def saveTask(Map params) {
			 params.domainClassName = getDomainClassName()
			 activitiService.setTaskFormUri(params)
		}				
		
		def completeTask(Map params) {
			 params.domainClassName = getDomainClassName()
			 activitiService.completeTask(params.taskId, params)
		}						
				
		def claimTask(String taskId) {		
			 activitiService.claimTask(taskId, session.username)
		}			
		
		def revokeTask(String taskId) {		
			 activitiService.claimTask(taskId, null)
		}					
		
		def deleteTask(String taskId) {
			 String domainClassName = null		
			 if (this.class != org.grails.activiti.TaskController) {
				  	domainClassName = getDomainClassName()
			  }
			 activitiService.deleteTask(taskId, domainClassName)
		}		

		private getDomainClassName() {
			 return "${this.class.package.name}.${GrailsNameUtils.getLogicalName(this.class.name, 'Controller')}"
		}
		
		def setAssignee(String taskId, String username) {		
			 if (username) {
				 activitiService.setAssignee(taskId, username)
			 } else {
				 revokeTask(taskId)
			 }
		}						
		
		def setPriority(String taskId, int priority) {		
			 activitiService.setPriority(taskId, priority)
		}					
				

		def getUnassignedTasksCount() {
				activitiService.getUnassignedTasksCount(session.username)
		}
		
		def getAssignedTasksCount() {
				activitiService.getAssignedTasksCount(session.username)
		}		
		
		def getAllTasksCount() {
				activitiService.getAllTasksCount()
		}
		
		def findUnassignedTasks(Map params) {
				params.username=session.username
		    if (!params.sort) {
					params.sort = "id"
					params.order = "desc"
		    	}			
				activitiService.findUnassignedTasks(params)
		}
		
		def findAssignedTasks(Map params) {
				params.username=session.username
		    if (!params.sort) {
					params.sort = "id"
					params.order = "desc"
		    	}			
				activitiService.findAssignedTasks(params)
		}
		
		def findAllTasks(Map params) {
		    if (!params.sort) {
					params.sort = "id"
					params.order = "desc"
		    	}			
				activitiService.findAllTasks(params)
		}
}
