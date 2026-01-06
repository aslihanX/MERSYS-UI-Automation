package stepdefinitions;

import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.HamburgerMenuPage;
import pages.SubmissionPage;
import utils.BaseDriver;
import java.time.Duration;

public class HamburgerMenu_SendMsgSteps {
    private static final Logger LOGGER = LogManager.getLogger(HamburgerMenu_SendMsgSteps.class);
    HamburgerMenuPage hp = new HamburgerMenuPage(BaseDriver.getDriver());
    SubmissionPage submissionPage = new SubmissionPage(BaseDriver.getDriver());

    @Then("a text editor pop-up should be displayed")
    public void a_text_editor_pop_up_should_be_displayed() {
        LOGGER.info("STEP: Verifying that the message editor window is displayed.");
        Assert.assertTrue(hp.isDisplayed(hp.receiversIcon), "ERROR: Message editor failed to open!");
    }

    @And("the user should see and click the {string} icon")
    public void the_user_should_see_and_click_the_icon(String iconName) {
        LOGGER.info("STEP: Clicking on the '{}' icon.", iconName);
        hp.clickElement(hp.receiversIcon);
    }

    @When("the user types {string} in the receiver field")
    public void the_user_types_in_the_receiver_field(String searchKey) {
        LOGGER.info("STEP: Typing '{}' into the receiver search field.", searchKey);
        hp.sendKeysToElement(hp.addReceivers, searchKey);
        hp.wait(2);
    }

    @Then("the system should display the results for {string}")
    public void theSystemShouldDisplayTheResultsFor(String receiverType) {
        LOGGER.info("STEP: Checking search results for receiver type: {}.", receiverType);
        WebDriverWait wait = new WebDriverWait(BaseDriver.getDriver(), Duration.ofSeconds(10));

        if (receiverType.equalsIgnoreCase("Teacher")) {
            // --- NORMAL FLOW FOR TEACHER ---
            try {
                wait.until(driver -> hp.receiverTableRows.size() > 0);
                LOGGER.info("SUCCESS: Teacher list populated successfully.");
            } catch (Exception e) {
                LOGGER.error("ERROR: Teacher list is empty!");
                Assert.fail("ERROR: Teacher search returned no results!");
            }
        }
        else if (receiverType.equalsIgnoreCase("Student")) {
            // --- BUG DETECTION LOGIC FOR STUDENT ---
            try {
                wait.until(ExpectedConditions.visibilityOf(hp.noDataMessage));

                if (hp.noDataMessage.isDisplayed()) {
                    LOGGER.error("!!! BUG DETECTED: Student search returned 'No data' message!");
                    throw new AssertionError("!!! BUG REPORT: Student search failed. System displayed 'There is no data to show you right now!' instead of actual records.");
                }
            } catch (Exception e) {
                if (hp.receiverTableRows.size() > 0) {
                    LOGGER.info("INFO: Student list populated, proceeding with test.");
                } else {
                    LOGGER.error("ERROR: Student search returned neither data nor the 'No Data' message.");
                    Assert.fail("Student search did not return a list or a 'No Data' notification.");
                }
            }
        }
    }

    @And("the user selects a random available receiver")
    public void theUserSelectsARandomAvailableReceiver() {
        LOGGER.info("STEP: Selecting a random receiver from the list.");

        if (hp.receiverTableRows.size() > 0) {
            int randomIndex = (int) (Math.random() * hp.receiverTableRows.size());
            WebElement selectedElement = hp.receiverTableRows.get(randomIndex);

            LOGGER.info("INFO: Selected receiver name: {}", selectedElement.getText());

            WebDriverWait wait = new WebDriverWait(BaseDriver.getDriver(), Duration.ofSeconds(10));

            JavascriptExecutor js = (JavascriptExecutor) BaseDriver.getDriver();
            js.executeScript("arguments[0].click();", selectedElement);

            wait.until(ExpectedConditions.elementToBeClickable(hp.addClose));

            js.executeScript("arguments[0].click();", hp.addClose);
            LOGGER.info("INFO: 'Add & Close' button clicked.");
        } else {
            LOGGER.error("ERROR: Receiver list is empty. Cannot select a random receiver.");
            Assert.fail("No receivers found to select!");
        }
    }

    @And("the user enters a subject as {string}")
    public void the_user_enters_a_subject_as(String subjectText) {
        LOGGER.info("STEP: Entering subject text: {}", subjectText);
        hp.sendKeysToElement(hp.subjectInput, subjectText);
    }

    @And("the user types {string} in the text editor")
    public void the_user_types_in_the_text_editor(String content) {
        LOGGER.info("STEP: Writing message content.");
        BaseDriver.getDriver().switchTo().frame(hp.messageIframe);

        hp.sendKeysToElement(hp.messageInputBody, content);
        LOGGER.debug("INFO: Content injected into iframe body.");
        BaseDriver.getDriver().switchTo().defaultContent();
    }

    @And("the user clicks on the {string} button")
    public void the_user_clicks_on_the_button(String buttonName) {
        LOGGER.info("STEP: Clicking on the '{}' button.", buttonName);
        hp.clickElement(hp.sendBtn);
    }

    @Then("a {string} notification should be displayed")
    public void a_notification_should_be_displayed(String expectedMessage) {
        LOGGER.info("STEP: Verifying the success notification.");
        Assert.assertTrue(
                submissionPage.isDraftSuccessMessageDisplayed(),
                "ERROR: Success message notification was not displayed!"
        );
        LOGGER.info("SUCCESS: Notification confirmed.");
    }

    @And("the user clicks on the {string} link and then {string} link")
    public void theUserClicksOnTheLinkAndThenLink(String menu, String folder) {
        LOGGER.info("STEP: Navigating to {} > {}.", menu, folder);
        hp.clickElement(hp.messaging);
        hp.wait(3);
        hp.clickElement(hp.outbox);
    }

    @Then("the message with subject {string} should be visible in the Outbox list")
    public void the_message_with_subject_should_be_visible_in_the_outbox_list(String subjectText) {
        LOGGER.info("STEP: Searching for subject '{}' in the Outbox.", subjectText);
        hp.wait(15);
        boolean isFound = hp.outboxTableRows.stream()
                .anyMatch(row -> row.getText().contains(subjectText));

        if(isFound) {
            LOGGER.info("SUCCESS: Message with subject '{}' found in Outbox.", subjectText);
        } else {
            LOGGER.error("ERROR: Message with subject '{}' NOT found in Outbox!", subjectText);
        }

        Assert.assertTrue(isFound, "Sent message subject not found in the Outbox list!");
    }
}
