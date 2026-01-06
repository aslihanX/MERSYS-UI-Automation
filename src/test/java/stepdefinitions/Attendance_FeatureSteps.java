package stepdefinitions;

import io.cucumber.java.en.*;
import org.apache.logging.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.*;
import utils.BaseDriver;
import java.io.File;
import java.time.Duration;

public class Attendance_FeatureSteps {

    private final WebDriver driver = BaseDriver.getDriver();
    private final AttendancePage ap = new AttendancePage(driver);
    private final HamburgerMenuPage hp = new HamburgerMenuPage(driver);
    private static final Logger LOGGER = LogManager.getLogger(Attendance_FeatureSteps.class);

    @When("the user navigates to the {string} section from the side menu")
    public void navigatesToAttendance(String menu) {
        LOGGER.info("STEP: Navigating to the {} section.", menu);
        ap.clickElement(ap.attendanceMenuItem);
    }

    @Then("the {string} menu should expand successfully")
    public void menuExpands(String menu) {
        LOGGER.info("STEP: Verifying that the {} menu has expanded.", menu);
        boolean isDisplayed = ap.isDisplayed(ap.attendanceExcusesBtn);
        Assert.assertTrue(isDisplayed, "ERROR: Attendance menu failed to expand or sub-menu is not visible!");
    }

    @When("the user clicks on the {string} button to access the attendance page")
    public void clicksExcuses(String btn) {
        LOGGER.info("STEP: Clicking the {} button to open the excuses page.", btn);
        ap.clickElement(ap.attendanceExcusesBtn);
    }

    @And("the user clicks {string} icon to create a new excuse")
    public void clicksPlus(String icon) {
        LOGGER.info("STEP: Clicking the '{}' icon to initiate a new excuse record.", icon);
        ap.clickElement(ap.plusIcon);
    }

    @And("the user selects excuse type")
    public void selectsType() {
        LOGGER.info("STEP: Selecting excuse type (Full Day).");
        ap.clickElement(ap.excuseTypeDropdown);
        ap.clickElement(ap.fullDayOption);
    }

    @And("the user selects date on the calendar")
    public void selectsDate() {
        LOGGER.info("STEP: Opening calendar and selecting today's date.");
        ap.clickElement(ap.calendarInput);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        ap.clickElement(ap.todayDate);
    }

    @And("the user enters {string} as excuse description")
    public void entersDesc(String desc) {
        LOGGER.info("STEP: Entering excuse description: {}", desc);
        ap.sendKeysToElement(ap.descriptionField, desc);
    }

    @And("the user attaches a file from path {string} if provided")
    public void attachesFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            LOGGER.info("STEP: Attempting to attach file from path: {}", filePath);
            try {
                WebElement hiddenFileInput = driver.findElement(By.cssSelector("input[type='file']"));
                hiddenFileInput.sendKeys(new File(filePath).getAbsolutePath());
                LOGGER.info("SUCCESS: File uploaded using Selenium sendKeys (Headless-friendly).");
            } catch (Exception e) {
                LOGGER.warn("DEBUG: Standard sendKeys failed, attempting file upload with Robot class...");
                ap.clickElement(ap.attachmentBtn);
                ap.uploadFileWithRobot(new File(filePath).getAbsolutePath());
                LOGGER.info("SUCCESS: File uploaded using Robot class.");
            }
        }
    }

    @And("the user clicks the {string} button")
    public void clicksSend(String btn) {
        LOGGER.info("STEP: Clicking the '{}' button.", btn);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(ap.sendBtn));
            ap.clickElement(ap.sendBtn);
            LOGGER.info("INFO: Send button clicked successfully.");
        } catch (Exception e) {
            LOGGER.warn("DEBUG: Normal click failed, attempting click with JavaScript Executor...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ap.sendBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ap.sendBtn);
            LOGGER.info("INFO: Send button clicked via JavaScript.");
        }
    }

    @Then("the {string} message must be displayed")
    public void verifyMessage(String msg) {
        LOGGER.info("STEP: Verifying the visibility of the {} message.", msg);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            WebElement successElem = wait.until(ExpectedConditions.visibilityOf(hp.successMessage));
            String actualText = successElem.getText();
            LOGGER.info("INFO: Received toast message text: '{}'", actualText);

            Assert.assertTrue(actualText.toLowerCase().contains("success") ||
                            actualText.toLowerCase().contains("successfully") ||
                            actualText.toLowerCase().contains("sent"),
                    "ERROR: Success message does not contain expected content! Received: " + actualText);

            LOGGER.info("SUCCESS: Attendance excuse submitted successfully.");

        } catch (TimeoutException e) {
            LOGGER.error("ERROR: Success message did not appear within the designated timeout period (15s)!");
            Assert.fail("Test failed: 'Success' notification was not displayed.");
        }
    }
}
