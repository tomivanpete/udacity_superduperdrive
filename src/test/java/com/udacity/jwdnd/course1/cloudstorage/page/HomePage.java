package com.udacity.jwdnd.course1.cloudstorage.page;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class HomePage {

    protected WebDriver driver;

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-files-tab")
    private WebElement filesTab;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "new-note-button")
    private WebElement newNoteButton;

    @FindBy(id = "noteTable")
    private WebElement noteTable;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(id = "noteDelete")
    private WebElement noteDeleteButton;

    @FindBy(id = "new-credential-button")
    private WebElement newCredentialButton;

    @FindBy(id = "credentialTable")
    private WebElement credentialTable;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id = "credentialSubmit")
    private WebElement credentialSubmitButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.visibilityOf(logoutButton));
    }

    public ResultPage createNewNote(String title, String description) {
        JavascriptExecutor js = (JavascriptExecutor) this.driver;

        js.executeScript("arguments[0].click()", notesTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(newNoteButton));

        js.executeScript("arguments[0].click()", newNoteButton);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(noteTitleField));

        noteTitleField.sendKeys(title);
        noteDescriptionField.sendKeys(description);
        js.executeScript("arguments[0].click()", noteSubmitButton);

        return new ResultPage(driver);
    }

    public ResultPage createNewCredential(String url, String username, String password) {
        JavascriptExecutor js = (JavascriptExecutor) this.driver;

        js.executeScript("arguments[0].click()", credentialsTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(newCredentialButton));

        js.executeScript("arguments[0].click()", newCredentialButton);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(credentialUrlField));

        credentialUrlField.sendKeys(url);
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.sendKeys(password);
        js.executeScript("arguments[0].click()", credentialSubmitButton);

        return new ResultPage(driver);
    }

    public List<Note> getNotes() {
        List<Note> homePageNotes = new ArrayList<>();

        JavascriptExecutor js = (JavascriptExecutor) this.driver;

        js.executeScript("arguments[0].click()", notesTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(noteTable));

        List<WebElement> noteTitles = noteTable.findElements(By.className("noteTitle"));
        List<WebElement> noteDescriptions = noteTable.findElements(By.className("noteDescription"));

        for (int i = 0; i < noteTitles.size(); i++) {
            Note note = new Note();
            note.setNoteTitle(noteTitles.get(i).getText());
            note.setNoteDescription(noteDescriptions.get(i).getText());
            homePageNotes.add(note);
        }

        return homePageNotes;
    }

    public LoginPage logout() {
        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.elementToBeClickable(logoutButton));

        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        js.executeScript("arguments[0].click()", logoutButton);

        return new LoginPage(driver);
    }
}
