package LibrarySuggestionApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;
import java.util.Objects;


public class AccessWebsite {
    static WebDriver driver;
    static Document doc;
    static WebElement webElement;
    static String publicationDate;
    static String format;
    static String price;
    static String adjustedPrice;
    static String normalPrice;

    public AccessWebsite() {
    }

    public static void launchWebsite(String isbn) throws IOException {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir").concat("\\chromedriver.exe"));
        driver = new ChromeDriver();
        driver.get("https://www.chapters.indigo.ca/en-ca/");
        searchWithIsbn(isbn);
    }

    public static void searchWithIsbn(String isbn) throws IOException {
        webElement = driver.findElement(By.id("header__quick-search"));
        webElement.sendKeys(isbn);
        webElement.submit();
        boolean results = driver.getPageSource().contains("0 results for ");
        if (results) {
            price = "unavailable";
            format = "unavailable";
            publicationDate = "unavailable";
            InputSuggestion.printSuggestion(price, publicationDate, format);
        } else {
            driver.findElement(By.className("product-list__product-title")).click();
            String url = driver.getCurrentUrl();
            parseWebpage();
        }
    }


    public static void parseWebpage() throws IOException {
        String webPage = driver.getPageSource();
        doc = Jsoup.parse(webPage);
        getPublicationDateFromWebPage();

    }

    public static void getPublicationDateFromWebPage() throws IOException {
       publicationDate = doc.getElementsByAttributeValueContaining("data-a8n", "detail-product-specs__value--ReleaseDate")
                .get(0).text();
        getFormatFromWebPage();
    }

    public static void getFormatFromWebPage() throws IOException {
        if (doc.getElementsByAttributeValueContaining("data-a8n", "detail-product-specs__value--Format").get(0).text().length() > 0) {
            format = doc.getElementsByAttributeValueContaining("data-a8n", "detail-product-specs__value--Format").get(0).text();
            getPriceFromWebPage();
        } else {
            format = "unavailable";
            getPriceFromWebPage();
        }
    }

    public static void getPriceFromWebPage() throws IOException {
        normalPrice = String.valueOf(doc.getElementsByAttributeValueContaining("data-a8n", "price-normal"));
        adjustedPrice = String.valueOf(doc.getElementsByAttributeValueContaining("data-a8n", "price-adjusted"));
        if (Objects.nonNull(normalPrice) && !normalPrice.isEmpty()) {
            price = price = doc.getElementsByAttributeValueContaining("data-a8n", "price-normal").get(0).text();
        }
        else if (Objects.nonNull(adjustedPrice) && !adjustedPrice.isEmpty()) {
            price = price = doc.getElementsByAttributeValueContaining("data-a8n", "price-adjusted").get(0).text();
        }
        else{
            price = "unavailable";
        }
        InputSuggestion.printSuggestion(price, publicationDate, format);
    }

}




