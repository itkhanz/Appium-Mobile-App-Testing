package com.itkhanz.tests.todoapp.chap9.steps.simple;

import com.itkhanz.pages.CreateTaskPage;
import com.itkhanz.pages.TasksListPage;
import com.itkhanz.tests.todoapp.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CreateNewTaskSteps extends BaseTest {
    CreateTaskPage createTaskPage ;
    TasksListPage tasksListPage;

    @Given("Click Add new Task")
    public void clickAddNewTask() {
        android_Setup();
        tasksListPage = new TasksListPage(driver);
        createTaskPage = tasksListPage.clickAddTaskBtn();
    }

    @And("Enter TaskName")
    public void enterTaskName() {
        createTaskPage.enterTaskName("Finish Appium Course");
    }

    @And("Enter TaskDesc")
    public void enterTaskDesc() {
        createTaskPage.enterTaskDesc("Finishing my course ASAP");
    }

    @When("Click Save")
    public void clickSave() {
        createTaskPage.clickSaveBtn();
    }

    @Then("Task added successfully")
    public void taskAddedSuccessfully() {
        Assert.assertEquals(tasksListPage.getTaskText(), "Finish Appium Course");
        tearDown();
    }
}
