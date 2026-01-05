package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pages.base.BasePage;

public class FinancePage extends BasePage {

    public FinancePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[normalize-space()='Students Fees']")
    private WebElement studentsFeesHeader;


    public void verifyFinancePageOpened() {
        LOGGER.info("Verifying Student Finance page is opened");
        wait.until(ExpectedConditions.visibilityOf(studentsFeesHeader));
        Assert.assertTrue(studentsFeesHeader.isDisplayed(),
                "Student Finance page is not opened");
    }


    public boolean isFinancePageOpened() {
        try {
            return isDisplayed(studentsFeesHeader);
        } catch (Exception e) {
            return false;
        }
    }
}
