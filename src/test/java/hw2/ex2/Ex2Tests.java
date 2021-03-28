package hw2.ex2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ex2Tests {
    WebDriver driver;

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Пиглин/IdeaProjects/spbstu/TestinLab2/src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.navigate().to("https://jdi-testing.github.io/jdi-light/index.html");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private void LogIn() {
        driver.get("https://jdi-testing.github.io/jdi-light/index.html");
        WebElement toggle = driver.findElement(By.xpath("/html/body/header/div/nav/ul[2]"));
        toggle.click();
        WebElement username = driver.findElement(By.id("name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement enter = driver.findElement(By.id("login-button"));
        username.sendKeys("Roman");
        password.sendKeys("Jdi1234");
        enter.click();
    }

    private void OpenDifferentElementsPage() {
        if (!driver.findElement(By.xpath("/html/body/header/div/nav/ul[1]/li[3]/ul/li[8]")).isDisplayed()) {
            WebElement service = driver.findElement(By.className("dropdown"));
            service.click();
        }
        WebElement toggle = driver.findElement(By.xpath("/html/body/header/div/nav/ul[1]/li[3]/ul/li[8]"));
        toggle.click();
    }

    //    1)Open test site by URL
    @Test
    public void openSiteURLTest() {
        SoftAssert softAssert = new SoftAssert();
        driver.get("https://jdi-testing.github.io/jdi-light/index.html");
        String currentUrl = driver.getCurrentUrl();
        softAssert.assertEquals(currentUrl, "https://jdi-testing.github.io/jdi-light/index.html", "wrong url in test1");
    }

    //    2)Assert Browser title
    @Test(dependsOnMethods = "openSiteURLTest")
    public void HomePageTest() {
        SoftAssert softAssert = new SoftAssert();
        String currentUrl = driver.getTitle();
        softAssert.assertEquals(currentUrl, "Home Page", "wrong title in test2");
    }

    //    3)Perform login
    @Test(dependsOnMethods = "HomePageTest")
    public void LoginTest() {
        SoftAssert softAssert = new SoftAssert();
        LogIn();
        //    4)Assert User name in the left-top side of screen that user is loggined
        WebElement gotUsername = driver.findElement(By.xpath("/html/body/header/div/nav/ul[2]/li/a/div/span"));
        String expectedUsername = "ROMAN IOVLEV";
        softAssert.assertEquals(gotUsername.getText(), expectedUsername, "loggined as wrong person");
    }


    //    5)Click on "Service" subcategory in the header and check that drop down contains options
    @Test(dependsOnMethods = "LoginTest")
    public void CheckServiceTest() {
        SoftAssert softAssert = new SoftAssert();
        WebElement service = driver.findElement(By.className("dropdown"));
        service.click();
        WebElement menu = driver.findElement(By.className("dropdown-menu"));
        ArrayList<String> serviceMenu = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String text = String.format("/html/body/header/div/nav/ul[1]/li[3]/ul/li[%d]", i + 1);
            WebElement support = menu.findElement(By.xpath(text));
            serviceMenu.add(support.getText());
        }
        List<String> serviceMenuExpected = Arrays.asList("SUPPORT", "DATES", "COMPLEX TABLE", "SIMPLE TABLE", "TABLE WITH PAGES", "DIFFERENT ELEMENTS");
        softAssert.assertTrue(serviceMenu.contains("SUPPORT"), "not contains support");
        softAssert.assertTrue(serviceMenu.contains("DATES"), "not contains dates");
        softAssert.assertTrue(serviceMenu.contains("COMPLEX TABLE"), "not contains complex table");
        softAssert.assertTrue(serviceMenu.contains("SIMPLE TABLE"), "not contains simple table");
        softAssert.assertTrue(serviceMenu.contains("TABLE WITH PAGES"), "not contains table with pages");
        softAssert.assertTrue(serviceMenu.contains("DIFFERENT ELEMENTS"), "not contains different elements");
        softAssert.assertAll();
    }

    //    6)Click on Service subcategory in the left section and check that drop down contains options
    @Test(dependsOnMethods = "CheckServiceTest")
    public void CheckLeftServiceTest() {
        SoftAssert softAssert = new SoftAssert();
        WebElement service = driver.findElement(By.className("menu-title"));
        service.click();
        WebElement menu = driver.findElement(By.className("sub"));
        ArrayList<String> serviceMenu = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String text = String.format("//*[@id=\"mCSB_1_container\"]/ul/li[3]/ul/li[%d]", i + 1);
            WebElement support = menu.findElement(By.xpath(text));
            serviceMenu.add(support.getText());
        }
        List<String> serviceMenuExpected = Arrays.asList("Support", "Dates", "Complex Table", "Simple Table", "Table with pages", "Different elements");
        softAssert.assertTrue(serviceMenu.contains("Support"), "not contains support");
        softAssert.assertTrue(serviceMenu.contains("Dates"), "not contains dates");
        softAssert.assertTrue(serviceMenu.contains("Complex Table"), "not contains complex table");
        softAssert.assertTrue(serviceMenu.contains("Simple Table"), "not contains simple table");
        softAssert.assertTrue(serviceMenu.contains("Table with pages"), "not contains table with pages");
        softAssert.assertTrue(serviceMenu.contains("Different elements"), "not contains different elements");
        softAssert.assertAll();
    }

    //    7)Open through the header menu Service -> Different Elements Page
    @Test(dependsOnMethods = "CheckLeftServiceTest")
    public void DifferentElementsPageTest() {
        SoftAssert softAssert = new SoftAssert();
        OpenDifferentElementsPage();
        softAssert.assertEquals(driver.getCurrentUrl(), "https://jdi-testing.github.io/jdi-light/different-elements.html");
    }

    //    8)Check interface on Different elements page, it contains all needed elements
    @Test(dependsOnMethods = "DifferentElementsPageTest")
    public void CheckingInterfaceTest() {
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> checkboxList = driver.findElements(By.xpath("//*[contains(@type,'checkbox')]"));
        List<WebElement> radioList = driver.findElements(By.xpath("//*[contains(@type,'radio')]"));
        List<WebElement> dropdownList = driver.findElements(By.tagName("select"));
        List<WebElement> buttonsList = driver.findElements(By.xpath("//*[@class='uui-button']"));
        softAssert.assertEquals(4, checkboxList.size());
        softAssert.assertEquals(4, radioList.size());
        softAssert.assertEquals(1, dropdownList.size());
        softAssert.assertEquals(2, buttonsList.size());
        //    9)Assert that there is Right Section
        softAssert.assertTrue(driver.findElement(By.xpath("//*[@name='log-sidebar']")).isDisplayed(), "there is no right section");
        //    10)Assert that there is Left Section
        softAssert.assertTrue(driver.findElement(By.id("mCSB_1")).isDisplayed(), "there is no left section");
        softAssert.assertAll();
    }

    //    11)Select checkboxes
    @Test(dependsOnMethods = "CheckingInterfaceTest")
    public void SelectingCheckBoxTest() {
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> checkboxList = driver.findElements(By.xpath("//*[contains(@type,'checkbox')]"));
        WebElement water = checkboxList.get(0);
        WebElement wind = checkboxList.get(2);
        ;
        water.click();
        wind.click();
        softAssert.assertTrue(water.isSelected(), "water isn`t selected");
        softAssert.assertTrue(wind.isSelected(), "wind isn`t selected");
        //    12)Assert that for each checkbox there is an individual log row and value is corresponded to the status of checkbox.
        List<WebElement> logs = driver.findElements(By.xpath("//*[@class='panel-body-list logs']/li"));
        softAssert.assertEquals(logs.size(), 2);
        softAssert.assertTrue(logs.get(0).isDisplayed(), "waterlog isn`t displayed");
        softAssert.assertTrue(logs.get(1).isDisplayed(), "windlog isn`t displayed");
        String waterlog = logs.get(1).getText();
        String windlog = logs.get(0).getText();
        softAssert.assertTrue(waterlog.contains("Water") && (waterlog.contains("true")), "waterlog dont contains water or true state");
        softAssert.assertTrue(windlog.contains("Wind") && (windlog.contains("true")), "windlog dont contains wind or true state");
        softAssert.assertAll();
    }


    //    13)Select radio
    @Test(dependsOnMethods = "SelectingCheckBoxTest")
    public void SelectingRadioTest() {
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> radioList = driver.findElements(By.xpath("//*[contains(@type,'radio')]"));
        WebElement selen = radioList.get(3);
        selen.click();
        softAssert.assertTrue(selen.isSelected(), "selen isn`t selected");
        //    14)Assert that for radiobutton there is a log row and value is corresponded to the status of radiobutton.
        List<WebElement> logs = driver.findElements(By.xpath("//*[@class='panel-body-list logs']/li"));
        for (int i = 0; i < logs.size(); i++) {
            String s = String.format("log %d isn`t displayed", i);
            softAssert.assertTrue(logs.get(i).isDisplayed(), s);
        }
        String selenlog = logs.get(0).getText();
        softAssert.assertTrue(selenlog.contains("Selen") && (selenlog.contains("metal")), "selenlog dont contains selen or metal");
        softAssert.assertAll();
    }

    //    15)Select in dropdown
    @Test(dependsOnMethods = "SelectingRadioTest")
    public void SelectingInDropdownTest() {
        SoftAssert softAssert = new SoftAssert();
        WebElement dropdown = driver.findElement(By.tagName("select"));
        dropdown.click();
        WebElement yellow = driver.findElement(By.xpath("/html/body/div/div[2]/main/div[2]/div/div[4]/select/option[4]"));
        yellow.click();
        softAssert.assertTrue(yellow.isSelected(), "yellow isn`t selected");
        //    16)Assert that for dropdown there is a log row and value is corresponded to the selected value.
        List<WebElement> logs = driver.findElements(By.xpath("//*[@class='panel-body-list logs']/li"));
        for (int i = 0; i < logs.size(); i++) {
            softAssert.assertTrue(logs.get(i).isDisplayed(), "");
        }
        String yellowlog = logs.get(0).getText();
        softAssert.assertTrue(yellowlog.contains("Colors") && (yellowlog.contains("Yellow")), "");
        softAssert.assertAll();
    }

    //    17)Unselect and assert checkboxes
    @Test(dependsOnMethods = "SelectingInDropdownTest")
    public void UnselectingCheckBoxTest() {
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> checkboxList = driver.findElements(By.xpath("//*[contains(@type,'checkbox')]"));
        WebElement water = checkboxList.get(0);
        WebElement wind = checkboxList.get(2);
        water.click();
        wind.click();
        softAssert.assertFalse(water.isSelected(), "water is selected");
        softAssert.assertFalse(wind.isSelected(), "wind is selected");
        //    18)Assert that for each checkbox there is an individual log row and value is corresponded to the status of checkbox.
        List<WebElement> logs = driver.findElements(By.xpath("//*[@class='panel-body-list logs']/li"));
        for (int i = 0; i < logs.size(); i++) {
            String s = String.format("log %d isn`t displayed", i);
            softAssert.assertTrue(logs.get(i).isDisplayed(), s);
        }
        String windlog = logs.get(0).getText();
        String waterlog = logs.get(1).getText();
        softAssert.assertTrue(waterlog.contains("Water") && (waterlog.contains("false")), "wrong waterlog or its state in true");
        softAssert.assertTrue(windlog.contains("Wind") && (windlog.contains("false")), "wrong windlog or its state in true");
        softAssert.assertAll();
    }

    @AfterTest
    public void endWork() {
        driver.close();
    }
}
