package example;

import helper.TestHelper;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleTest {

    Properties properties;

    WebDriver driver = null;

    @BeforeClass
    @SneakyThrows
    public void beforeClass() {
        properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(Path.of("config.properties"))) {
            properties.load(reader);
            String driverName = properties.getProperty("driver");
            File file = new File(properties.getProperty("path"));
            System.setProperty(driverName, file.getAbsolutePath());
            if (driverName.contains("chrome")) {
                driver = new ChromeDriver();
            } else if (driverName.contains("edge")) {
                driver = new EdgeDriver();
            }
            assertThat(driver).isNotNull();
        }
    }

    @AfterClass
    public void afterClass() {
        //driver.quit();
    }


    @Test
    @SneakyThrows
    public void testLinks() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        //driver.navigate().to(TestHelper.siteURL() + "link.html");
        visit("link.html");
        WebElement link = driver.findElement(By.linkText("Recommend Selenium"));
        assertThat(link).isNotNull();

        link.click();

        WebElement h2 = driver.findElement(By.tagName("h2"));
        assertThat(h2).isNotNull();
        assertThat(h2.getText()).isEqualTo("Test Site for Selenium Recipes");

        driver.navigate().back();

        //driver.findElement(By.id("recommend_selenium_link")).click();
        driver.findElement(By.xpath("//*[@id=\"recommend_selenium_link\"]")).click();
        // //*[@id="recommend_selenium_link"]
        // //p/a[text()='Recommend Selenium']

        driver.navigate().back();


        // //*[@id="container"]/div[3]/div[1]/a
        // //*[@id="second_div"]/a
        // //div[contains(text(), \"Second\")]/a[text()=\"Click here\"]
        driver.findElement(By.xpath("//div[contains(text(), \"First\")]/a[text()=\"Click here\"]")).click();

    }

    @Test
    @SneakyThrows
    public void testButtons() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        visit("button.html");
        driver.findElement(By.xpath("//button[contains(text(),'Choose Selenium')]")).click();

        driver.findElement(By.id("choose_selenium_btn")).click();
        // //*[@id="container"]/div[1]/form/input[3]
        // //input[@value='Space After ']

        //driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/form/input"));
        driver.findElement(By.xpath("//input[contains(@src, 'button_go.jpg')]")).click();
    }

    private void visit(String path) {
        driver.navigate().to(TestHelper.siteURL() + path);
    }
}


