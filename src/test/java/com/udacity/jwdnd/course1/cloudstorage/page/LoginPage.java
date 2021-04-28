package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    protected WebDriver driver;

    @FindBy(id = "login-username")
    private WebElement usernameField;

    @FindBy(id = "login-password")
    private WebElement passwordField;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(className = "alert")
    private WebElement alertMsg;

    @FindBy(id = "error-msg")
    private WebElement errorMsg;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.visibilityOf(usernameField));
    }

    public HomePage loginValidUser(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        js.executeScript("arguments[0].click()", submitButton);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Home"));

        return new HomePage(driver);
    }

    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        js.executeScript("arguments[0].click()", submitButton);
    }

    public boolean isErrorMsgDisplayed() {
        return errorMsg.isDisplayed();
    }

    public String getAlertMessageId() {
        return alertMsg.getAttribute("id");
    }
}
