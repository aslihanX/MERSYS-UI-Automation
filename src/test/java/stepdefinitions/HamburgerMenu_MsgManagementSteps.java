package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.HamburgerMenuPage;
import utils.BaseDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class HamburgerMenu_MsgManagementSteps {

    private static final Logger LOGGER = LogManager.getLogger(HamburgerMenu_MsgManagementSteps.class);

    WebDriver driver = BaseDriver.getDriver();
    HamburgerMenuPage hp = new HamburgerMenuPage(driver);
    Actions actions = new Actions(driver);

    @When("the user clicks on the {string} icon")
    public void the_user_clicks_on_the_icon(String iconName) {
        LOGGER.info("STEP: Clicking on the Hamburger Menu icon.");
        hp.clickElement(hp.hamburgerMenu);
    }

    @When("the user hovers over the {string} menu item")
    public void the_user_hovers_over_the_menu_item(String menuName) {
        LOGGER.info("STEP: Hovering over the '{}' menu item.", menuName);
        hp.wait.until(ExpectedConditions.visibilityOf(hp.messaging));
        actions.moveToElement(hp.messaging).perform();
        LOGGER.debug("Hover action performed on messaging element.");
    }

    @Then("the following sub-menu options should be visible:")
    public void verify_sub_menu_options(io.cucumber.datatable.DataTable dataTable) {
        LOGGER.info("STEP: Verifying visibility of sub-menu options.");

        WebDriverWait wait = new WebDriverWait(BaseDriver.getDriver(), Duration.ofSeconds(15));

        Assert.assertTrue(hp.isDisplayed(hp.inbox), "Inbox option is not visible!");
        LOGGER.info("Verification Passed: Inbox is visible.");

        Assert.assertTrue(hp.isDisplayed(hp.outbox), "Outbox option is not visible!");
        LOGGER.info("Verification Passed: Outbox is visible.");

        wait.until(ExpectedConditions.visibilityOf(hp.trash));
        LOGGER.info("DEBUG: Trash element became visible after wait.");

        Assert.assertTrue(hp.isDisplayed(hp.trash), "Trash option is not visible!");
        LOGGER.info("Verification Passed: Trash is visible.");

        // Checking for the Naming Bug
        String actualText = hp.sendMessage.getText();
        LOGGER.info("INFO: Text retrieved from system button: '{}'", actualText);

        // Logical check for the Naming Bug reporting
        if (!"New Message".equals(actualText)) {
            LOGGER.error("!!! BUG DETECTED: Requirement expects 'New Message', but found '{}' in the system.", actualText);
        }

        Assert.assertEquals(actualText, "New Message", "The button text does not match the requirement!");
    }

    @And("the user clicks on the {string} link")
    public void click_on_link(String linkName) {
        LOGGER.info("STEP: Clicking on the '{}' link.", linkName);
        hp.clickElement(hp.sendMessage);
        LOGGER.debug("Click action executed on sendMessage element.");
    }
}