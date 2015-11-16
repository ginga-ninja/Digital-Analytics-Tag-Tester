

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;


public class PetshopOrder {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private JavascriptExecutor js;

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://petshop.scl.com/mspetshop/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    js = (JavascriptExecutor) driver;    //need to add this for DA tag testing
  }

  @Test
  public void testPetshopAddtocart() throws Exception {
    driver.get(baseUrl);
    Thread.sleep(2000);     //add this for DA Tag Testing
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);     //add this for DA Tag Testing
    driver.findElement(By.linkText("Fish")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    driver.findElement(By.linkText("Koi")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    driver.findElement(By.linkText("Spotted")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    driver.findElement(By.cssSelector("img[alt=\"Add to Cart\"]")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    //try {
     // assertEquals("Shopping cart", driver.getTitle());
    //} catch (Error e) {
   //   verificationErrors.append(e.toString());
    //}
    driver.findElement(By.cssSelector("img[alt=\"Proceed to Checkout\"]")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    driver.findElement(By.cssSelector("img[alt=\"Continue\"]")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    driver.findElement(By.id("btnSubmit")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    driver.findElement(By.id("btnContinue")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    driver.findElement(By.cssSelector("img[alt=\"Continue\"]")).click();
    Thread.sleep(2000);
    DATester.checkForLib(driver);
    DATester.getTags(driver, js);
    
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
