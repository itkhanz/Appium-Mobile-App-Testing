package com.itkhanz.tests.todoapp.chap7;

import com.itkhanz.pages.CreateTaskPage;
import com.itkhanz.pages.TasksListPage;
import com.itkhanz.tests.todoapp.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ToDoIOSTest extends BaseTest {
    CreateTaskPage createTaskPage;
    TasksListPage tasksListPage;

    @Test
    public void test_add_task() {
        iOS_Setup();
        tasksListPage = new TasksListPage(driver);
        createTaskPage = tasksListPage.clickAddTaskBtn();
        createTaskPage
                .enterTaskName("Finish Appium Course")
                .enterTaskDesc("Finishing my course ASAP")
                .clickSaveBtn();

        //hiding iOS keyboard
        /*HashMap<String, String[]> map = new HashMap<String, String[]>();
        String[] keys = {"Done"};
        map.put("keys", keys);
        ((IOSDriver) driver).executeScript("mobile: hideKeyboard", map);*/

        Assert.assertEquals(tasksListPage.getTaskName(), "Finish Appium Course");

        tearDown();
    }
}
