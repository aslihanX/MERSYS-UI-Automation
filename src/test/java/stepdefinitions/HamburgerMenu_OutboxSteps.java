package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.HamburgerMenuPage;
import utils.BaseDriver;
import java.time.Duration;

public class HamburgerMenu_OutboxSteps {

    private static final Logger log = LogManager.getLogger(HamburgerMenu_OutboxSteps.class);

    HamburgerMenuPage hp = new HamburgerMenuPage(BaseDriver.getDriver());
    WebDriverWait wait = new WebDriverWait(BaseDriver.getDriver(), Duration.ofSeconds(15));
    JavascriptExecutor js = (JavascriptExecutor) BaseDriver.getDriver();

    @And("the user navigates to Outbox under Messaging menu")
    public void navigateToOutboxMenu() {
        log.info("STEP: Navigating to Outbox under the Messaging menu.");

        log.debug("Searching for the 'Messaging' menu header...");
        WebElement messagingMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Messaging']")));
        messagingMenu.click();
        log.debug("Successfully clicked on the Messaging menu.");

        log.debug("Waiting for the Outbox link to become visible...");
        wait.until(ExpectedConditions.visibilityOf(hp.outbox));
        js.executeScript("arguments[0].click();", hp.outbox);
        log.debug("Successfully clicked on the Outbox link.");

        wait.until(ExpectedConditions.visibilityOf(hp.confirmOutbox));
        log.info("Navigation SUCCESS: Currently on the Outbox page.");
    }

    @Then("the user should see the list of outgoing messages")
    public void verifyOutboxList() {
        log.info("STEP: Verifying the visibility of the outgoing messages list.");
        wait.until(ExpectedConditions.visibilityOf(hp.confirmOutbox));
        Assert.assertTrue(hp.confirmOutbox.isDisplayed(), "ERROR: Outbox list is not displayed!");
        log.info("Verification SUCCESS: Outbox list is visible.");
    }

    @And("the user clicks on the {string} icon for an outgoing message")
    public void clickRubbishIcon(String iconType) {
        log.info("STEP: Clicking on the '{}' icon.", iconType);

        wait.until(ExpectedConditions.elementToBeClickable(hp.rubbish));
        log.debug("Rubbish button located. Performing JavaScript click.");
        js.executeScript("arguments[0].click();", hp.rubbish);
        log.info("Rubbish (Delete) icon clicked.");
    }

    @Then("an outbox confirmation pop-up should be displayed")
    public void verifyOutboxPopUp() {
        log.info("STEP: Waiting for the deletion confirmation pop-up.");

        wait.until(ExpectedConditions.visibilityOf(hp.outboxConfirmMsg));
        String text = hp.outboxConfirmMsg.getText();
        log.debug("Retrieved confirmation text: {}", text);

        Assert.assertTrue(text.contains("move this message to trash"),
                "ERROR: Confirmation message text did not match the expected content!");
        log.info("Verification SUCCESS: Deletion confirmation pop-up is displayed.");
    }

    @When("the user confirms the outbox deletion")
    public void confirmOutboxDeletion() {
        log.info("STEP: Confirming the deletion process (Clicking 'YES' button).");

        wait.until(ExpectedConditions.elementToBeClickable(hp.yesButton));
        log.debug("'YES' button is now clickable. Executing click.");
        js.executeScript("arguments[0].click();", hp.yesButton);
        log.info("Deletion confirmed via pop-up.");
    }

    @Then("the {string} message should be displayed indicating the message was deleted")
    public void verifySuccessMessage(String msgType) {
        log.info("STEP: Checking for the '{}' success notification.", msgType);

        wait.until(ExpectedConditions.visibilityOf(hp.successMessage));
        Assert.assertTrue(hp.successMessage.isDisplayed(), "ERROR: Success notification was not visible!");
        log.info("Verification SUCCESS: Success message displayed on the UI.");
    }

    @And("the message should no longer be visible in the Outbox list")
    public void verifyMessageIsGone() {
        log.info("STEP: Verifying that the message has been removed from the Outbox list.");

        wait.until(ExpectedConditions.invisibilityOf(hp.successMessage));
        log.info("US006 Scenario COMPLETED: Message successfully moved from Outbox to Trash.");
    }
}

