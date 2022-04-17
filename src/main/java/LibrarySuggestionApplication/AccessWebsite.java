package LibrarySuggestionApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Objects;


public class AccessWebsite {

    private final static String PROPERTY_WEB_DRIVER = "webdriver.chrome.driver";
    private final static String SYSTEM_WEB_DRIVER = "user.dir";
    private final static String NAME_CHROME_DRIVER = "\\chromedriver.exe";
    private final static String INDIGO_URL = "https://www.chapters.indigo.ca/en-ca/";
    private final static String INDIGO_SEARCH = "header__quick-search";
    private final static String INDIGO_ZERO_RESULTS = "0 results for ";
    private final static String UNAVAILABLE = "unavailable";
    private final static String INDIGO_PRODUCT_LIST = "product-list__product-title";
    private final static String INDIGO_DATA_KEY = "data-a8n";
    private final static String INDIGO_RELEASE_DATE = "detail-product-specs__value--ReleaseDate";
    private final static String INDIGO_FORMAT = "detail-product-specs__value--Format";
    private final static String INDIGO_NORMAL_PRICE = "price-normal";
    private final static String INDIGO_ADJUSTED_PRICE = "price-adjusted";
    private static WebDriver driver;
    private static Document doc;
    private static WebElement webElement;
    private static String publicationDate;
    private static String format;
    private static String price;
    private static String adjustedPrice;
    private static String normalPrice;

    public AccessWebsite() {
    }

    public static void launchWebsite(final String isbn) throws IOException {
        System.setProperty(PROPERTY_WEB_DRIVER, System.getProperty(SYSTEM_WEB_DRIVER).concat(NAME_CHROME_DRIVER));
        driver = new ChromeDriver();
        driver.get(INDIGO_URL);
        searchWithIsbn(isbn);
    }

    public static void searchWithIsbn(final String isbn) throws IOException {
        webElement = driver.findElement(By.id(INDIGO_SEARCH));
        webElement.sendKeys(isbn);
        webElement.submit();
        boolean results = driver.getPageSource().contains(INDIGO_ZERO_RESULTS);
        if (results) {
            CsvWriter.printSuggestion(UNAVAILABLE, UNAVAILABLE, UNAVAILABLE);
        } else {
            driver.findElement(By.className(INDIGO_PRODUCT_LIST)).click();
            parseWebpage();
        }
    }

    public static void parseWebpage() throws IOException {
        String webPage = driver.getPageSource();
        doc = Jsoup.parse(webPage);
        getPublicationDateFromWebPage();
        getFormatFromWebPage();
        getPriceFromWebPage();
        CsvWriter.printSuggestion(price, publicationDate, format);
    }

    public static void getPublicationDateFromWebPage() {
        publicationDate = doc.getElementsByAttributeValueContaining(INDIGO_DATA_KEY, INDIGO_RELEASE_DATE)
                .get(0).text();
    }

    public static void getFormatFromWebPage() {
        if (doc.getElementsByAttributeValueContaining(INDIGO_DATA_KEY, INDIGO_FORMAT).get(0).text().length() > 0) {
            format = doc.getElementsByAttributeValueContaining(INDIGO_DATA_KEY, INDIGO_FORMAT).get(0).text();
        } else {
            format = UNAVAILABLE;
        }
    }

    public static void getPriceFromWebPage() {
        normalPrice = String.valueOf(doc.getElementsByAttributeValueContaining(INDIGO_DATA_KEY, INDIGO_NORMAL_PRICE));
        adjustedPrice = String.valueOf(doc.getElementsByAttributeValueContaining(INDIGO_DATA_KEY, INDIGO_ADJUSTED_PRICE));
        if (Objects.nonNull(normalPrice) && !normalPrice.isEmpty()) {
            price = doc.getElementsByAttributeValueContaining(INDIGO_DATA_KEY, INDIGO_NORMAL_PRICE).get(0).text();
        } else if (Objects.nonNull(adjustedPrice) && !adjustedPrice.isEmpty()) {
            price = doc.getElementsByAttributeValueContaining(INDIGO_DATA_KEY, INDIGO_ADJUSTED_PRICE).get(0).text();
        } else {
            price = UNAVAILABLE;
        }
    }

}




