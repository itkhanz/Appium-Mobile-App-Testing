package com.itkhanz.tests.todoapp.chap7;

import com.itkhanz.pages.CreateTaskPage;
import com.itkhanz.pages.TasksListPage;
import com.itkhanz.tests.todoapp.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.itkhanz.constants.Paths.TEST_DATA_DIR;

public class ToDoAndroidTest extends BaseTest {
    CreateTaskPage createTaskPage ;
    TasksListPage tasksListPage;

    @Test
    public void test_add_task() {
        android_Setup();
        tasksListPage = new TasksListPage(driver);
        createTaskPage = tasksListPage.clickAddTaskBtn();
        createTaskPage
                .enterTaskName("Finish Appium Course")
                .enterTaskDesc("Finishing my course ASAP")
                .clickSaveBtn();

        //hiding Android keyboard
        //((AndroidDriver)driver).hideKeyboard();
        Assert.assertEquals(tasksListPage.getTaskText(), "Finish Appium Course");

        tearDown();
    }
}
