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

public class HamburgerMenu_ManagingTrashSteps {

    private static final Logger log = LogManager.getLogger(HamburgerMenu_ManagingTrashSteps.class);
    HamburgerMenuPage hp = new HamburgerMenuPage(BaseDriver.getDriver());
    WebDriverWait wait = new WebDriverWait(BaseDriver.getDriver(), Duration.ofSeconds(15));
    JavascriptExecutor js = (JavascriptExecutor) BaseDriver.getDriver();

    @When("the user navigates to {string} under {string}")
    public void the_user_navigates_to_under(String child, String parent) {
        log.info("STEP: Navigating to {} under {} menu.", child, parent);

        log.debug("Waiting for Messaging menu header...");
        WebElement messagingMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Messaging')]")));
        messagingMenu.click();

        log.debug("Waiting for the {} link to be visible...", child);
        wait.until(ExpectedConditions.visibilityOf(hp.trash));
        js.executeScript("arguments[0].click();", hp.trash);
        log.info("Navigation SUCCESS: Currently in the Trash folder.");
    }

    @When("the user clicks on the {string} icon for a deleted message")
    public void click_restore_icon(String iconType) {
        log.info("STEP: Clicking on the '{}' icon to recover a message.", iconType);

        wait.until(driver -> !hp.restoreButtonList.isEmpty());
        log.debug("Restore button list populated. Clicking the first available item.");
        js.executeScript("arguments[0].click();", hp.restoreButtonList.get(0));
        log.info("Restore icon clicked.");
    }

    @When("the user clicks on the {string} icon to permanently delete a message")
    public void click_delete_icon(String iconType) {
        log.info("STEP: Clicking on the '{}' icon for permanent deletion.", iconType);

        wait.until(driver -> !hp.trashCanButtonList.isEmpty());
        log.debug("Delete button list populated. Clicking the first available item.");
        js.executeScript("arguments[0].click();", hp.trashCanButtonList.get(0));
        log.info("Permanent delete icon clicked.");
    }

    @Then("the message should be removed from the list successfully")
    @Then("the message should no longer be visible in the Trash list")
    @And("the message should be completely removed from the system")
    public void verify_success_status() {
        log.info("STEP: Verifying successful removal from the Trash list.");

        wait.until(ExpectedConditions.visibilityOf(hp.successMessage));
        Assert.assertTrue(hp.successMessage.isDisplayed(), "ERROR: Success notification was not displayed!");
        log.info("Verification SUCCESS: System message: '{}'", hp.successMessage.getText());
    }

    @Then("a confirmation pop-up should be displayed with a message")
    public void aConfirmationPopUpShouldBeDisplayedWithAMessage() {
        log.info("STEP: Verifying the visibility of the confirmation pop-up.");
        wait.until(ExpectedConditions.visibilityOf(hp.deleteMsg));
        log.info("INFO: Confirmation pop-up is visible.");
    }

    @When("the user confirms the permanent deletion")
    public void the_user_confirms_the_permanent_deletion() {
        log.info("STEP: Confirming permanent deletion (Clicking Delete/Confirm button).");

        wait.until(ExpectedConditions.elementToBeClickable(hp.deleteButon));
        log.debug("Delete button located. Performing JavaScript click.");
        js.executeScript("arguments[0].click();", hp.deleteButon);
        log.info("Permanent deletion confirmed.");
    }
}
