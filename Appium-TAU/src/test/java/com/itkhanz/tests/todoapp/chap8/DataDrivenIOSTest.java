package com.itkhanz.tests.todoapp.chap8;

import com.itkhanz.pages.CreateTaskPage;
import com.itkhanz.pages.TasksListPage;
import com.itkhanz.tests.todoapp.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DataDrivenIOSTest extends BaseTest {
    CreateTaskPage createTaskPage ;
    TasksListPage tasksListPage;


    @Test(dataProvider = "tasks data")
    public void addTaskIOSTest(String taskName, String taskDesc) {
        iOS_Setup();
        tasksListPage = new TasksListPage(driver);
        createTaskPage = tasksListPage.clickAddTaskBtn();
        createTaskPage
                .enterTaskName(taskName)
                .enterTaskDesc(taskDesc)
                .clickSaveBtn();
        Assert.assertEquals(tasksListPage.getTaskName(), taskName);
        tearDown();
    }
}
