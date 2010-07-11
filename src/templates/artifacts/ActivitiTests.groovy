@artifact.package@import grails.test.*
import org.junit.*
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*
import static org.junit.matchers.JUnitMatchers.*
import org.activiti.test.*

class @artifact.name@ {

    @Rule public LogInitializer logSetup = new LogInitializer()
  
    @Rule public ProcessDeployer deployer = new ProcessDeployer()

    @Before void setUp() {
    }

    @After void tearDown() {
    }

    @Test @ProcessDeclared void something() {

    }
}
