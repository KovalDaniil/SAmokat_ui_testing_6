package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import test.data.OrderData;

import java.time.Duration;

public class ScooterPage {
    private static final String SCOOTER_URL = "https://qa-scooter.education-services.ru/";
    private final WebDriver driver;
    private final WebDriverWait wait;

    //====================СПИСОК ЭЛЕМЕНТОВ СТРАНИЦЫ====================

    // Кнопка принятия cookie-уведомления «да все привыкли»
    private final By cookieButton = By.id("rcc-confirm-button");
    // Верхняя кнопка «Заказать» (первая точка входа в сценарий заказа)
    private final By orderUpButton = By.xpath(
            "//button[@class='Button_Button__ra12g' and text()='Заказать']");
    // Нижняя кнопка «Заказать» (вторая точка входа, в футере страницы)
    private final By orderDownButton = By.xpath(
            "//button[@class='Button_Button__ra12g Button_Middle__1CSJM' and text()='Заказать']");
    // Заголовок раздела «Вопросы о важном»
    private final By questionsTitle = By.xpath("//*[text()='Вопросы о важном']");
    // ---- Форма «Для кого самокат» ----
    private final By forWhomScooterTitle = By.xpath("//*[text()='Для кого самокат']");
    private final By firstNameField = By.xpath("//input[@placeholder='* Имя']");
    private final By lastNameField = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroStationField = By.xpath("//input[@placeholder='* Станция метро']");
    // Опция «Черкизовская» в списке станций метро
    private final By metroCherkizovskayaOption = By.xpath("//div[contains(text(),'Черкизовская')]");
    private final By phoneNumberField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[contains(text(), 'Далее')]");
    // ---- Форма «Про аренду» ----
    private final By aboutRentTitle = By.xpath("//*[text()='Про аренду']");
    private final By deliveryDateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodDropdownArrow = By.className("Dropdown-arrow");
    private final By periodDayOption = By.xpath("//div[text()='сутки']");
    private final By periodMonthOption = By.xpath("//div[text()='четверо суток']");
    private final By colorBlackPearlCheckbox = By.id("black");
    private final By colorGrayDesolationCheckbox = By.id("grey");
    private final By courierCommentField = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By placeOrderButton = By.xpath("//button[@class='Button_Button__ra12g Button_Middle__1CSJM' and text()='Заказать']");
    // --Всплывающие окна--
    private final By confirmOrderPromptText = By.xpath("//*[contains(text(),'Хотите оформить заказ?')]");
    private final By yesButton = By.xpath("//button[text()='Да']");
    private final By orderDoneTitle = By.xpath("//*[contains(text(),'Заказ оформлен')]");
    public ScooterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    /* Открыть страницу и убрать cookie-уведомление, если оно появилось. */
    public void open() {
        driver.get(SCOOTER_URL);
        try {
            WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(cookieButton));
            button.click();
        } catch (Exception ignored) {
            // уведомления может и не быть — тогда ничего не делаем
        }
    }

    public void clickOrderUpButton() {
        wait.until(ExpectedConditions.elementToBeClickable(orderUpButton)).click();
    }

    public void clickOrderDownButton() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(orderDownButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        wait.until(ExpectedConditions.elementToBeClickable(orderDownButton)).click();
    }

    /* Клик по стрелке вопроса «Вопросы о важном» с указанным номером (0..7). */
    public void clickQuestionByIndex(int index) {
        By heading = By.id("accordion__heading-" + index);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(heading));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(heading)).click();
    }

    /* Текст ответа вопроса с указанным номером. */
    public String getAnswerTextByIndex(int index) {
        By panel = By.id("accordion__panel-" + index);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(panel)).getText();
    }

    /* Заполнить форму «Для кого самокат». */
    public void fillForWhomForm(OrderData data) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(forWhomScooterTitle));
        driver.findElement(firstNameField).sendKeys(data.firstName);
        driver.findElement(lastNameField).sendKeys(data.lastName);
        driver.findElement(addressField).sendKeys(data.address);
        driver.findElement(metroStationField).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(metroCherkizovskayaOption)).click();
        driver.findElement(phoneNumberField).sendKeys(data.phone);
    }

    /* Заполнить форму «Про аренду»: дата, срок, цвет, комментарий. */
    public void fillAboutRentForm(OrderData data) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(aboutRentTitle));
        driver.findElement(deliveryDateField).clear();
        driver.findElement(deliveryDateField).sendKeys(data.date);
        driver.findElement(rentalPeriodDropdownArrow).click();
        if (data.period.equals("четверо суток")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(periodMonthOption)).click();
        } else {
            wait.until(ExpectedConditions.visibilityOfElementLocated(periodDayOption)).click();
        }
        WebElement colorCheckbox;
        if (data.color.equals("черный")) {
            colorCheckbox = driver.findElement(colorBlackPearlCheckbox);
        } else {
            colorCheckbox = driver.findElement(colorGrayDesolationCheckbox);
        }
        if (!colorCheckbox.isSelected()) {
            colorCheckbox.click();
        }
        driver.findElement(courierCommentField).clear();
        driver.findElement(courierCommentField).sendKeys(data.comment);
    }
    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickPlaceOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
    }
    /* В окне «Хотите оформить заказ?» нажать «Да». */
    public void acceptOrderPrompt() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmOrderPromptText));
        wait.until(ExpectedConditions.elementToBeClickable(yesButton)).click();
    }
    /* Появилось ли окно об успешном создании заказа. */
    public boolean isOrderSuccessPopupVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(orderDoneTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /* Открылась ли форма «Для кого самокат» (проверка точек входа). */
    public boolean isForWhomFormOpened() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(forWhomScooterTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}