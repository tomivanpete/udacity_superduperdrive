package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultPage {

    protected WebDriver driver;

    @FindBy(id = "msg-container")
    private WebElement messageContainer;

    @FindBy(id = "success-msg")
    private WebElement successMessage;

    @FindBy(id = "error-msg-generic")
    private WebElement errorMessageGeneric;

    @FindBy(id = "error-msg-specific")
    private WebElement errorMessageSpecific;

    @FindBy(linkText = "here")
    private WebElement homeLink;

    public ResultPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.visibilityOf(messageContainer));
    }

    public String getMessageText() {
        String messageTitle = null;
        String messageDescription = null;

        if (successMessage.isDisplayed()) {
            messageTitle = successMessage.findElement(By.className("display-5")).getText();
            messageDescription = successMessage.findElement(By.tagName("span")).getText();
        } else if (errorMessageGeneric.isDisplayed()) {
            messageTitle = errorMessageGeneric.findElement(By.className("display-5")).getText();
            messageDescription = errorMessageGeneric.findElement(By.tagName("span")).getText();
        } else if (errorMessageSpecific.isDisplayed()) {
            messageTitle = errorMessageSpecific.findElement(By.className("display-5")).getText();
            messageDescription = errorMessageSpecific.findElement(By.tagName("span")).getText();
        }

        return messageTitle + " " + messageDescription;
    }

    public HomePage clickHomeLink() {
        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        js.executeScript("arguments[0].click()", homeLink);
        return new HomePage(driver);
    }
}
