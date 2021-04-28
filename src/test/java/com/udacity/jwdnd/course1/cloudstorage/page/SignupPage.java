package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignupPage {

    protected WebDriver driver;

    @FindBy(name = "firstName")
    private WebElement firstNameField;

    @FindBy(name = "lastName")
    private WebElement lastNameField;

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(className = "alert")
    private WebElement alertMsg;

    @FindBy(id = "success-msg")
    private WebElement successMsg;

    @FindBy(id = "error-msg")
    private WebElement errorMsg;

    @FindBy(id = "login-link")
    private WebElement loginLink;

    public SignupPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.visibilityOf(firstNameField));
    }

    public String createUser(String firstName, String lastName, String username, String password) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        js.executeScript("arguments[0].click()", submitButton);

        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.visibilityOf(alertMsg));

        return alertMsg.getAttribute("id");
    }

    public LoginPage clickLoginLink() {
        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.elementToBeClickable(loginLink));

        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        js.executeScript("arguments[0].click()", loginLink);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Login"));

        return new LoginPage(driver);
    }

    public boolean isSuccessMsgDisplayed() {
        return successMsg.isDisplayed();
    }

    public boolean isErrorMsgDisplayed() {
        return errorMsg.isDisplayed();
    }
}
