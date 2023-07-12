package com.itkhanz.tests.todoapp.chap10;

import com.itkhanz.pages.CreateTaskPage;
import com.itkhanz.pages.TasksListPage;
import com.itkhanz.utils.PropertyUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AndroidParallelTests extends ParallelBaseTest{
    CreateTaskPage createTaskPage;
    TasksListPage tasksListPage;

    @Test
    public void test_add_task1() {
        //Start appium server with
        //appium -p 1000 --use-drivers=uiautomator2
        android_Setup("1000", PropertyUtils.getDeviceProp("ANDROID_UDID"), 8201);
        tasksListPage = new TasksListPage(getDriver());
        createTaskPage = tasksListPage.clickAddTaskBtn();
        createTaskPage
                .enterTaskName("Finish Appium Course")
                .enterTaskDesc("Finishing my course ASAP")
                .clickSaveBtn();
        Assert.assertEquals(tasksListPage.getTaskText(), "Finish Appium Course");
        tearDown();
    }

    @Test
    public void test_add_task2() {
        //Start appium server with
        //appium -p 2000 --use-drivers=uiautomator2
        android_Setup("2000", PropertyUtils.getDeviceProp("ANDROID_UDID_2"), 8202);
        tasksListPage = new TasksListPage(getDriver());
        createTaskPage = tasksListPage.clickAddTaskBtn();
        createTaskPage
                .enterTaskName("Finish Appium Course")
                .enterTaskDesc("Finishing my course ASAP")
                .clickSaveBtn();
        Assert.assertEquals(tasksListPage.getTaskText(), "Finish Appium Course");
        tearDown();
    }
}
