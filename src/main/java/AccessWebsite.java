import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.List;

public class AccessWebsite {

    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir").concat("\\chromedriver.exe"));
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.chapters.indigo.ca/en-ca/");
        WebElement webElement = driver.findElement(By.id("header__quick-search"));
        webElement.sendKeys("9780385697378");
        webElement.submit();
        driver.findElement(By.className("product-list__product-title")).click();
        String url = driver.getCurrentUrl();

        String laPage = driver.getPageSource();

        Document doc = Jsoup.parse(laPage);
  Elements element2 = doc.getElementsByClass("detail-product-specs__value");


//        System.out.println(element2);
//element2.
    }


////        final Document document = Jsoup.connect(hrefValue).get();
//        document.outerHtml();

//        driver.findElement(By.className("product-list__product-title")).click();
//        String url = driver.getCurrentUrl();
}


