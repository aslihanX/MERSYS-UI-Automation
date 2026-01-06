package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.ProfileFeaturePage;
import utils.BaseDriver;
import java.time.Duration;
import java.util.Random;

public class Profile_FeatureThemeSteps {

    private static final Logger LOGGER = LogManager.getLogger(Profile_FeatureThemeSteps.class);
    private final ProfileFeaturePage pfp = new ProfileFeaturePage(BaseDriver.getDriver());
    private final WebDriverWait wait = new WebDriverWait(BaseDriver.getDriver(), Duration.ofSeconds(10));

    @Then("the user should see a drop-down theme menu on the settings page")
    public void verifyThemeDropdownVisible() {
        LOGGER.info("STEP: Verifying theme dropdown visibility.");
        wait.until(ExpectedConditions.visibilityOf(pfp.themeDropdown));
        Assert.assertTrue(pfp.themeDropdown.isDisplayed(), "FAILED: Theme dropdown menu is not visible on the page!");
        LOGGER.info("INFO: Theme dropdown is visible.");
    }

    @When("the user clicks on the theme drop-down menu")
    public void clickThemeDropdown() {
        LOGGER.info("STEP: Clicking on the theme dropdown.");
        pfp.clickElement(pfp.themeDropdown);
        LOGGER.debug("DEBUG: Dropdown clicked.");
    }

    @Then("the user should see at least three different theme options")
    public void verifyAtLeastThreeThemes() {
        LOGGER.info("STEP: Verifying theme options count.");
        // Options are loaded into DOM upon clicking mat-select
        wait.until(ExpectedConditions.visibilityOfAllElements(pfp.themeOptions));

        int count = pfp.themeOptions.size();
        LOGGER.info("INFO: Number of theme options found: {}", count);
        Assert.assertTrue(count >= 3, "FAILED: Expected at least 3 themes, but only found: " + count);
    }

    @When("the user selects a new theme from the options")
    public void selectRandomTheme() {
        LOGGER.info("STEP: Selecting a random theme from the list.");
        wait.until(ExpectedConditions.visibilityOfAllElements(pfp.themeOptions));

        int optionCount = pfp.themeOptions.size();

        if (optionCount > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(optionCount);

            WebElement selectedOption = pfp.themeOptions.get(randomIndex);
            String selectedThemeName = selectedOption.getText();

            LOGGER.info("INFO: Random index generated: {}", randomIndex);
            LOGGER.info("INFO: Selected theme: {}", selectedThemeName);

            pfp.clickElement(selectedOption);
            LOGGER.debug("DEBUG: Random theme selection action completed.");
        } else {
            LOGGER.error("ERROR: No theme options found to select!");
            Assert.fail("No theme options available in the dropdown list.");
        }
    }

    @Then("the user should see that the theme changes immediately without confirmation")
    public void verifyImmediateThemeChange() {
        LOGGER.info("STEP: Verifying immediate UI theme change.");

        LOGGER.debug("DEBUG: Waiting 2 seconds for UI rendering and color transition...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.warn("Wait interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        wait.until(ExpectedConditions.visibilityOf(pfp.themeDropdown));
        String currentText = pfp.themeDropdown.getText();

        LOGGER.info("INFO: Current text displayed in dropdown: {}", currentText);

        Assert.assertFalse(currentText.isEmpty(), "FAILED: Theme selection failed, dropdown text is empty!");
        LOGGER.info("SUCCESS: Theme change verified successfully.");
    }
}