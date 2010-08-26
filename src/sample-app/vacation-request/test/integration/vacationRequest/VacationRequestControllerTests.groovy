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
package vacationRequest

  /**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 5.0.beta1
 */
class VacationRequestControllerTests extends GroovyTestCase {
	def processService
	def identityService
	String deploymentId
    protected void setUp() {
        super.setUp()
				deploymentId = processService.createDeployment()
											.name("vacationRequest")
											.addClasspathResource("VacationRequest.bpmn20.xml")                                       
											.deploy()
 				identityService.with {		
					saveUser(newUser("kermit"))
					saveUser(newUser("fozzie"))
					saveGroup(newGroup("management"))
					createMembership("fozzie", "management")
				}																
    }

    protected void tearDown() {
				processService.deleteDeploymentCascade(deploymentId)
 				identityService.with {
					deleteGroup("management")
          deleteUser("fozzie")
          deleteUser("kermit")				
				}						
        super.tearDown()
    }

    void testStartProcessRedirectedUrl() {
			def vrc = new VacationRequestController()
			vrc.startForm()
			assertEquals "vacationRequest/create", vrc.response.redirectedUrl
    }
}
