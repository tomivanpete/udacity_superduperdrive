package com.udacity.jwdnd.course1.cloudstorage;

import com.github.javafaker.Faker;
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

	private HomePage signupUserAndLogin() {
		createTestData();

		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);

		signupPage.createUser(testUserFirstname, testUserLastname, testUserUsername, testUserPassword);
		LoginPage loginPage = signupPage.clickLoginLink();

		return loginPage.loginValidUser(testUserUsername, testUserPassword);
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
		HomePage homePage = signupUserAndLogin();
		Assertions.assertEquals("Home", driver.getTitle());

		LoginPage loginPage = homePage.logout();
		Assertions.assertEquals("logout-msg", loginPage.getAlertMessageId());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNoteCreate() {
		HomePage homePage = signupUserAndLogin();
		Assertions.assertEquals("Home", driver.getTitle());

		String newNoteTitle = "noteTitle";
		String newNoteDescription = "noteDescription";
		ResultPage resultPage = homePage.createNewNote(newNoteTitle, newNoteDescription);
		Assertions.assertTrue(resultPage.getMessageText().startsWith("Success"));

		homePage = resultPage.clickHomeLink();
		List<Note> homePageNotes = homePage.getNotes();
		Assertions.assertEquals(1, homePageNotes.size());
		Assertions.assertEquals(newNoteTitle, homePageNotes.get(0).getNoteTitle());
		Assertions.assertEquals(newNoteDescription, homePageNotes.get(0).getNoteDescription());
	}

}
