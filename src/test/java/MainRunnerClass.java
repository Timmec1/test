import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.impl.WebDriverContainer;
import com.codeborne.selenide.impl.WebDriverThreadLocalContainer;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;

import static com.codeborne.selenide.WebDriverRunner.webdriverContainer;

/**
 * Created by timmec1 on 7/07/2017.
 */
public class MainRunnerClass extends Selenide {
    private static String urlTemp = "https://idp.iamfas.qa.belgium.be/fasui/registration/weakprofile";

    String username = new RandomStringClass().getUsername();
    String emailAddress = new RandomStringClass().getEmailAddress();
    String firstname = new RandomStringClass().getFirstname();
    String password = new RandomStringClass().getPassword();
    String lastname = new RandomStringClass().getLastname();
    private static String browserMain = "";

    private static String upperLanguage = "EN".toUpperCase();
    //test

    //NL, FR, DE, EN
    public void loginProcess() {
        clickCookieButton();
        switchLanguage();
        $(By.id("emailAddress")).sendKeys(emailAddress);
        $(By.id("firstname")).sendKeys(firstname);
        $(By.id("lastname")).sendKeys(lastname);
        $(By.id("password")).sendKeys(password);
        $(By.id("username")).sendKeys(username);
        $(By.id("eulaApproval")).click();
        $(By.id("register-weak-profile")).click();
    }

    public void loginProcess(String browser) {
        JavascriptExecutor js = (JavascriptExecutor) getWebdriver();
        if (browser.equalsIgnoreCase("edge")) {
            js.executeScript("arguments[0].click();", $("a[onclick = \"changeLanguage(this, \'"+ upperLanguage +"\', 'null');\"]"));
        } else if (browser.equalsIgnoreCase("ie")){
            clickCookieButton();
            switchLanguage();
        }
        $(By.id("username")).sendKeys(username);
        $(By.id("emailAddress")).sendKeys(emailAddress);
        $(By.id("firstname")).sendKeys(firstname);
        $(By.id("password")).sendKeys(password);
        $(By.id("lastname")).sendKeys(lastname);

        if (browser.equalsIgnoreCase("edge")) {
            js.executeScript("arguments[0].click();", $(By.id("eulaApproval")));

        //$(By.id("register-weak-profile")).click();
        JavascriptExecutor jsa = (JavascriptExecutor) getWebdriver();
        jsa.executeScript("arguments[0].click();", $(By.id("register-weak-profile")));
        } else if (browser.equalsIgnoreCase("ie")){
            $(By.id("eulaApproval")).click();
            $(By.id("register-weak-profile")).click();
        }
     }

    public void clickCookieButton(){
       if ($(By.id("cookie-button")).isDisplayed()){
           $(By.id("cookie-button")).click();
       }
       //if (browserMain.equalsIgnoreCase("firefox")){
         //   $(By.id("cookie-button")).click();
        //}
    }

    public void switchLanguage(){
        $("a[onclick = \"changeLanguage(this, \'" + upperLanguage + "\', 'null');\"]").click();
    }


    @Test
    public void loginChrome() {
        browserMain = "chrome".toUpperCase();
        System.out.println("username: " + username + "\nemailAddress: " + emailAddress + "\npassword: " + password);
        Configuration.browser = "chrome";
        open(urlTemp);
        loginProcess();
        System.out.println();
    }

    @Test
    public void loginFirefox() throws InterruptedException {
        Thread.sleep(5000);
        browserMain = "firefox".toUpperCase();
        System.out.println("username: " + username + "\nemailAddress: " + emailAddress + "\npassword: " + password + "\nlastname: " + lastname);
        Configuration.browser = "gecko";
        open(urlTemp);
        loginProcess();
    }

    @Test
    public void loginEdge() {
        browserMain = "edge".toUpperCase();
        System.out.println("username: " + username + "\nemailAddress: " + emailAddress + "\nfirstname: " + firstname + "\npassword: " + password + "\nlastname: " + lastname);
        Configuration.browser = "edge";
        open(urlTemp);
        loginProcess(browserMain.toLowerCase());
        System.out.println();
    }

    @Test
    public void loginIe() throws InterruptedException {
        browserMain = "ie".toUpperCase();
        System.out.println("username: " + username + "\nemailAddress: " + emailAddress + "\npassword: " + password);
        com.codeborne.selenide.Configuration.browser = browserMain.toLowerCase();
        open(urlTemp);
        loginProcess("ie");

    }

    public class RandomStringClass {
        private String username;
        private String emailAddress;
        private String firstname;
        private String password;
        private String lastname;

        public RandomStringClass() {
            setUsername(RandomStringUtils.randomAlphabetic(10));
            setEmailAddress(RandomStringUtils.randomAlphabetic(10) + "@mailinator.com");
            setFirstname(RandomStringUtils.randomAlphabetic(6));
            setPassword(RandomStringUtils.randomAlphabetic(10) + "1");
            setLastname(RandomStringUtils.randomAlphabetic(0) + "TEST");
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    }

    @After
    public void afterTest() {
        close();
        try {
            Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static WebDriver getWebdriver(){
       return webdriverContainer.getWebDriver();
    }

}
