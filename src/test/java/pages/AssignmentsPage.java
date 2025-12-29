package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pages.base.BasePage;
import java.util.List;

import static utils.BaseDriver.getDriver;

public class AssignmentsPage extends BasePage {

    @FindBy(xpath = "//ms-layout-menu-button[@page='ASSIGNMENT']")
    private WebElement assignmentsLogo;

    @FindBy(css = ".cdk-overlay-pane")
    private WebElement assignmentsCountOverlay;

    WebDriver driver = getDriver();
    Actions actions = new Actions(driver);

    public AssignmentsPage(WebDriver driver) {
        super(driver);
    }

    public void verifyAssignmentsLogoDisplayed() {
        LOGGER.info("Verifying Assignments logo is displayed");
        Assert.assertTrue(isDisplayed(assignmentsLogo), "Assignments logo is not displayed");
    }

    public void hoverOverAssignmentsLogo() {
        LOGGER.info("Hovering over Assignments logo");
        actions.moveToElement(assignmentsLogo).perform();
    }

    public void verifyAssignmentsCountOnHover() {
        LOGGER.info("Verifying assignments count on hover");

        actions.moveToElement(assignmentsLogo).perform();
        LOGGER.info("Hovered over Assignments link");

        Assert.assertTrue(isDisplayed(assignmentsCountOverlay),"Assignments count overlay is not displayed on hover");

        String countText = assignmentsCountOverlay.getText().trim();
        LOGGER.info("Assignments count text from overlay: [{}]", countText);

        Assert.assertFalse(countText.isEmpty(),"Assignments count text is empty");

        Assert.assertTrue(countText.matches("\\d+"),"Assignments count is not numeric: " + countText);

        LOGGER.info("Assignments count verified successfully: {}", countText);
    }

    public void clickAssignmentsLogo() {
        LOGGER.info("Clicking Assignments logo");
        clickElement(assignmentsLogo);
    }

    public void verifyAssignedTasksDisplayed() {

        By assignedTasks = By.xpath("//*[@id='container-3']//*[contains(text(),'B8')]");
        List<WebElement> tasks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(assignedTasks));

        Assert.assertFalse(tasks.isEmpty(), "Assigned tasks list is empty");

        LOGGER.info("Total assigned tasks: {}", tasks.size());

        for (WebElement task : tasks) {
            String text = task.getText().trim();
            LOGGER.debug("Task content: {}", text);
            Assert.assertTrue(text.contains("B8"), "Non-B8 task found: " + text);
        }
    }
}