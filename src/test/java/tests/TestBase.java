package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.sleep;

public class TestBase {

    @BeforeAll
    static void setUp() {
        String browser = System.getProperty("browser", "chrome");
        String version = System.getProperty("version", "91");
        String size = System.getProperty("size", "1920x1080");
        //String remoteUrl = System.getProperty("remoteUrl", "selenoid.autotests.cloud/wd/hub");
        String login = System.getProperty("login", "user1");
        String pass = System.getProperty("pass", "1234");

        Configuration.baseUrl = "https://demoqa.com";

        //String url = "https://" + login + ":" + pass + "@" + remoteUrl;
        //Configuration.remote = url;
        Configuration.browser = browser;
        Configuration.browserVersion = version;
        Configuration.browserSize = size;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;

        SelenideLogger.addListener("allure", new AllureSelenide());

        Attach.attachAsText("Browser: ", browser);
        Attach.attachAsText("Version: ", version);
        //Attach.attachAsText("Remote Url: ", remoteUrl);

    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        sleep(5000);
        closeWebDriver();
    }

}
