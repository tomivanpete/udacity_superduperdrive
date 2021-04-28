package com.udacity.jwdnd.course1.cloudstorage;

import com.github.javafaker.Faker;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private Faker faker;
	private String testUserFirstname;
	private String testUserLastname;
	private String testUserUsername;
	private String testUserPassword;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	private void createTestData() {
		faker = new Faker();

		testUserFirstname = faker.name().firstName();
		testUserLastname = faker.name().lastName();
		testUserUsername = (testUserFirstname.charAt(0) + testUserLastname).toLowerCase();
		testUserPassword = faker.internet().password();
	}

	@Test
	public void testLoginRedirect() {
		driver.get("http://localhost:" + this.port + "/");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/doesNotExist");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testInvalidLogin() {
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);

		loginPage.login("notAValidUser", "notAValidPassword");
		Assertions.assertTrue(loginPage.isErrorMsgDisplayed());
	}

	@Test
	public void testSignupSuccess() {
		createTestData();

		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);

		signupPage.createUser(testUserFirstname, testUserLastname, testUserUsername, testUserPassword);
		Assertions.assertTrue(signupPage.isSuccessMsgDisplayed());
		signupPage.clickLoginLink();
	}

	@Test
	public void testSignupError() {
		createTestData();

		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);

		signupPage.createUser(testUserFirstname, testUserLastname, testUserUsername, testUserPassword);
		driver.navigate().refresh();
		String result = signupPage.createUser(testUserFirstname, testUserLastname, testUserUsername, testUserPassword);
		Assertions.assertEquals("error-msg", result);
	}

	@Test
	public void testSignupLoginLogout() {
		//HomePage homePage = signupUserAndLogin();
		testSignupSuccess();
		driver.navigate().refresh();
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser(testUserUsername, testUserPassword);

		Assertions.assertEquals("Home", driver.getTitle());

		loginPage = homePage.logout();
		Assertions.assertEquals("logout-msg", loginPage.getAlertMessageId());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNoteCreate() {
		testSignupLoginLogout();
		driver.navigate().refresh();
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser(testUserUsername, testUserPassword);

		String noteTitle = "My new note title";
		String noteDescription = "My new note description";
		ResultPage resultPage = homePage.createNewNote(noteTitle, noteDescription);
		Assertions.assertTrue(resultPage.getMessageText().startsWith("Success"));

		homePage = resultPage.clickHomeLink();
		List<Note> homePageNotes = homePage.getNotes();
		Assertions.assertEquals(1, homePageNotes.size());
		Assertions.assertEquals(noteTitle, homePageNotes.get(0).getNoteTitle());
		Assertions.assertEquals(noteDescription, homePageNotes.get(0).getNoteDescription());
		homePage.logout();
	}

	@Test
	public void testNoteEdit() {
		testNoteCreate();
		driver.navigate().refresh();
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser(testUserUsername, testUserPassword);

		String noteTitle = "Updated note title";
		String noteDescription = "Updated note description";
		ResultPage resultPage = homePage.editNote(0, noteTitle, noteDescription);
		Assertions.assertTrue(resultPage.getMessageText().startsWith("Success"));

		homePage = resultPage.clickHomeLink();
		List<Note> homePageNotes = homePage.getNotes();
		Assertions.assertEquals(1, homePageNotes.size());
		Assertions.assertEquals(noteTitle, homePageNotes.get(0).getNoteTitle());
		Assertions.assertEquals(noteDescription, homePageNotes.get(0).getNoteDescription());
		homePage.logout();
	}

	@Test
	public void testNoteDelete() {
		testNoteCreate();
		driver.navigate().refresh();
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser(testUserUsername, testUserPassword);

		ResultPage resultPage = homePage.deleteNote(0);
		Assertions.assertTrue(resultPage.getMessageText().startsWith("Success"));

		homePage = resultPage.clickHomeLink();
		List<Note> homePageNotes = homePage.getNotes();
		Assertions.assertEquals(0, homePageNotes.size());
		homePage.logout();
	}

	@Test
	public void testCredentialCreate() {
		testSignupLoginLogout();
		driver.navigate().refresh();
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser(testUserUsername, testUserPassword);

		String url = "https://seleniumunit.tests";
		String username = "MyUser";
		String password = "MyPassword";
		ResultPage resultPage = homePage.createNewCredential(url, username, password);
		Assertions.assertTrue(resultPage.getMessageText().startsWith("Success"));

		homePage = resultPage.clickHomeLink();
		List<Credential> homePageCredentials = homePage.getCredentials();
		Assertions.assertEquals(1, homePageCredentials.size());
		Assertions.assertEquals(url, homePageCredentials.get(0).getUrl());
		Assertions.assertEquals(username, homePageCredentials.get(0).getUsername());
		homePage.logout();
	}

	@Test
	public void testCredentialEdit() {
		testCredentialCreate();
		driver.navigate().refresh();
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser(testUserUsername, testUserPassword);

		String url = "https://seleniumunit.tests/updated";
		String username = "MyUpdatedUser";
		String password = "MyUpdatedPassword";
		ResultPage resultPage = homePage.editCredential(0, url, username, password);
		Assertions.assertTrue(resultPage.getMessageText().startsWith("Success"));

		homePage = resultPage.clickHomeLink();
		List<Credential> homePageCredentials = homePage.getCredentials();
		Assertions.assertEquals(1, homePageCredentials.size());
		Assertions.assertEquals(url, homePageCredentials.get(0).getUrl());
		Assertions.assertEquals(username, homePageCredentials.get(0).getUsername());
		homePage.logout();
	}

	@Test
	public void testCredentialDelete() {
		testCredentialCreate();
		driver.navigate().refresh();
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser(testUserUsername, testUserPassword);

		ResultPage resultPage = homePage.deleteCredential(0);
		Assertions.assertTrue(resultPage.getMessageText().startsWith("Success"));

		homePage = resultPage.clickHomeLink();
		List<Credential> homePageCredentials = homePage.getCredentials();
		Assertions.assertEquals(0, homePageCredentials.size());
		homePage.logout();
	}

}
