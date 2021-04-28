package com.udacity.jwdnd.course1.cloudstorage.page;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
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

    @FindBy(id = "noteDeleteModal")
    private WebElement noteDeleteModal;

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

    @FindBy(id = "credentialDeleteModal")
    private WebElement credentialDeleteModal;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        new WebDriverWait(this.driver, 10)
                .until(ExpectedConditions.visibilityOf(logoutButton));
    }

    public ResultPage createNewNote(String title, String description) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click()", notesTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(newNoteButton));

        js.executeScript("arguments[0].click()", newNoteButton);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(noteTitleField));

        noteTitleField.sendKeys(title);
        noteDescriptionField.sendKeys(description);
        js.executeScript("arguments[0].click()", noteSubmitButton);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Result"));

        return new ResultPage(driver);
    }

    public ResultPage editNote(int noteIndex, String title, String description) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click()", notesTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(newNoteButton));

        WebElement noteEditButton = noteTable.findElements(By.className("btn-success")).get(noteIndex);

        js.executeScript("arguments[0].click()", noteEditButton);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(noteTitleField));

        noteTitleField.clear();
        noteTitleField.sendKeys(title);
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(description);
        js.executeScript("arguments[0].click()", noteSubmitButton);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Result"));

        return new ResultPage(driver);
    }

    public ResultPage deleteNote(int noteIndex) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click()", notesTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(newNoteButton));

        WebElement noteDeleteButton = noteTable.findElements(By.className("btn-danger")).get(noteIndex);

        js.executeScript("arguments[0].click()", noteDeleteButton);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(noteDeleteModal));

        WebElement confirmDeleteButton = noteDeleteModal.findElement(By.className("btn-danger"));
        js.executeScript("arguments[0].click()", confirmDeleteButton);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Result"));

        return new ResultPage(driver);
    }

    public ResultPage createNewCredential(String url, String username, String password) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

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

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Result"));

        return new ResultPage(driver);
    }

    public ResultPage editCredential(int credentialIndex, String url, String username, String password) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click()", credentialsTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(newCredentialButton));

        WebElement credentialEditButton = credentialTable.findElements(By.className("btn-success")).get(credentialIndex);

        js.executeScript("arguments[0].click()", credentialEditButton);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(credentialUrlField));

        credentialUrlField.clear();
        credentialUrlField.sendKeys(url);
        credentialUsernameField.clear();
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.clear();
        credentialPasswordField.sendKeys(password);
        js.executeScript("arguments[0].click()", credentialSubmitButton);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Result"));

        return new ResultPage(driver);
    }

    public ResultPage deleteCredential(int credentialIndex) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click()", credentialsTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(newCredentialButton));

        WebElement credentialDeleteButton = credentialTable.findElements(By.className("btn-danger")).get(credentialIndex);

        js.executeScript("arguments[0].click()", credentialDeleteButton);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(credentialDeleteModal));

        WebElement confirmDeleteButton = credentialDeleteModal.findElement(By.className("btn-danger"));
        js.executeScript("arguments[0].click()", confirmDeleteButton);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Result"));

        return new ResultPage(driver);
    }

    public List<Note> getNotes() {
        List<Note> homePageNotes = new ArrayList<>();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click()", notesTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(noteTable));

        List<WebElement> noteTitles = noteTable.findElements(By.className("note-title"));
        List<WebElement> noteDescriptions = noteTable.findElements(By.className("note-description"));

        for (int i = 0; i < noteTitles.size(); i++) {
            Note note = new Note();
            note.setNoteTitle(noteTitles.get(i).getText());
            note.setNoteDescription(noteDescriptions.get(i).getText());
            homePageNotes.add(note);
        }

        return homePageNotes;
    }

    public List<Credential> getCredentials() {
        List<Credential> homePageCredentials = new ArrayList<>();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click()", credentialsTab);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(credentialTable));

        List<WebElement> credentialUrls = credentialTable.findElements(By.className("credential-url"));
        List<WebElement> credentialUsernames = credentialTable.findElements(By.className("credential-username"));
        List<WebElement> credentialPasswords = credentialTable.findElements(By.className("credential-password"));

        for (int i = 0; i < credentialUrls.size(); i++) {
            Credential credential = new Credential();
            credential.setUrl(credentialUrls.get(i).getText());
            credential.setUsername(credentialUsernames.get(i).getText());
            credential.setPassword(credentialPasswords.get(i).getText());
            homePageCredentials.add(credential);
        }

        return homePageCredentials;
    }

    public LoginPage logout() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(logoutButton));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()", logoutButton);

        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.titleIs("Login"));

        return new LoginPage(driver);
    }
}
