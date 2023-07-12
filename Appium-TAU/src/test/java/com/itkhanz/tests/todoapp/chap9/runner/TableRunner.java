package com.itkhanz.tests.todoapp.chap9.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/CreateTaskExampleTable.feature",
        glue = {"com.itkhanz.tests.todoapp.chap9.steps.table"},
        plugin = {"pretty","html:target/cucumber-html-report.html"}
)
public class TableRunner extends AbstractTestNGCucumberTests {
}
