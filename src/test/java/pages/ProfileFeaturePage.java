package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.base.BasePage;

public class ProfileFeaturePage extends BasePage {

    @FindBy(xpath= "//span[@class='username mr-12']")
    public WebElement profileSettingsButton;

    @FindBy(xpath= "//span[text()='Settings']")
    public WebElement settingsButton;

    @FindBy(xpath = "//img[contains(@class, 'profile-image') and contains(@class, 'avatar')]")
    public WebElement uploadPicture;

    @FindBy(xpath = "//*[contains(text(),'MB') or contains(text(),'KB')]")
    public WebElement fileSizeText;

    @FindBy(xpath = "//button[.//span[contains(text(),'Upload')]]")
    public WebElement confirmUploadBtn;

    @FindBy(xpath = "//span[text()='Save']//ancestor::button")
    public WebElement saveBtn;

    @FindBy(xpath = "//div[contains(text(),'Success') or contains(text(),'successfully')]")
    public WebElement successMessage;












    public ProfileFeaturePage(WebDriver driver) {
        super(driver);
    }
}
