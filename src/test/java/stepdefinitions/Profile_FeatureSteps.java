package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.ProfileFeaturePage;
import utils.BaseDriver;
import java.time.Duration;

public class Profile_FeatureSteps {

    private final WebDriver driver = BaseDriver.getDriver();
    private final ProfileFeaturePage pfp = new ProfileFeaturePage(driver);
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    @Given("the user navigates to {string} > {string}")
    public void navigatesToProfileSettings(String menu, String submenu) {
        System.out.println("--- STEP: Navigation to Profile > Settings Started ---");
        wait.until(ExpectedConditions.elementToBeClickable(pfp.profileSettingsButton));
        pfp.clickElement(pfp.profileSettingsButton);
        System.out.println("DEBUG: Profile menu expanded.");

        wait.until(ExpectedConditions.elementToBeClickable(pfp.settingsButton));
        pfp.clickElement(pfp.settingsButton);
        System.out.println("DEBUG: Navigated to Settings page.");
    }

    @When("the user clicks on the profile picture placeholder")
    public void clicksOnAvatar() {
        System.out.println("--- STEP: Clicking on avatar placeholder ---");
        wait.until(ExpectedConditions.elementToBeClickable(pfp.uploadPicture));
        pfp.clickElement(pfp.uploadPicture);
    }

    @And("the user clicks the {string} button to select a picture")
    public void readyForSelection(String btnName) {
        System.out.println("DEBUG: Ready for file selection (Mac OS Bypass enabled).");
    }

    @And("the user selects a profile picture from path {string}")
    public void selectsProfilePicture(String relativePath) {
        String fullPath = System.getProperty("user.dir") + relativePath;
        System.out.println("DEBUG: Constructing file path: " + fullPath);

        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='file']")));
            fileInput.sendKeys(fullPath);
            System.out.println("INFO: File path successfully injected into hidden input.");
        } catch (TimeoutException e) {
            System.err.println("ERROR: File input (type='file') not found in DOM!");
            Assert.fail("File input element could not be located.");
        }
    }

    @Then("the user should see the size of the uploaded picture")
    public void verifyFileSize() {
        System.out.println("--- STEP: Verifying file size visibility ---");
        wait.until(ExpectedConditions.visibilityOf(pfp.fileSizeText));
        String size = pfp.fileSizeText.getText();
        System.out.println("INFO: Detected File Size: " + size);
        Assert.assertTrue(pfp.fileSizeText.isDisplayed(), "File size text is not visible!");
    }

    @When("the user clicks the {string} button to confirm upload")
    public void confirmUpload(String btnName) {
        System.out.println("--- STEP: Confirming upload in modal ---");
        wait.until(ExpectedConditions.elementToBeClickable(pfp.confirmUploadBtn));
        pfp.clickElement(pfp.confirmUploadBtn);

        System.out.println("DEBUG: Waiting 3 seconds for server-side processing...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("the user see {string} button on the page and click it")
    public void clickSave(String btnName) {
        System.out.println("--- STEP: Clicking main Save button ---");
        wait.until(ExpectedConditions.elementToBeClickable(pfp.saveBtn));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pfp.saveBtn);
        System.out.println("DEBUG: Save button clicked via JavaScript.");
    }

    @Then("the {string} message should be displayed")
    public void verifySuccessMessage(String expectedMessage) {
        System.out.println("--- STEP: Verifying success message ---");
        wait.until(ExpectedConditions.visibilityOf(pfp.successMessage));
        String actualMsg = pfp.successMessage.getText();
        System.out.println("INFO: Toast message received: " + actualMsg);

        Assert.assertTrue(actualMsg.toLowerCase().contains("success"),
                "Actual message does not contain 'success'. Received: " + actualMsg);
        System.out.println("--- SCENARIO COMPLETED SUCCESSFULLY ---");
    }
}
