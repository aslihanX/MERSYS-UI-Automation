package stepdefinitions;

import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.ProfileFeaturePage;
import utils.BaseDriver;
import java.time.Duration;

public class Profile_FeatureSteps {

    private static final Logger LOGGER = LogManager.getLogger(Profile_FeatureSteps.class);
    private final WebDriver driver = BaseDriver.getDriver();
    private final ProfileFeaturePage pfp = new ProfileFeaturePage(driver);
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    @Given("the user navigates to {string} > {string}")
    public void navigatesToProfileSettings(String menu, String submenu) {
        LOGGER.info("STEP: Navigating to Profile > Settings.");
        wait.until(ExpectedConditions.elementToBeClickable(pfp.profileSettingsButton));
        pfp.clickElement(pfp.profileSettingsButton);
        LOGGER.debug("Profile menu button clicked.");

        wait.until(ExpectedConditions.elementToBeClickable(pfp.settingsButton));
        pfp.clickElement(pfp.settingsButton);
        LOGGER.info("Successfully navigated to the Settings page.");
    }

    @When("the user clicks on the profile picture placeholder")
    public void clicksOnAvatar() {
        LOGGER.info("STEP: Clicking on the profile picture placeholder.");
        wait.until(ExpectedConditions.elementToBeClickable(pfp.uploadPicture));
        pfp.clickElement(pfp.uploadPicture);
    }

    @And("the user clicks the {string} button to select a picture")
    public void readyForSelection(String btnName) {
        LOGGER.debug("INFO: System ready for file selection. Bypassing OS file picker for Mac compatibility.");
    }

    @And("the user selects a profile picture from path {string}")
    public void selectsProfilePicture(String relativePath) {
        String fullPath = System.getProperty("user.dir") + relativePath;
        LOGGER.info("STEP: Uploading picture from path: {}", fullPath);

        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='file']")));
            fileInput.sendKeys(fullPath);
            LOGGER.info("SUCCESS: File path successfully injected into the hidden file input.");
        } catch (TimeoutException e) {
            LOGGER.error("CRITICAL: File input (type='file') not found in DOM!");
            Assert.fail("Failed to locate the file input element.");
        }
    }

    @Then("the user should see the size of the uploaded picture")
    public void verifyFileSize() {
        LOGGER.info("STEP: Verifying visibility of the uploaded file size.");
        wait.until(ExpectedConditions.visibilityOf(pfp.fileSizeText));
        String size = pfp.fileSizeText.getText();
        LOGGER.info("Retrieved file size: {}", size);
        Assert.assertTrue(pfp.fileSizeText.isDisplayed(), "File size label is not visible!");
    }

    @When("the user clicks the {string} button to confirm upload")
    public void confirmUpload(String btnName) {
        LOGGER.info("STEP: Clicking the '{}' button in the upload modal.", btnName);
        wait.until(ExpectedConditions.elementToBeClickable(pfp.confirmUploadBtn));
        pfp.clickElement(pfp.confirmUploadBtn);

        LOGGER.debug("Waiting 3 seconds for backend server synchronization.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LOGGER.warn("Wait interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @And("the user see {string} button on the page and click it")
    public void clickSave(String btnName) {
        LOGGER.info("STEP: Clicking the main '{}' button on the settings page.", btnName);
        wait.until(ExpectedConditions.elementToBeClickable(pfp.saveBtn));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pfp.saveBtn);
        LOGGER.debug("Save button clicked via JavaScript to ensure execution.");
    }

    @Then("the {string} message should be displayed")
    public void verifySuccessMessage(String expectedMessage) {
        LOGGER.info("STEP: Verifying display of the success notification.");
        wait.until(ExpectedConditions.visibilityOf(pfp.successMessage));
        String actualMsg = pfp.successMessage.getText();
        LOGGER.info("Toast notification received: '{}'", actualMsg);

        Assert.assertTrue(actualMsg.toLowerCase().contains("success"),
                "Success message was not detected! Received: " + actualMsg);
        LOGGER.info("SCENARIO COMPLETED: Profile picture update verified.");
    }
}

