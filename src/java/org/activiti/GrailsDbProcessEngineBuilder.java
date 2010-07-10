package org.activiti;
/**
 * 
 */

/**
 * @author limcheekin
 *  This class is created to fix org.springframework.beans.NotWritablePropertyException: 
 *  Invalid property 'jobExecutorAutoActivation' of bean class [org.activiti.DbProcessEngineBuilder]
 */
public class GrailsDbProcessEngineBuilder extends DbProcessEngineBuilder {
	public boolean isJobExecutorAutoActivation() {
	    return jobExecutorAutoActivate;
	  }
}
