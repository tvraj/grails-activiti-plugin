import grails.util.GrailsNameUtils

includeTargets << grailsScript("Init")

target(main: "Change all JUnit3 style examples to JUnit4") { changeToJUnit4Style() }

private changeToJUnit4Style() {
	println "Changing example test cases to JUnit4 style..."
	removeImport = "org.activiti.engine.impl.test.ActivitiInternalTestCase"
	addImports = ["org.activiti.engine.test.ActivitiRule",
								"org.activiti.engine.*",
								"org.junit.*",
								"static org.junit.Assert.*"]
	removeExtend = "extends ActivitiInternalTestCase {"
	addActivitiRuleLine = "@Rule public ActivitiRule activitiRule = new ActivitiRule();"
	addTestAnnotation = "@Test"
	testMethod = "public void test"
	activitiServices = [identityService:false,
											managementService:false,
											processEngine:false,
											runtimeService:false,
											repositoryService:false,
											taskService:false, 
											historyService:false]
	new File("${basedir}/src/activiti-examples").eachFileRecurse { testFile ->
		if (testFile.isFile() && testFile.text.indexOf(removeImport) > -1) {
			generatedFile = new File("${testFile.absolutePath}.tmp")
			generatedFile.withWriter { file ->
				testFile.eachLine { line ->
					if (line.indexOf(removeImport) > -1) {
						addImports.each {
							file.writeLine( "import $it;" )
						}
					} else if (line.indexOf(removeExtend) > -1) {
						file.writeLine( line.replace(removeExtend, "{\n    $addActivitiRuleLine") )
					} else if (line.indexOf(testMethod) > -1) {
						file.writeLine("  $addTestAnnotation")
						file.writeLine(line)
						activitiServices.each { k, v -> 
							activitiServices[k] = false
						}
					} else { 
						service = isActivitiServicesFound(line, activitiServices)
						if (service) {
							activitiServices[service] = true 
							serviceClassNameRepresentation = GrailsNameUtils.getClassNameRepresentation(service)
							file.writeLine("    ${serviceClassNameRepresentation} ${service} = activitiRule.get${serviceClassNameRepresentation}();")
						} 
						file.writeLine(line)
					}
				}
			}
			ant.move file:generatedFile.absolutePath, tofile:testFile.absolutePath
		}
	}
}

private isActivitiServicesFound(def line, def activitiServices) {
	String serviceFound = null
	activitiServices.each { k, v -> 
		if (!v && serviceFound == null && line.indexOf(k) > -1) {
			serviceFound = k
		}
	}	
	return serviceFound
}


setDefaultTarget(main)
