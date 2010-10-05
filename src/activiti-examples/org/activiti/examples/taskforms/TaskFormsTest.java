/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.activiti.examples.taskforms;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.*;
import org.junit.*;

import static org.junit.Assert.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class TaskFormsTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();

  @Before
  public void setUp() throws Exception {
    IdentityService identityService = activitiRule.getIdentityService();
    identityService.saveUser(identityService.newUser("fozzie"));
    identityService.saveGroup(identityService.newGroup("management"));
    identityService.createMembership("fozzie", "management");
  }

  @After
  public void tearDown() throws Exception {
	  IdentityService identityService = activitiRule.getIdentityService();
    identityService.deleteGroup("management");
    identityService.deleteUser("fozzie");
  }

  @Deployment(resources = { 
    "org/activiti/examples/taskforms/VacationRequest.bpmn20.xml", 
    "org/activiti/examples/taskforms/approve.form", 
    "org/activiti/examples/taskforms/request.form", 
    "org/activiti/examples/taskforms/adjustRequest.form" })
  @Test 
  public void testTaskFormsWithVacationRequestProcess() {

    // Get start form
    TaskService taskService = activitiRule.getTaskService();
    Object startForm = taskService.getRenderedStartFormByKey("vacationRequest");
    assertNotNull(startForm);
    
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
    assertEquals("org/activiti/examples/taskforms/request.form", processDefinition.getStartFormResourceKey());

    // Define variables that would be filled in through the form
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("employeeName", "kermit");
    variables.put("numberOfDays", "4");
    variables.put("vacationMotivation", "I'm tired");
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("vacationRequest", variables);

    // Management should now have a task assigned to them
    Task task = taskService.createTaskQuery().candidateGroup("management").singleResult();
    assertEquals("Vacation request by kermit", task.getDescription());
    Object taskForm = taskService.getRenderedTaskForm(task.getId());
    assertNotNull(taskForm);

  }

  @Deployment
  @Test 
  public void testTaskFormUnavailable() {
    TaskService taskService = activitiRule.getTaskService();
    assertNull(taskService.getRenderedStartFormByKey("noStartOrTaskForm"));

    RuntimeService runtimeService = activitiRule.getRuntimeService();
    runtimeService.startProcessInstanceByKey("noStartOrTaskForm");
    Task task = taskService.createTaskQuery().singleResult();
    assertNull(taskService.getRenderedTaskForm(task.getId()));
  }

}
