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
package org.activiti.examples.bpmn.event.timer;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Joram Barrez
 */
public class BoundaryTimerEventTest {
  @Rule public ActivitiRule activitiRule = new ActivitiRule();

  @Deployment
  @Test 
  public void testInterruptingTimerDuration() {
    
    Date startTime = new Date();

    // Start process instance
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("interruptingBoundaryTimer");

    // There should be one task, with a timer : first line support
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
    assertEquals("First line support", task.getName());

    // Set clock to the future such that the timer can fire
    ClockUtil.setCurrentTime(new Date(startTime.getTime() + (5 * 60 * 60 * 1000)));
    waitForJobExecutorToProcessAllJobs(10000L, 250L);

    // The timer has fired, and the second task (secondlinesupport) now exists
    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
    assertEquals("Second line support", task.getName());
  }

  private void waitForJobExecutorToProcessAllJobs(long maxMillisToWait, long intervalMillis) {
	    JobExecutor jobExecutor = ((ProcessEngineImpl)activitiRule.getProcessEngine()).getJobExecutor();
	    jobExecutor.start();

	    try {
	      Timer timer = new Timer();
	      InteruptTask task = new InteruptTask(Thread.currentThread());
	      timer.schedule(task, maxMillisToWait);
	      boolean areJobsAvailable = true;
	      try {
	        while (areJobsAvailable && !task.isTimeLimitExceeded()) {
	          Thread.sleep(intervalMillis);
	          areJobsAvailable = areJobsAvailable();
	        }
	      } catch (InterruptedException e) {
	      } finally {
	        timer.cancel();
	      }
	      if (areJobsAvailable) {
	        throw new ActivitiException("time limit of " + maxMillisToWait + " was exceeded");
	      }

	    } finally {
	      jobExecutor.shutdown();
	    }
	  }
  
  public boolean areJobsAvailable() {
	    return !activitiRule.getManagementService()
	      .createJobQuery()
	      .executable()
	      .list()
	      .isEmpty();
	  }

	  private static class InteruptTask extends TimerTask {
	    protected boolean timeLimitExceeded = false;
	    protected Thread thread;
	    public InteruptTask(Thread thread) {
	      this.thread = thread;
	    }
	    public boolean isTimeLimitExceeded() {
	      return timeLimitExceeded;
	    }
	    public void run() {
	      timeLimitExceeded = true;
	      thread.interrupt();
	    }
	  }  
}

