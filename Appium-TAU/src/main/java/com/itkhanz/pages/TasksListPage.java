package com.itkhanz.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class TasksListPage  extends BasePage {
    public TasksListPage(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(id = "fab")
    @iOSXCUITFindBy(accessibility = "plus.circle")
    WebElement addTaskBtn;

    @AndroidFindBy(id = "textViewListView")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeCell//XCUIElementTypeStaticText)[3]")
    WebElement taskText;

    public CreateTaskPage clickAddTaskBtn() {
        click(addTaskBtn);
        return new CreateTaskPage(driver);
    }

    public String getTaskText() {
        return getAttribute(taskText, "text");
    }

    public String getTaskName() {
        return getAttribute(taskText, "name");
    }
}
