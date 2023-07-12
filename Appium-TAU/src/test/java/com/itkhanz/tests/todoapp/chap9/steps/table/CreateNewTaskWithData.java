package com.itkhanz.tests.todoapp.chap9.steps.table;

import com.itkhanz.pages.CreateTaskPage;
import com.itkhanz.pages.TasksListPage;
import com.itkhanz.tests.todoapp.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CreateNewTaskWithData extends BaseTest {
    CreateTaskPage createTaskPage ;
    TasksListPage tasksListPage;

    @Given("Click add new Task")
    public void click_add_new_task() {
        android_Setup();
        tasksListPage = new TasksListPage(driver);
        createTaskPage = tasksListPage.clickAddTaskBtn();
    }
    @Given("Enter {string} and {string}")
    public void enter_and(String taskName, String taskDesc) {
        createTaskPage.enterTaskName(taskName);
        createTaskPage.enterTaskDesc(taskDesc);
    }

    @When("Click Save")
    public void click_save() {
        createTaskPage.clickSaveBtn();
    }

    @Then("Task {string} Added Successfully")
    public void task_added_successfully(String taskName) {
        Assert.assertEquals(tasksListPage.getTaskText(), taskName);
        tearDown();
    }
}
